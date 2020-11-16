package com.unicom.urban.management.service.statistics;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.statistics.StatisticsRepository;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
        for (Statistics s : statisticsList){
            eventIdList.add(s.getEvent().getId());
        }
        return eventIdList;
    }

    public StatisticsVO findById(String statisticsId) {
        Statistics statistics = statisticsRepository.findById(statisticsId).orElse(new Statistics());
        StatisticsVO statisticsVO = new StatisticsVO();
        statisticsVO.setOpinions(statistics.getOpinions());
        if(statistics.getUser() != null){
            statisticsVO.setUserName(statistics.getUser().getName());
            statisticsVO.setDeptName("");
        }
        return statisticsVO;
    }
}
