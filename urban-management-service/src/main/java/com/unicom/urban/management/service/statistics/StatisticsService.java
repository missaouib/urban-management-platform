package com.unicom.urban.management.service.statistics;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.statistics.StatisticsRepository;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
        List<Statistics> statisticsList = statisticsRepository.findAllByEvent_IdOrderByStartTimeDesc(eventId);
        List<StatisticsVO> statisticsVOS = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        /*重新封装*/
        statisticsList.forEach(s -> {
            /*开始时间str*/
            String starTime = df.format(s.getStartTime());
            /*结束时间str*/
            String endTime = "";
            /*如果没有结束事件 就以当前时间判断超没超时*/
            if (s.getEndTime() != null) {
                endTime = df.format(s.getEndTime());
            }
            List<String> file = new ArrayList<>();
            s.getEventFileList().forEach(f -> file.add(f.getFileName()));
            StatisticsVO statisticsVO = StatisticsVO.builder()
                    .starTime(starTime)
                    .endTime(endTime)
                    .opinions(Optional.ofNullable(s.getOpinions()).orElse("无"))
                    .fileName(file)
                    .user(Optional.ofNullable(s.getUser()).map(User::getUsername).orElse(""))
                    .link(s.getTaskName())
                    .taskId(s.getTaskId())
                    .taskName(s.getTaskName())
                    .sts(s.getSts())
                    .build();
            statisticsVOS.add(statisticsVO);
        });
        return statisticsVOS;
    }


    public List<String> getEventIdByMe(){
        List<Statistics> allByUser_id = statisticsRepository.findAllByUser_IdAndEndTimeIsNotNull(SecurityUtil.getUserId());
        List<String> list = new ArrayList<>();
        allByUser_id.forEach(a->list.add(a.getEvent().getId()));
      return list.stream().distinct().collect(Collectors.toList());
    }

}
