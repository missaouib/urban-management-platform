package com.unicom.urban.management.service.bigscreen;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.dao.event.EventRepository;
import com.unicom.urban.management.pojo.entity.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author 顾志杰
 * @date 2020/11/23-18:19
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class IndexService {
    private static final NumberFormat nt;

    static {
        nt = NumberFormat.getPercentInstance();
        nt.setMinimumFractionDigits(2);
    }

    @Autowired
    private EventRepository eventRepository;


    public Map<String, Object> count(String timeType) {
        Map<String, Object> map = new ConcurrentHashMap<>();
        List<Event> events = eventRepository.findAll((Specification<Event>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            if ("year".equals(timeType)) {
                now = now.with(TemporalAdjusters.firstDayOfYear()).toLocalDate().atStartOfDay();
            } else if ("month".equals(timeType)) {
                now = now.with(TemporalAdjusters.firstDayOfMonth()).toLocalDate().atStartOfDay();
            } else {
                now = now.toLocalDate().atStartOfDay();
            }
            list.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createTime").as(LocalDateTime.class), now));
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        });
        /*上报数*/
        map.put("count", events.size());
        /*结案数*/
        map.put("close", events.stream().filter(e -> Optional.ofNullable(e.getEventSate()).isPresent()).filter(e -> KvConstant.CLOS_ETESK.equals(e.getEventSate().getId())).count());
        /*事件*/
        List<Event> event = events.stream().filter(e -> "事件".equals(e.getEventType().getParent().getParent().getName())).collect(Collectors.toList());
        /*事件应处置数*/
        long eventNum = event.stream().filter(e -> e.getStatisticsList().stream().anyMatch(s -> 1 == s.getInst())).count();
        map.put("eventNum", eventNum);
        /*事件处置数*/
        long eventDisponse = event.stream().filter(e -> e.getStatisticsList().stream().anyMatch(s -> 1 == s.getDispose())).count();
        map.put("eventDisponse", eventDisponse);
        /*事件处置率*/
        String eventRate = this.getRate(eventDisponse, eventNum);
        map.put("eventRate", eventRate.length() > 1 ? eventRate.substring(0, eventRate.length() - 1) : eventRate);
        /*部件*/
        List<Event> unit = events.stream().filter(e -> "部件".equals(e.getEventType().getParent().getParent().getName())).collect(Collectors.toList());
        /*部件应处置数*/
        long unitNum = unit.stream().filter(e -> e.getStatisticsList().stream().filter(s -> Optional.ofNullable(s.getInst()).isPresent()).anyMatch(s -> 1 == s.getInst())).count();
        map.put("unitNum", unitNum);
        /*部件处置数*/
        long unitDisponse = unit.stream().filter(e -> e.getStatisticsList().stream().filter(s -> Optional.ofNullable(s.getInst()).isPresent()).anyMatch(s -> 1 == s.getDispose())).count();
        map.put("unitDisponse", unitDisponse);
        /*立案数*/
        map.put("inst", eventNum + unitNum);
        /*部件处置率*/
        String unitRate = this.getRate(unitDisponse, unitNum);
        map.put("unitRate", unitRate.length() > 1 ? unitRate.substring(0, unitRate.length() - 1) : unitRate);

        return map;
    }

    private String getRate(Long num1, Long num2) {
        if (num2 == 0) {
            return "0";
        }
        Double closeRate = (double) (num1) / (double) num2;
        return Optional.of(nt.format(closeRate)).filter(s -> !"NaN".equals(s)).orElse("0");
    }


}
