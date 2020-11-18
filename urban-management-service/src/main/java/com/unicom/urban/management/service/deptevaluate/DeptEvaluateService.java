package com.unicom.urban.management.service.deptevaluate;

import com.unicom.urban.management.dao.statistics.StatisticsRepository;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.vo.DeptEvaluate;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.service.dept.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 部门评价
 *
 * @author 顾志杰
 * @date 2020/11/17-18:21
 */
@Service
public class DeptEvaluateService {


    @Autowired
    private DeptService deptService;

    @Autowired
    private StatisticsRepository statisticsRepository;

    private final NumberFormat nt = NumberFormat.getPercentInstance();

    public DeptEvaluateService() {
        nt.setMinimumFractionDigits(0);
    }

    public List<DeptEvaluate> deptEvaluates() {
        List<DeptEvaluate> list = new ArrayList<>();
        List<DeptVO> deptVOS = deptService.getAll();

        List<Statistics> all = statisticsRepository.findAll();
        /*endtime不等于null的*/
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
            deptEvaluate.setCloseRate(this.getRate(closeNum, mustCloseNum));
            deptEvaluate.setManagementNum(this.managementNum(disposeList, d.getId()));
            /*按时处置数*/
            Integer onTimeManagementNum = this.onTimeManagementNum(disposeList, d.getId());
            deptEvaluate.setOnTimeManagementNum(onTimeManagementNum);
            /*应处置数*/
            Integer mustManagementNum = this.mustManagementNum(needDisposeList, d.getId());
            deptEvaluate.setOnTimeManagementRate(this.getRate(onTimeManagementNum, mustManagementNum));
            deptEvaluate.setMustManagementNum(mustManagementNum);
            /*返工数*/
            Integer reworkNum = this.reworkNum(reworkList, d.getId());
            deptEvaluate.setReworkNum(reworkNum);
            deptEvaluate.setReworkRate(this.getRate(reworkNum, mustCloseNum));

            list.add(deptEvaluate);

        });
        return list;
    }

    private double comprehensive(Integer registerNum) {
        Double register = 0.0;
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
        return 0.0;
    }


    private String getRate(Integer num1, Integer num2) {
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
        List<Statistics> collect = statistics.stream().filter(s -> null != s.getInTimeClose()).filter(s -> s.getInTimeClose() == 1).collect(Collectors.toList());
        List<Event> events = new ArrayList<>();
        collect.forEach(s -> events.add(s.getEvent()));
        return statisticsRepository.findAllByEventInAndTaskNameAndDisposeUnit_Id(events, "专业部门", deptId).size();
    }


    /**
     * 结案数
     */
    private Integer closeNum(List<Statistics> statistics, String deptId) {
        List<Event> events = new ArrayList<>();
        statistics.forEach(s -> events.add(s.getEvent()));
        return statisticsRepository.findAllByEventInAndTaskNameAndDisposeUnit_Id(events, "专业部门", deptId).size();
    }

    /**
     * 应结案数
     */
    private Integer mustCloseNum(List<Statistics> statistics, String deptId) {
        //TODO 应结案数 = 处置数 + 超时未处置数
        List<Statistics> dispose = statistics.stream().filter(s -> null != s.getDispose()).filter(s -> s.getDispose() == 1).collect(Collectors.toList());
        // 处置数
        long count = dispose.stream().filter(s -> deptId.equals(s.getDisposeUnit().getId())).count();
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
