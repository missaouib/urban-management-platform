package com.unicom.urban.management.service.bigscreen;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.dao.event.EventRepository;
import com.unicom.urban.management.pojo.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        /*处置数*/
        map.put("dispose",events.stream().filter(e -> e.getStatisticsList().stream().anyMatch(s -> 1 == s.getDispose())).count());
        /*事件数*/
        map.put("event",events.stream().filter(e->"事件".equals(e.getEventType().getParent().getParent().getName())).count());
        /*部件数*/
        map.put("unit",events.stream().filter(e->"部件".equals(e.getEventType().getParent().getParent().getName())).count());
        /*公众举报*/
        map.put("hotline",events.stream().filter(e->KvConstant.HOTLINE.equals(e.getEventSource().getValue())).count());
        /*监督员上报*/
        map.put("supervise",events.stream().filter(e->KvConstant.SUPERVISE.equals(e.getEventSource().getValue())).count());
        return map;
    }
}
