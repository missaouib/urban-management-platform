package com.unicom.urban.management.service.supervise;

import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.dao.supervise.SuperviseRepository;
import com.unicom.urban.management.pojo.dto.SuperviseDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.entity.Supervise;
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


    public void save(SuperviseDTO superviseDTO){
        Event event = eventService.findOne(superviseDTO.getEventId());
        List<Statistics> collect = event.getStatisticsList().stream().filter(s -> null == s.getEndTime()).collect(Collectors.toList());
        Supervise supervise = new Supervise();
        supervise.setEvent(event);
        if(collect.size()>0){
            supervise.setDept(collect.get(0).getDisposeUnit());
        }
        supervise.setDeleted(superviseDTO.getOpinion());
        supervise.setUser(SecurityUtil.getUser().castToUser());
        superviseRepository.save(supervise);
    }
}
