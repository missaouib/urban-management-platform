package com.unicom.urban.management.service.home;

import com.unicom.urban.management.dao.event.EventRepository;
import com.unicom.urban.management.dao.eventtype.EventTypeRepository;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
public class HomeService {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventTypeRepository eventTypeRepository;

    public List<Map<String,Object>> eventTypeCount(String type){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime week = now.minusDays(now.getDayOfWeek().getValue() - 1);
        LocalDateTime month = now.with(TemporalAdjusters.firstDayOfMonth());
        List<Event> all = eventRepository.findAll();
        List<Event> weekList = all.stream().filter(e -> e.getCreateTime().isAfter(week.toLocalDate().atStartOfDay())).collect(Collectors.toList());
        List<Event> monthList = all.stream().filter(e -> e.getCreateTime().isAfter(month.toLocalDate().atStartOfDay())).collect(Collectors.toList());
        List<EventType> unit = eventTypeRepository.findAllByLevelAndParent_Name("1", type);
        List<Map<String,Object>> list = new ArrayList<>();
        unit.forEach(u->{
            Map<String,Object> map = new ConcurrentHashMap<>();
            map.put("typeName",u.getName());
            map.put("weekCount",weekList.stream().filter(w-> Optional.ofNullable(w.getEventType().getParent()).isPresent()).filter(w->w.getEventType().getParent().getName().equals(u.getName())).count());
            map.put("monthCount", monthList.stream().filter(w -> Optional.ofNullable(w.getEventType().getParent()).isPresent()).filter(w -> w.getEventType().getParent().getName().equals(u.getName())).count());
            map.put("count", all.stream().filter(w -> Optional.ofNullable(w.getEventType().getParent()).isPresent()).filter(w -> w.getEventType().getParent().getName().equals(u.getName())).count());
            list.add(map);
        });
        return list;
    }
}
