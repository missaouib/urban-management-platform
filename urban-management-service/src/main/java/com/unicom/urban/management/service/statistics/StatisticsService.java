package com.unicom.urban.management.service.statistics;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.dao.statistics.StatisticsRepository;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

    public List<StatisticsVO> findByEventId(String eventId) {
        /*查询流程数据*/
        List<Statistics> statisticsList = statisticsRepository.findAllByEvent_Id(eventId);
        List<StatisticsVO> statisticsVOS = new ArrayList<>();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        /*重新封装*/
        statisticsList.forEach(s -> {
            /*开始时间str*/
            String starTime = df.format(s.getStartTime());
            /*结束时间str*/
            String endTime = "";
            /*如果没有结束事件 就以当前时间判断超没超时*/
            LocalDateTime end = LocalDateTime.now();
            /*sign 0 没超时  1超时*/
            String sign = "0";
            if(s.getEndTime() != null){
                endTime = df.format(s.getEndTime());
                end = s.getEndTime();
            }
            long timeLimit = s.getProcessTimeLimit().getTimeLimit();
            long timeNum;
            /*工作时*/
            if(KvConstant.HOUR.equals(s.getProcessTimeLimit().getLevel().getId())){
                timeNum = timeLimit * 60 * 60 *1000;
            }else{
                /*工作日*/
                timeNum = timeLimit * 24 * 60 * 60 * 1000;
            }
            Duration between = Duration.between(s.getStartTime(), end);
            long millis = between.toMillis();
            if(millis>timeNum){
                sign = "1";
            }
            List<String> file = new ArrayList<>();
            s.getEventFileList().forEach(f-> file.add(f.getFileName()));
            StatisticsVO statisticsVO = StatisticsVO.builder()
                    .starTime(starTime)
                    .endTime(endTime)
                    .sign(sign)
                    .opinions(s.getOpinions())
                    .fileName(file)
                    .user(s.getUser().getName())
                    .link(s.getTaskName())
                    .taskId(s.getTaskId())
                    .taskName(s.getTaskName())
                    .build();
            statisticsVOS.add(statisticsVO);
        });
        return statisticsVOS;
    }

}
