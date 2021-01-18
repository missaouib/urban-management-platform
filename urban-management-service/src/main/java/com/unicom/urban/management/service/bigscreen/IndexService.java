package com.unicom.urban.management.service.bigscreen;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.dao.event.EventRepository;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    @Autowired
    private EventTypeService eventTypeService;

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
        /*处置数*/
        long dispose = event.stream().filter(e -> e.getStatisticsList().stream().anyMatch(s -> 1 == s.getDispose())).count();
        map.put("dispose", dispose);
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

    public List<Map<String, String>> showPoints(String timeType, String showType) {
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
        List<Map<String, String>> showPointList = new ArrayList<>();
        //上报
        if ("reporting".equals(showType)) {
            for (Event event : events) {
                Map<String, String> map1 = new HashMap<>();
                map1.put("eventId", event.getId());
                map1.put("coordinate", event.getX() + " " + event.getY());
                map1.put("eventCode", event.getEventCode());
                map1.put("represent", event.getRepresent());
                String name1 = event.getEventType().getParent().getParent().getName();
                String name2 = event.getEventType().getParent().getName();
                String name3 = event.getEventType().getName();
                map1.put("eventTypeName", name1 + "-" + name2 + "-" + name3);
                showPointList.add(map1);
            }
            //立案
        } else if ("register".equals(showType)) {
            for (Event event : events) {
                for (Statistics statistics : event.getStatisticsList()) {
                    if (statistics.getInst() == 1) {
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("eventId", event.getId());
                        map1.put("coordinate", event.getX() + " " + event.getY());
                        map1.put("eventCode", event.getEventCode());
                        map1.put("represent", event.getRepresent());
                        String name1 = event.getEventType().getParent().getParent().getName();
                        String name2 = event.getEventType().getParent().getName();
                        String name3 = event.getEventType().getName();
                        map1.put("eventTypeName", name1 + "-" + name2 + "-" + name3);
                        showPointList.add(map1);
                    }
                }
            }
            //处置
        } else if ("caseDispose".equals(showType)) {
            for (Event event : events) {
                for (Statistics statistics : event.getStatisticsList()) {
                    if (statistics.getDispose() == 1) {
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("eventId", event.getId());
                        map1.put("coordinate", event.getX() + " " + event.getY());
                        map1.put("eventCode", event.getEventCode());
                        map1.put("represent", event.getRepresent());
                        String name1 = event.getEventType().getParent().getParent().getName();
                        String name2 = event.getEventType().getParent().getName();
                        String name3 = event.getEventType().getName();
                        map1.put("eventTypeName", name1 + "-" + name2 + "-" + name3);
                        showPointList.add(map1);
                    }
                }
            }
            //结案
        } else if ("caseclosed".equals(showType)) {
            for (Event event : events) {
                for (Statistics statistics : event.getStatisticsList()) {
                    if (statistics.getClose() == 1) {
                        Map<String, String> map1 = new HashMap<>();
                        map1.put("eventId", event.getId());
                        map1.put("coordinate", event.getX() + " " + event.getY());
                        map1.put("eventCode", event.getEventCode());
                        map1.put("represent", event.getRepresent());
                        String name1 = event.getEventType().getParent().getParent().getName();
                        String name2 = event.getEventType().getParent().getName();
                        String name3 = event.getEventType().getName();
                        map1.put("eventTypeName", name1 + "-" + name2 + "-" + name3);
                        showPointList.add(map1);
                    }
                }
            }
        }
        return showPointList;
    }

    public List<Map<String, Object>> caseAnalysisList(EventDTO eventDTO) {
        List<Event> eventList = eventRepository.findAll((Specification<Event>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            /* 问题描述 */
            if (StringUtils.isNotBlank(eventDTO.getRepresent())) {
                list.add(criteriaBuilder.like(root.get("represent").as(String.class), "%" + eventDTO.getRepresent() + "%"));
            }
            /* 所在网格 */
            if (StringUtils.isNotBlank(eventDTO.getGrid())) {
                list.add(criteriaBuilder.equal(root.get("grid").get("id").as(String.class), eventDTO.getGrid()));
            }
            /* 问题来源 */
            if (StringUtils.isNotBlank(eventDTO.getEventSourceId())) {
                list.add(criteriaBuilder.equal(root.get("eventSource").get("id").as(String.class), eventDTO.getEventSourceId()));
            }
            /* 案件类型 */
            if (StringUtils.isNotBlank(eventDTO.getEventTypeId())) {
                CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("eventType").get("id"));
                List<String> type = eventTypeService.getComponentTypeIds(eventDTO.getEventTypeId());
                type.forEach(in::value);
                list.add(in);
            }
            /* 立案区域 */
            if (StringUtils.isNotBlank(eventDTO.getEventCondition())) {
                list.add(criteriaBuilder.equal(root.get("condition").get("parent").get("id").as(String.class), eventDTO.getEventCondition()));
            }
            /* 立案条件 */
            if (StringUtils.isNotBlank(eventDTO.getConditionId())) {
                list.add(criteriaBuilder.equal(root.get("condition").get("id").as(String.class), eventDTO.getConditionId()));
            }
            /* 计时等级 */
            if (StringUtils.isNotBlank(eventDTO.getTimeType())) {
                list.add(criteriaBuilder.equal(root.get("timeLimit").get("id").as(String.class), eventDTO.getTimeType()));
            }
            Predicate[] p = new Predicate[list.size()];
            return criteriaBuilder.and(list.toArray(p));
        });

        List<Map<String, Object>> reportList = new ArrayList<>();
        List<Map<String, Object>> operateList = new ArrayList<>();
        List<Map<String, Object>> instList = new ArrayList<>();
        List<Map<String, Object>> dispatchList = new ArrayList<>();
        List<Map<String, Object>> checkNumList = new ArrayList<>();
        List<Map<String, Object>> disposeList = new ArrayList<>();
        List<Map<String, Object>> closeList = new ArrayList<>();

        if (eventList.size() > 0) {
            for (Event event : eventList) {
                for (Statistics statistics : event.getStatisticsList()) {
                    /* 上报 */
                    if (statistics.getReport() == 1) {
                        setDataForCaseAnalysisList(reportList, event);
                    }
                    /* 受理 */
                    if (statistics.getOperate() == 1) {
                        setDataForCaseAnalysisList(operateList, event);
                    }
                    /* 立案 */
                    if (statistics.getInst() == 1) {
                        setDataForCaseAnalysisList(instList, event);
                    }
                    /* 派遣 */
                    if (statistics.getDispatch() == 1) {
                        setDataForCaseAnalysisList(dispatchList, event);
                    }
                    /* 核查 */
                    if (statistics.getCheckNum() == 1) {
                        setDataForCaseAnalysisList(checkNumList, event);
                    }
                    /* 处置 */
                    if (statistics.getDispose() == 1) {
                        setDataForCaseAnalysisList(disposeList, event);
                    }
                    /* 结案 */
                    if (statistics.getClose() == 1) {
                        setDataForCaseAnalysisList(closeList, event);
                    }
                }
            }
        }

        List<Map<String, Object>> mapList = new ArrayList<>(7);
        setListDataForCaseAnalysisList("上报", reportList, mapList);
        setListDataForCaseAnalysisList("受理", operateList, mapList);
        setListDataForCaseAnalysisList("立案", instList, mapList);
        setListDataForCaseAnalysisList("派遣", dispatchList, mapList);
        setListDataForCaseAnalysisList("处置", disposeList, mapList);
        setListDataForCaseAnalysisList("核查", checkNumList, mapList);
        setListDataForCaseAnalysisList("结案", closeList, mapList);
        return mapList;
    }

    private void setListDataForCaseAnalysisList(String name, List<Map<String, Object>> mapListData, List<Map<String, Object>> mapList){
        Map<String, Object> map = new HashMap<>(2);
        map.put("name", name);
        map.put("data", mapListData);
        mapList.add(map);
    }

    private void setDataForCaseAnalysisList(List<Map<String, Object>> mapList, Event event) {
        Map<String, Object> mapData = new HashMap<>(5);
        mapData.put("eventId", event.getId());
        mapData.put("coordinate", event.getX() + " " + event.getY());
        mapData.put("eventCode", event.getEventCode());
        mapData.put("represent", event.getRepresent());
        String name1 = event.getEventType().getParent().getParent().getName();
        String name2 = event.getEventType().getParent().getName();
        String name3 = event.getEventType().getName();
        mapData.put("eventTypeName", name1 + "-" + name2 + "-" + name3);
        mapList.add(mapData);
    }

}
