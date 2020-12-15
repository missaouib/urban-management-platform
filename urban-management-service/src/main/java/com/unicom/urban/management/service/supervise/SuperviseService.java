package com.unicom.urban.management.service.supervise;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.supervise.SuperviseRepository;
import com.unicom.urban.management.pojo.dto.SuperviseDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.entity.Supervise;
import com.unicom.urban.management.pojo.vo.SuperviseVO;
import com.unicom.urban.management.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 顾志杰
 * @date 2020/11/19-13:44
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class SuperviseService {

    @Autowired
    private SuperviseRepository superviseRepository;
    @Autowired
    private EventService eventService;


    public void save(SuperviseDTO superviseDTO) {
        Event event = eventService.findOne(superviseDTO.getEventId());
        List<Statistics> collect = event.getStatisticsList().stream().filter(s -> null == s.getEndTime()).collect(Collectors.toList());
        Supervise supervise = new Supervise();
        supervise.setEvent(event);
        if (collect.size() > 0) {
            supervise.setDept(collect.get(0).getDisposeUnit());
        }
        supervise.setOpinion(superviseDTO.getOpinion());
        supervise.setUser(SecurityUtil.getUser().castToUser());
        superviseRepository.save(supervise);
    }

    public void reply(SuperviseDTO superviseDTO) {
        List<Supervise> supervises = superviseRepository.findAllByEvent_Id(superviseDTO.getEventId());
        if (supervises.size() > 0) {
            supervises.get(0).setReplayOpinion(superviseDTO.getReplayOpinion());
            superviseRepository.saveAndFlush(supervises.get(0));
        }
    }

    public SuperviseVO superviseVO(String eventId) {
        Event event = eventService.findOne(eventId);
        if (event.getSupervises().size() > 0) {
            Supervise supervise = event.getSupervises().get(0);
            return SuperviseVO.builder()
                    .eventId(eventId)
                    .deptName(supervise.getDept().getDeptName())
                    .user(supervise.getUser().getName())
                    .opinion(supervise.getOpinion())
                    .replayOpinion(supervise.getReplayOpinion())
                    .build();
        } else {
            List<Statistics> collect = event.getStatisticsList().stream().filter(s -> null == s.getEndTime()).collect(Collectors.toList());
            if(collect.size()>0){
                Statistics statistics = collect.get(0);
                return SuperviseVO.builder()
                        .eventId(eventId)
                        .deptName(statistics.getDisposeUnit().getDeptName())
                        .opinion("")
                        .replayOpinion("")
                        .build();
            }
          throw new DataValidException("查询异常");
        }
    }
}
