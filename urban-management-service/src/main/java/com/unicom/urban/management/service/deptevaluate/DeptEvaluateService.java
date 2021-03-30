package com.unicom.urban.management.service.deptevaluate;

import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.dao.statistics.StatisticsRepository;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.vo.DeptEvaluate;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.service.dept.DeptService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门评价
 *
 * @author 顾志杰
 * @date 2020/11/17-18:21
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class DeptEvaluateService {


    @Autowired
    private DeptService deptService;

    @Autowired
    private StatisticsRepository statisticsRepository;

    private final NumberFormat nt = NumberFormat.getPercentInstance();

    public DeptEvaluateService() {
        nt.setMinimumFractionDigits(0);
    }

    public List<DeptEvaluate> deptEvaluates(String starTimeStr, String endTimeStr) {

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        List<DeptEvaluate> list = new ArrayList<>();
        List<DeptVO> deptVOS = deptService.getAll();

        List<Statistics> all = statisticsRepository.findAll((Specification<Statistics>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list1 = new ArrayList<>();
            //查询更新时间在此时间范围内的所有spu对象
            if (StringUtils.isNotBlank(starTimeStr)) {
                LocalDateTime starTime = LocalDateTime.parse(starTimeStr, df);
                list1.add(criteriaBuilder.greaterThanOrEqualTo(root.get("event").get("createTime").as(LocalDateTime.class), starTime));
            }
            if (StringUtils.isNotBlank(endTimeStr)) {
                LocalDateTime endTime = LocalDateTime.parse(endTimeStr, df);
                list1.add(criteriaBuilder.lessThanOrEqualTo(root.get("event").get("createTime").as(LocalDateTime.class), endTime));
            }
            Predicate[] p = new Predicate[list1.size()];
            return criteriaBuilder.and(list1.toArray(p));
        });
        /*endTime不等于null的*/
        List<Statistics> endTimeIsNotNull = all.stream().filter(s -> s.getEndTime() != null).collect(Collectors.toList());
        /*结案数*/
        List<Statistics> closeNumList = all.stream().filter(s -> null != s.getClose()).filter(s -> 1 == s.getClose()).collect(Collectors.toList());
        /*处置数*/
        List<Statistics> disposeList = all.stream().filter(s -> null != s.getDispose()).filter(s -> 1 == s.getDispose()).collect(Collectors.toList());
        /*应处置数*/
        List<Statistics> needDisposeList = all.stream().filter(s -> null != s.getNeedDispose()).filter(s -> 1 == s.getNeedDispose()).collect(Collectors.toList());
        /*返工数*/
        List<Statistics> reworkList = all.stream().filter(s -> null != s.getRework()).filter(s -> s.getRework() == 1).collect(Collectors.toList());

        deptVOS.forEach(d -> {
            DeptEvaluate deptEvaluate = new DeptEvaluate();
            deptEvaluate.setDeptName(d.getDeptName());
            deptEvaluate.setRegisterNum(this.registerNum(endTimeIsNotNull, d.getId()));
            deptEvaluate.setOnTimeCloseNum(this.onTimeCloseNum(closeNumList, d.getId()));
            /*结案数*/
            Integer closeNum = this.closeNum(closeNumList, d.getId());
            deptEvaluate.setCloseNum(closeNum);
            /*应结案数*/
            Integer mustCloseNum = this.mustCloseNum(all, d.getId());
            deptEvaluate.setMustCloseNum(mustCloseNum);
            String closeRate = this.getRate(closeNum, mustCloseNum);
            deptEvaluate.setCloseRate(closeRate);
            deptEvaluate.setManagementNum(this.managementNum(disposeList, d.getId()));
            /*按时处置数*/
            Integer onTimeManagementNum = this.onTimeManagementNum(disposeList, d.getId());
            deptEvaluate.setOnTimeManagementNum(onTimeManagementNum);
            /*应处置数*/
            Integer mustManagementNum = this.mustManagementNum(needDisposeList, d.getId());
            String onTimeManagementRate = this.getRate(onTimeManagementNum, mustManagementNum);
            deptEvaluate.setOnTimeManagementRate(onTimeManagementRate);
            deptEvaluate.setMustManagementNum(mustManagementNum);
            /*返工数*/
            Integer reworkNum = this.reworkNum(reworkList, d.getId());
            deptEvaluate.setReworkNum(reworkNum);
            String reworkRate = this.getRate(reworkNum, mustCloseNum);
            deptEvaluate.setReworkRate(reworkRate);
            deptEvaluate.setComprehensive(this.comprehensive(reworkNum, closeRate, onTimeManagementRate, reworkRate));
            list.add(deptEvaluate);

        });
        return list;
    }


    public Map<String, Map<String, Object>> countByEventType() {

        Map<String, Map<String, Object>> map = new HashMap<>();
        DecimalFormat df = new DecimalFormat("0");
        List<Statistics> all = statisticsRepository.findAll();
        //部件
        /*部件处置数*/
        long unitDispose = all.stream().filter(s -> null != s.getDispose()).filter(s -> 1 == s.getDispose()).filter(s -> KvConstant.UNIT.equals(s.getEvent().getEventType().getParent().getParent().getId())).count();
        /*部件应处置数*/
        long unitNeedDispose = all.stream().filter(s -> null != s.getNeedDispose()).filter(s -> 1 == s.getNeedDispose()).filter(s -> KvConstant.UNIT.equals(s.getEvent().getEventType().getParent().getParent().getId())).count();
        String unitNeedDisposeRateStr = "0";
        if (unitNeedDispose != 0) {
            unitNeedDisposeRateStr = df.format((double) unitDispose / (double) unitNeedDispose);
        }
        Map<String, Object> unitMap = new HashMap<>();
        unitMap.put("unitDispose", unitDispose);
        unitMap.put("unitNeedDispose", unitNeedDispose);
        unitMap.put("unitNeedDisposeRateStr", unitNeedDisposeRateStr);
        map.put("unit", unitMap);

        //事件
        /*事件处置数*/
        long eventDispose = all.stream().filter(s -> null != s.getDispose()).filter(s -> 1 == s.getDispose()).filter(s -> KvConstant.EVENT.equals(s.getEvent().getEventType().getParent().getParent().getId())).count();
        /*事件应处置数*/
        long eventNeedDispose = all.stream().filter(s -> null != s.getNeedDispose()).filter(s -> 1 == s.getNeedDispose()).filter(s -> KvConstant.EVENT.equals(s.getEvent().getEventType().getParent().getParent().getId())).count();
        String eventNeedDisposeRateStr = "0";
        if (eventNeedDispose != 0) {
            eventNeedDisposeRateStr = df.format((double) eventDispose / (double) eventNeedDispose);
        }
        Map<String, Object> eventMap = new HashMap<>();
        unitMap.put("eventDispose", eventDispose);
        unitMap.put("eventNeedDispose", eventNeedDispose);
        unitMap.put("eventNeedDisposeRateStr", eventNeedDisposeRateStr);
        map.put("event", eventMap);
        return map;
    }

    private String comprehensive(Integer registerNum, String closeRateStr, String onTimeManagementRateStr, String reworkRateStr) {
        double register;
        if (registerNum <= 50) {
            register = 10.0;
        } else if (registerNum <= 100) {
            register = 9.0;
        } else if (registerNum <= 200) {
            register = 7.5;
        } else if (registerNum <= 500) {
            register = 6.0;
        } else if (registerNum <= 1000) {
            register = 4.0;
        } else {
            register = 0.0;
        }
        double closeRate = 0.0;
        if (!"0".equals(closeRateStr)) {
            closeRate = Double.parseDouble(closeRateStr.substring(0, closeRateStr.indexOf("%"))) * 0.3;
        }

        double onTimeManagementRate = 0.0;
        if (!"0".equals(onTimeManagementRateStr)) {
            onTimeManagementRate = Double.parseDouble(onTimeManagementRateStr.substring(0, onTimeManagementRateStr.indexOf("%"))) * 0.3;
        }

        double reworkRate = 0.0;
        if (!"0".equals(reworkRateStr)) {
            reworkRate = (100 - Double.parseDouble(reworkRateStr.substring(0, reworkRateStr.indexOf("%")))) * 0.3;
        }
        DecimalFormat df = new DecimalFormat("0");

        return df.format(register + closeRate + onTimeManagementRate + reworkRate);
    }


    private String getRate(Integer num1, Integer num2) {
        if (num2 == 0) {
            return "0";
        }
        Double closeRate = (double) (num1) / (double) num2;
        return Optional.of(nt.format(closeRate)).filter(s -> !"NaN".equals(s)).orElse("0");
    }

    /**
     * 立案数
     */
    private Integer registerNum(List<Statistics> statistics, String deptId) {
        List<Statistics> collect = statistics.stream().filter(s -> "派遣员-派遣".equals(s.getTaskName())).collect(Collectors.toList());
        List<Event> events = new ArrayList<>();
        collect.forEach(s -> events.add(s.getEvent()));
        List<Statistics> statistics1 = statisticsRepository.findAllByEventInAndTaskNameAndDisposeUnit_Id(events, "专业部门", deptId);
        Integer count = Math.toIntExact(statistics1.stream().filter(s -> s.getEndTime() == null).count());
        Integer count1 = Math.toIntExact(statistics1.stream().filter(s -> null != s.getDispose()).filter(s -> s.getDispose() == 1).count());
        return count + count1;
    }

    /**
     * 按时结案数
     */
    private Integer onTimeCloseNum(List<Statistics> statistics, String deptId) {
        List<Event> events = new ArrayList<>();
        statistics.forEach(s -> events.add(s.getEvent()));
        return Math.toIntExact(statisticsRepository.findAllByEventInAndTaskNameAndDisposeUnit_Id(events, "专业部门", deptId).stream().filter(s -> null != s.getInTimeDispose()).filter(s -> s.getInTimeDispose() == 1).count());
    }


    /**
     * 结案数
     */
    private Integer closeNum(List<Statistics> statistics, String deptId) {
        List<Event> events = new ArrayList<>();
        statistics.forEach(s -> events.add(s.getEvent()));
        List<Statistics> statisticsList = statisticsRepository.findAllByEventInAndTaskNameAndDisposeUnit_Id(events, "专业部门", deptId).stream().filter(s -> null != s.getDispose()).filter(s -> s.getDispose() == 1).collect(Collectors.toList());
        List<String> event = new ArrayList<>();
        statisticsList.forEach(s -> event.add(s.getEvent().getId()));
        return Math.toIntExact(event.stream().distinct().count());
    }

    /**
     * 应结案数
     */
    private Integer mustCloseNum(List<Statistics> statistics, String deptId) {
        //应结案数 = 处置数 + 超时未处置数
        List<Statistics> dispose = statistics.stream().filter(s -> null != s.getDispose()).filter(s -> s.getDispose() == 1).collect(Collectors.toList());
        List<Statistics> overtimeToDispose = statistics.stream().filter(s -> null != s.getDispose()).filter(s -> s.getOvertimeToDispose() == 1).collect(Collectors.toList());
        // 处置数
        long count = dispose.stream().filter(s -> deptId.equals(s.getDisposeUnit().getId())).count();
        count += overtimeToDispose.stream().filter(s -> deptId.equals(s.getDisposeUnit().getId())).count();
        return Math.toIntExact(count);
    }

    /**
     * 按时处置数
     */
    private Integer onTimeManagementNum(List<Statistics> statistics, String deptId) {
        long count = statistics.stream().filter(s -> s.getInTimeDispose() == 1).filter(s -> deptId.equals(s.getDisposeUnit().getId())).count();
        return Math.toIntExact(count);
    }

    /**
     * 处置数
     */
    private Integer managementNum(List<Statistics> statistics, String deptId) {
        long count = statistics.stream().filter(s -> deptId.equals(s.getDisposeUnit().getId())).count();
        return Math.toIntExact(count);
    }

    /**
     * 应处置数
     */
    private Integer mustManagementNum(List<Statistics> statistics, String deptId) {
        long count = statistics.stream().filter(s -> deptId.equals(s.getDisposeUnit().getId())).count();
        return Math.toIntExact(count);
    }

    /**
     * 返工数
     */
    private Integer reworkNum(List<Statistics> statistics, String deptId) {
        List<Event> events = new ArrayList<>();
        statistics.forEach(s -> events.add(s.getEvent()));
        return statisticsRepository.findAllByEventInAndTaskNameAndDisposeUnit_Id(events, "专业部门", deptId).size();
    }

}
