package com.unicom.urban.management.service.statistics;

import com.unicom.urban.management.common.exception.DataValidException;
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
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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

    public List<Statistics> findAllByEventIdOrderBySort(String eventId) {
        return statisticsRepository.findAllByEvent_IdOrderBySortDesc(eventId);
    }

    /**
     * 高发区域
     * @return mapList
     */
    public List<Map<String,Object>> findHotGrid(){
        List<Map<String,Object>> hotGridList = new ArrayList<>();
        List<Map<String,Object>> mapList = statisticsRepository.findHotGrid();
        if (mapList != null && mapList.size() > 0) {
            Map<String,Object> hotMap = new HashMap<>(3);
            for (Map<String, Object> map : mapList) {
                hotMap.put("totalInst",map.get("totalInst").toString());
                hotMap.put("totalClose",map.get("totalClose").toString());
                hotMap.put("gridName",findFirstGrid(map.get("gridId").toString()));
                hotGridList.add(hotMap);
            }
        }
        return hotGridList;
    }

    private String findFirstGrid(String gridId) {
        Grid grid = gridService.findOne(gridId);
        return grid.getParent().getParent().getParent().getGridName();
    }

    /* 大屏 趋势分析 */
    public List<Map<String, Object>> getTrendAnalysis(String time) {
        final String year = "year";
        final String month = "month";
        final String day = "day";
        switch (time) {
            case year:
                return getTrendAnalysisForYear();
            case month:
                return getTrendAnalysisForMonth();
            case day:
                return getTrendAnalysisForDay();
            default:
                throw new DataValidException("请正确选择日期");
        }
    }

    /* 大屏 趋势分析 年 */
    private List<Map<String, Object>> getTrendAnalysisForYear() {
        /* 格式化 */
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        /* 获取当前时间 */
        LocalDateTime now = LocalDateTime.now();
        /* 本年 */
        int year = now.getYear();
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> reportMap = new HashMap<>(2);
        reportMap.put("name", "上报量");
        Map<String, Object> operateMap = new HashMap<>(2);
        operateMap.put("name", "受理量");
        Map<String, Object> instMap = new HashMap<>(2);
        instMap.put("name", "立案量");
        Map<String, Object> dispatchMap = new HashMap<>(2);
        dispatchMap.put("name", "派遣量");
        Map<String, Object> disposeMap = new HashMap<>(2);
        disposeMap.put("name", "处置量");
        Map<String, Object> checkNumMap = new HashMap<>(2);
        checkNumMap.put("name", "核查量");
        Map<String, Object> closeMap = new HashMap<>(2);
        closeMap.put("name", "结案量");
        for (int i = 0; i < 7; i++) {
            int reportSize = statisticsRepository.findByReport(
                    LocalDateTime.parse((year - i) + "-01-01 00:00:01", df),
                    LocalDateTime.parse((year - i) + "-12-31 23:59:59", df)).size();
            Map<String, Object> reportSizeMap = new HashMap<>(2);
            reportSizeMap.put("time" + i, (year - i));
            reportSizeMap.put("size" + i, reportSize);
            reportMap.put("data" + i, reportSizeMap);

            int operateSize = statisticsRepository.findByOperate(
                    LocalDateTime.parse((year - i) + "-01-01 00:00:01", df),
                    LocalDateTime.parse((year - i) + "-12-31 23:59:59", df)).size();
            Map<String, Object> operateSizeMap = new HashMap<>(2);
            operateSizeMap.put("time" + i, (year - i));
            operateSizeMap.put("size" + i, operateSize);
            operateMap.put("data" + i, operateSizeMap);

            int instSize = statisticsRepository.findByInst(
                    LocalDateTime.parse((year - i) + "-01-01 00:00:01", df),
                    LocalDateTime.parse((year - i) + "-12-31 23:59:59", df)).size();
            Map<String, Object> instSizeMap = new HashMap<>(2);
            instSizeMap.put("time" + i, (year - i));
            instSizeMap.put("size" + i, instSize);
            instMap.put("data" + i, instSizeMap);

            int dispatchSize = statisticsRepository.findByDispatch(
                    LocalDateTime.parse((year - i) + "-01-01 00:00:01", df),
                    LocalDateTime.parse((year - i) + "-12-31 23:59:59", df)).size();
            Map<String, Object> dispatchSizeMap = new HashMap<>(2);
            dispatchSizeMap.put("time" + i, (year - i));
            dispatchSizeMap.put("size" + i, dispatchSize);
            dispatchMap.put("data" + i, dispatchSizeMap);

            int disposeSize = statisticsRepository.findByDispose(
                    LocalDateTime.parse((year - i) + "-01-01 00:00:01", df),
                    LocalDateTime.parse((year - i) + "-12-31 23:59:59", df)).size();
            Map<String, Object> disposeSizeMap = new HashMap<>(2);
            disposeSizeMap.put("time" + i, (year - i));
            disposeSizeMap.put("size" + i, disposeSize);
            disposeMap.put("data" + i, disposeSizeMap);

            int checkNumSize = statisticsRepository.findByCheckNum(
                    LocalDateTime.parse((year - i) + "-01-01 00:00:01", df),
                    LocalDateTime.parse((year - i) + "-12-31 23:59:59", df)).size();
            Map<String, Object> checkNumSizeMap = new HashMap<>(2);
            checkNumSizeMap.put("time" + i, (year - i));
            checkNumSizeMap.put("size" + i, checkNumSize);
            checkNumMap.put("data" + i, checkNumSizeMap);

            int closeSize = statisticsRepository.findByClose(
                    LocalDateTime.parse((year - i) + "-01-01 00:00:01", df),
                    LocalDateTime.parse((year - i) + "-12-31 23:59:59", df)).size();
            Map<String, Object> closeSizeMap = new HashMap<>(2);
            closeSizeMap.put("time" + i, (year - i));
            closeSizeMap.put("size" + i, closeSize);
            closeMap.put("data" + i, closeSizeMap);
        }
        mapList.add(reportMap);
        mapList.add(operateMap);
        mapList.add(instMap);
        mapList.add(dispatchMap);
        mapList.add(disposeMap);
        mapList.add(checkNumMap);
        mapList.add(closeMap);
        return mapList;
    }

    /* 大屏 趋势分析 月 */
    private List<Map<String, Object>> getTrendAnalysisForMonth() {
        /* 格式化 */
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        /* 获取当前时间 */
        LocalDateTime now = LocalDateTime.now();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDateTime startLocalTime = now.with(TemporalAdjusters.firstDayOfMonth()).minusMonths(i).with(TemporalAdjusters.firstDayOfMonth());
            String startTime = dateTime.format(startLocalTime);
            LocalDateTime endLocalTime = now.with(TemporalAdjusters.lastDayOfMonth()).minusMonths(i).with(TemporalAdjusters.lastDayOfMonth());
            String endTime = dateTime.format(endLocalTime);
            int reportSize = statisticsRepository.findByReport(
                    LocalDateTime.parse(startTime + " 00:00:01", df),
                    LocalDateTime.parse(endTime + " 23:59:59", df)).size();
            int operateSize = statisticsRepository.findByOperate(
                    LocalDateTime.parse(startTime + " 00:00:01", df),
                    LocalDateTime.parse(endTime + " 23:59:59", df)).size();
            int instSize = statisticsRepository.findByInst(
                    LocalDateTime.parse(startTime + " 00:00:01", df),
                    LocalDateTime.parse(endTime + " 23:59:59", df)).size();
            int dispatchSize = statisticsRepository.findByDispatch(
                    LocalDateTime.parse(startTime + " 00:00:01", df),
                    LocalDateTime.parse(endTime + " 23:59:59", df)).size();
            int disposeSize = statisticsRepository.findByDispose(
                    LocalDateTime.parse(startTime + " 00:00:01", df),
                    LocalDateTime.parse(endTime + " 23:59:59", df)).size();
            int checkNumSize = statisticsRepository.findByCheckNum(
                    LocalDateTime.parse(startTime + " 00:00:01", df),
                    LocalDateTime.parse(endTime + " 23:59:59", df)).size();
            int closeSize = statisticsRepository.findByClose(
                    LocalDateTime.parse(startTime + " 00:00:01", df),
                    LocalDateTime.parse(endTime + " 23:59:59", df)).size();
        }
        return mapList;
    }

    /* 大屏 趋势分析 日 */
    private List<Map<String, Object>> getTrendAnalysisForDay() {
        /* 格式化 */
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        /* 获取当前时间 */
        LocalDateTime now = LocalDateTime.now();
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDateTime localDateTime = now.minusDays(i);
            String localDate = dateTime.format(localDateTime);
            int reportSize = statisticsRepository.findByReport(
                    LocalDateTime.parse(localDate + " 00:00:01", df),
                    LocalDateTime.parse(localDate + " 23:59:59", df)).size();
            int operateSize = statisticsRepository.findByOperate(
                    LocalDateTime.parse(localDate + " 00:00:01", df),
                    LocalDateTime.parse(localDate + " 23:59:59", df)).size();
            int instSize = statisticsRepository.findByInst(
                    LocalDateTime.parse(localDate + " 00:00:01", df),
                    LocalDateTime.parse(localDate + " 23:59:59", df)).size();
            int dispatchSize = statisticsRepository.findByDispatch(
                    LocalDateTime.parse(localDate + " 00:00:01", df),
                    LocalDateTime.parse(localDate + " 23:59:59", df)).size();
            int disposeSize = statisticsRepository.findByDispose(
                    LocalDateTime.parse(localDate + " 00:00:01", df),
                    LocalDateTime.parse(localDate + " 23:59:59", df)).size();
            int checkNumSize = statisticsRepository.findByCheckNum(
                    LocalDateTime.parse(localDate + " 00:00:01", df),
                    LocalDateTime.parse(localDate + " 23:59:59", df)).size();
            int closeSize = statisticsRepository.findByClose(
                    LocalDateTime.parse(localDate + " 00:00:01", df),
                    LocalDateTime.parse(localDate + " 23:59:59", df)).size();
        }
        return mapList;
    }

    /* 首页 各项数值 */
    public Map<String, Object> getIndexValueByWeek() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monday = now.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).plusDays(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime sunday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1).withHour(23).withMinute(59).withSecond(59);
        int reportSize = statisticsRepository.findByReport(monday, sunday).size();
        int instSize = statisticsRepository.findByInst(monday, sunday).size();
        int dispatchSize = statisticsRepository.findByDispatch(monday, sunday).size();
        int closeSize = statisticsRepository.findByClose(monday, sunday).size();
        Map<String, Object> map = new HashMap<>(4);
        map.put("report", reportSize);
        map.put("inst", instSize);
        map.put("dispatch", dispatchSize);
        map.put("close", closeSize);
        return map;
    }

    public Map<String, Object> findPersonInfo() {
        Map<String, Object> personMap = new HashMap<>();
        String roleName = SecurityUtil.getRoleName().get(0);
        String userId = SecurityUtil.getUserId();
        Integer takeCase = statisticsRepository.findTakeCase(userId);
        Integer instCase = statisticsRepository.findInstCase(userId);
        Integer closeCase = statisticsRepository.findCloseCase(userId);
        personMap.put("roleName",roleName);
        personMap.put("takeCase",takeCase);
        personMap.put("instCase",instCase);
        personMap.put("closeCase",closeCase);
        return personMap;
    }

    public Map<String, Object> findEventSource() {
        Map<String, Object> map = new HashMap<>();
        Integer reportPatrolNum = statisticsRepository.findReportPatrolNum();
        Integer reportSelfNum = statisticsRepository.findReportSelfNum();
        map.put("reportPatrolNum", reportPatrolNum);
        map.put("reportSelfNum", reportSelfNum);
        return map;
    }

    /* 大屏 高发问题 */
    public List<Map<String, String>> findHighIncidence() {
        List<Map<String, String>> highIncidenceByInst = findHighIncidenceByInst();
        findHighIncidenceByClose(highIncidenceByInst);
        return highIncidenceByInst;
    }

    /* 大屏 高发问题 立案数 */
    private List<Map<String, String>> findHighIncidenceByInst() {
        return statisticsRepository.findHighIncidenceByInst();
    }

    /* 大屏 高发问题 增加结案数 */
    private void findHighIncidenceByClose(List<Map<String, String>> statisticsVOList) {
        for (Map<String, String> map : statisticsVOList) {
            Map<String, String> newMap = new HashMap<>(map);
            Map<String, String> highIncidenceByClose = statisticsRepository.findHighIncidenceByClose(newMap.get("id"));
            map.put("totalClose", highIncidenceByClose.get("totalClose"));
        }
    }

}
