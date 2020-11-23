package com.unicom.urban.management.service.statistics;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.statistics.StatisticsRepository;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.CellGridRegionVO;
import com.unicom.urban.management.pojo.vo.StatisticsVO;
import com.unicom.urban.management.service.grid.GridService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 流转统计实体类
 *
 * @author jiangwen
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class StatisticsService {

    @Autowired
    private StatisticsRepository statisticsRepository;
    @Autowired
    private GridService gridService;

    public Statistics save(Statistics statistics) {
        return statisticsRepository.save(statistics);
    }

    public void update(Statistics statistics) {

        statisticsRepository.saveAndFlush(statistics);
    }

    public Statistics findByEventIdAndEndTimeIsNull(String eventId) {
        return statisticsRepository.findByEvent_IdAndEndTimeIsNull(eventId);
    }

    public List<Statistics> findByEventIdToList(String eventId) {
        return statisticsRepository.findAllByEvent_IdOrderByStartTimeDesc(eventId);
    }

    public List<StatisticsVO> findByEventId(String eventId) {
        /*查询流程数据*/
        List<Statistics> statisticsList = statisticsRepository.findAllByEvent_IdOrderBySortDesc(eventId);
        List<StatisticsVO> statisticsVOList = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        /*重新封装*/
        statisticsList.forEach(statistics -> {
            /*开始时间str*/
            String starTime = dateTimeFormatter.format(statistics.getStartTime());
            /*结束时间str*/
            String endTime = "";
            /*如果没有结束事件 就以当前时间判断超没超时*/
            if (statistics.getEndTime() != null) {
                endTime = dateTimeFormatter.format(statistics.getEndTime());
            }
            List<Map<String, Object>> stringList = new ArrayList<>();
            statistics.getEventFileList().forEach(eventFile -> {
                Map<String, Object> map = new HashMap<>(3);
                map.put("url", eventFile.getFilePath());
                map.put("type", eventFile.getFileType().getValue());
                map.put("management", eventFile.getManagement().getValue());
                stringList.add(map);
            });
            StatisticsVO statisticsVO = StatisticsVO.builder()
                    .starTime(starTime)
                    .endTime(endTime)
                    .opinions(Optional.ofNullable(statistics.getOpinions()).orElse("无"))
                    .fileName(stringList)
                    .user(Optional.ofNullable(statistics.getUser()).map(User::getUsername).orElse(""))
                    .link(statistics.getTaskName())
                    .taskId(statistics.getTaskId())
                    .taskName(statistics.getTaskName())
                    .sts(statistics.getSts())
                    .build();
            statisticsVOList.add(statisticsVO);
        });
        return statisticsVOList;
    }


    public List<String> getEventIdByMe() {
        List<Statistics> allByUserId = statisticsRepository.findAllByUser_IdAndEndTimeIsNotNull(SecurityUtil.getUserId());
        List<String> list = new ArrayList<>();
        allByUserId.forEach(a -> list.add(a.getEvent().getId()));
        return list.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getEventIdByHang() {
        List<Statistics> allByHangIsNot = statisticsRepository.findAllByHang(1);
        List<String> list = new ArrayList<>();
        allByHangIsNot.forEach(a -> list.add(a.getEvent().getId()));
        return list.stream().distinct().collect(Collectors.toList());
    }

    public List<String> getEventIdByCancel() {
        List<Statistics> allByHangIsNot = statisticsRepository.findAllByCancel(1);
        List<String> list = new ArrayList<>();
        allByHangIsNot.forEach(a -> list.add(a.getEvent().getId()));
        return list.stream().distinct().collect(Collectors.toList());
    }

    public List<String> findEventIdByNotOperate(Integer notOperate) {
        List<Statistics> statisticsList = statisticsRepository.findByNotOperate(notOperate);
        List<String> eventIdList = new ArrayList<>();
        for (Statistics s : statisticsList) {
            eventIdList.add(s.getEvent().getId());
        }
        return eventIdList;
    }

    public StatisticsVO findById(String statisticsId) {
        Statistics statistics = statisticsRepository.findById(statisticsId).orElse(new Statistics());
        StatisticsVO statisticsVO = new StatisticsVO();
        statisticsVO.setOpinions(statistics.getOpinions());
        if (statistics.getUser() != null) {
            statisticsVO.setUserName(statistics.getUser().getName());
            statisticsVO.setDeptName("");
        }
        return statisticsVO;
    }

    /**
     * 区域评价
     *
     * @return 数据
     */
    public Page<CellGridRegionVO> findAllForCellGridRegion(String time1, String time2, Pageable pageable) {
        LocalDateTime startTime = getTimeForBetween(time1, time2).get("startTime");
        LocalDateTime endTime = getTimeForBetween(time1, time2).get("endTime");
        List<Grid> gridList = gridService.allByLevelAndRecordSts();
        List<CellGridRegionVO> cellGridRegionVOList = new ArrayList<>();
        for (Grid grid : gridList) {
            CellGridRegionVO cellGridRegionVO = new CellGridRegionVO();
            cellGridRegionVO.setGridName(grid.getGridName());
            Grid community = grid.getParent();
            cellGridRegionVO.setCommunityName(community.getGridName());
            Grid street = community.getParent();
            cellGridRegionVO.setStreetName(street.getGridName());
            int inTimeCloseSize = statisticsRepository.findAllByInTimeClose(grid.getId(), startTime, endTime).size();
            int closeSize = statisticsRepository.findAllByClose(grid.getId(), startTime, endTime).size();
            int closeOrToCloseSize = statisticsRepository.findAllByCloseOrToClose(grid.getId(), startTime, endTime).size();
            int publicReportAndInstSize = statisticsRepository.findAllByPublicReportAndInst(grid.getId(), startTime, endTime).size();
            int instSize = statisticsRepository.findAllByInst(grid.getId(), startTime, endTime).size();
            /* 按期结案数 */
            cellGridRegionVO.setInTimeCloseSize(inTimeCloseSize);
            /* 结案数 */
            cellGridRegionVO.setCloseSize(closeSize);
            /* 应结案数 */
            cellGridRegionVO.setCloseOrToCloseSize(closeOrToCloseSize);
            /* 监督举报核实数 */
            cellGridRegionVO.setPublicReportAndInstSize(publicReportAndInstSize);
            /* 立案数 */
            cellGridRegionVO.setInstSize(instSize);
            /* 监督举报率：监督举报核实数/立案数×100% */
            cellGridRegionVO.setPublicReportAndInstRate(getRateByDouble(publicReportAndInstSize, instSize));
            /* 结案率：结案数/应结案数×100%*/
            cellGridRegionVO.setCloseRate(getRateByDouble(closeSize, closeOrToCloseSize));
            /* 按期结案率：按期结案数/应结案数×100%*/
            cellGridRegionVO.setInTimeCloseRate(getRateByDouble(inTimeCloseSize, closeOrToCloseSize));
            double publicReportAndInstRate = 100D - Double.parseDouble(cellGridRegionVO.getPublicReportAndInstRate().split("%")[0]);
            double closeRate = 100D - Double.parseDouble(cellGridRegionVO.getCloseRate().split("%")[0]);
            double inTimeCloseRate = 100D - Double.parseDouble(cellGridRegionVO.getInTimeCloseRate().split("%")[0]);
            double instSizeValue;
            if (instSize == 0) {
                instSizeValue = 100;
            } else if (instSize < 3) {
                instSizeValue = 90;
            } else if (instSize < 5) {
                instSizeValue = 75;
            } else if (instSize < 7) {
                instSizeValue = 60;
            } else if (instSize < 9) {
                instSizeValue = 40;
            } else {
                instSizeValue = 0;
            }
            double comprehensiveIndexValue = publicReportAndInstRate * 0.1 + instSizeValue * 0.2 + closeRate * 0.3 + inTimeCloseRate * 0.1;
            BigDecimal bigDecimal = BigDecimal.valueOf(comprehensiveIndexValue);
            double f1 = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            cellGridRegionVO.setComprehensiveIndexValue(f1);
            cellGridRegionVOList.add(cellGridRegionVO);
        }
        return new PageImpl<>(cellGridRegionVOList, pageable, 0);
    }

    private String getRateByDouble(int a, int b) {
        double percent;
        if (b == 0) {
            percent = 0;
        } else {
            percent = (double) a / (double) b;
        }
        NumberFormat nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);
        return nt.format(percent);
    }

    private Map<String, LocalDateTime> getTimeForBetween(String startTime, String endTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Map<String, LocalDateTime> map = new HashMap<>(2);
        if (StringUtils.isNotBlank(startTime)) {
            map.put("startTime", LocalDateTime.parse(startTime, df));
        } else {
            map.put("startTime", LocalDateTime.parse("1970-01-01 00:00:00", df));
        }
        if (StringUtils.isNotBlank(endTime)) {
            map.put("endTime", LocalDateTime.parse(endTime, df));
        } else {
            map.put("endTime", LocalDateTime.now());
        }
        return map;
    }

    public List<Statistics> findAllByEventIdOrderBySort(String eventId){
        return statisticsRepository.findAllByEvent_IdOrderBySortDesc(eventId);
    }
}
