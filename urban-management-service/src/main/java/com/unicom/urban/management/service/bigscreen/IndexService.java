package com.unicom.urban.management.service.bigscreen;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.dao.event.EventRepository;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 顾志杰
 * @date 2020/11/23-18:19
 */
@Service
public class IndexService {

    @Autowired
    private EventRepository eventRepository;


    public Map count() {
        Map<String, Object> map = new HashMap<>();
        List<Event> events = eventRepository.findAll();
        /*上报数*/
        map.put("count", events.size());
        /*结案数*/
        map.put("close", events.stream().filter(e -> KvConstant.CLOS_ETESK.equals(e.getEventSate().getId())).count());
        /*事件*/
        List<Event> event = events.stream().filter(e -> "事件".equals(e.getEventType().getParent().getParent().getName())).collect(Collectors.toList());
        /*事件应处置数*/
        long eventNum = event.stream().filter(e -> e.getStatisticsList().stream().anyMatch(s -> 1 == s.getInst())).count();
        /*事件处置数*/
        long eventDisponse = event.stream().filter(e -> e.getStatisticsList().stream().anyMatch(s -> 1 == s.getDispose())).count();
        /*部件*/
        List<Event> unit = events.stream().filter(e -> "部件".equals(e.getEventType().getParent().getParent().getName())).collect(Collectors.toList());
        /*部件应处置数*/
        long unitNum = unit.stream().filter(e -> e.getStatisticsList().stream().anyMatch(s -> 1 == s.getInst())).count();
        /*部件处置数*/
        long unitDisponse = unit.stream().filter(e -> e.getStatisticsList().stream().anyMatch(s -> 1 == s.getDispose())).count();
        /*立案数*/
        map.put("inst", eventNum+unitNum);

        return map;
    }


}
