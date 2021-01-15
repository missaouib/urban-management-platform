package com.unicom.urban.management.service.comprehensiveevaluation;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.statistics.StatisticsRepository;
import com.unicom.urban.management.mapper.GridMapper;
import com.unicom.urban.management.pojo.entity.*;
import com.unicom.urban.management.pojo.vo.*;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.grid.GridService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 综合评价Service
 *
 * @author liubozhi
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class ComprehensiveEvaluationService {

    @Autowired
    GridService gridService;
    @Autowired
    DeptService deptService;
    @Autowired
    StatisticsRepository statisticsRepository;


    private final NumberFormat nt = NumberFormat.getPercentInstance();

    public List<ComprehensiveVO> search(String starTimeStr, String endTimeStr, String gridId) {



        List<ComprehensiveVO> list = new ArrayList<>();
        List<Statistics> all = getStatistics(starTimeStr, endTimeStr);
        /*处置数*/
        List<Statistics> disposeList = all.stream().filter(s -> null != s.getDispose()).filter(s -> 1 == s.getDispose()).collect(Collectors.toList());
        /*应处置数*/
        List<Statistics> needDisposeList = all.stream().filter(s -> null != s.getNeedDispose()).filter(s -> 1 == s.getNeedDispose()).collect(Collectors.toList());
        /*超期处置数*/
        List<Statistics> overTimeDisposeList = all.stream().filter(s -> null != s.getRework()).filter(s -> s.getOvertimeDispose() == 1).collect(Collectors.toList());
        /*返工数*/
        List<Statistics> reworkList = all.stream().filter(s -> null != s.getRework()).filter(s -> s.getRework() == 1).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(gridId)) {
            List<Dept> subDeptList = deptService.findAllByGridId(gridId);
            subDeptList.forEach(d->{
                ComprehensiveVO comprehensiveVO = new ComprehensiveVO();
                comprehensiveVO.setDeptName(d.getDeptName());
                /*处置数*/
                comprehensiveVO.setDisposeNum(this.disposeNum(disposeList,d.getId()));
                /*应处置数*/
                comprehensiveVO.setNeedDisposeNum(this.needDisposeNum(needDisposeList, d.getId()));
                /*超期处置数*/
                comprehensiveVO.setOvertimeDisposeNum(this.overtimeDisposeNum(overTimeDisposeList, d.getId()));
                /*返工数*/
                comprehensiveVO.setReworkNum(this.reworkNum(reworkList, d.getId()));
                list.add(comprehensiveVO);
            });
        }else {
            for (GridVO gridVO : gridService.findGridAll()) {
                for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                    ComprehensiveVO comprehensiveVO = new ComprehensiveVO();
                    comprehensiveVO.setDeptName(d.getDeptName());
                    /*处置数*/
                    comprehensiveVO.setDisposeNum(this.disposeNum(disposeList,d.getId()));
                    /*应处置数*/
                    comprehensiveVO.setNeedDisposeNum(this.needDisposeNum(needDisposeList, d.getId()));
                    /*超期处置数*/
                    comprehensiveVO.setOvertimeDisposeNum(this.overtimeDisposeNum(overTimeDisposeList, d.getId()));
                    /*返工数*/
                    comprehensiveVO.setReworkNum(this.reworkNum(reworkList, d.getId()));
                    list.add(comprehensiveVO);
                }
            }
        }
        return list;
    }

    private List<Statistics> getStatistics(String starTimeStr, String endTimeStr) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return statisticsRepository.findAll((Specification<Statistics>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list1 = new ArrayList<>();
            //查询更新时间在此时间范围内的所有spu对象
            if (StringUtils.isNotBlank(starTimeStr) && StringUtils.isNotBlank(endTimeStr)) {
                LocalDateTime starTime = LocalDateTime.parse(starTimeStr, df);
                list1.add(criteriaBuilder.greaterThanOrEqualTo(root.get("event").get("createTime").as(LocalDateTime.class), starTime));
                LocalDateTime endTime = LocalDateTime.parse(endTimeStr, df);
                list1.add(criteriaBuilder.lessThanOrEqualTo(root.get("event").get("createTime").as(LocalDateTime.class), endTime));
            }
            Predicate[] p = new Predicate[list1.size()];
            return criteriaBuilder.and(list1.toArray(p));
        });
    }

    private String getRate(Integer num1, Integer num2) {
        if (num2 == 0) {
            return "0";
        }
        Double closeRate = (double) (num1) / (double) num2;
        return Optional.of(nt.format(closeRate)).filter(s -> !"NaN".equals(s)).orElse("0");
    }
    /**
     * 处置数
     */
    private Integer disposeNum(List<Statistics> statistics, String deptId) {
        long count = statistics.stream().filter(s -> deptId.equals(s.getDisposeUnit().getId())).count();
        return Math.toIntExact(count);
    }
    /**
     * 按时处置数
     */
    private Integer inTimeDisposeNum(List<Statistics> statistics, String deptId) {
        long count = statistics.stream().filter(s -> s.getInTimeDispose() == 1).filter(s -> deptId.equals(s.getDisposeUnit().getId())).count();
        return Math.toIntExact(count);
    }
    /**
     * 应处置数
     */
    private Integer needDisposeNum(List<Statistics> statistics, String deptId) {
        long count = statistics.stream().filter(s -> deptId.equals(s.getDisposeUnit().getId())).count();
        return Math.toIntExact(count);
    }
    /**
     * 超时处置数
     */
    private Integer overtimeDisposeNum(List<Statistics> statistics, String deptId) {
        long count = statistics.stream().filter(s -> s.getOvertimeDispose() == 1).filter(s -> deptId.equals(s.getDisposeUnit().getId())).count();
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

    public Map<String,Object> findRankingList(String starTimeStr, String endTimeStr, String gridId){
        Map<String,Object> map  = new HashMap<>();
//        final String year = "year";
//        final String month = "month";
//        final String day = "day";
//        String time = month;
//        LocalDateTime startTime;
//        LocalDateTime endTime;
//        LocalDateTime now = LocalDateTime.now();
//        switch (time) {
//            case year:
//                startTime = LocalDateTime.of(LocalDate.from(now.with(TemporalAdjusters.firstDayOfYear())), LocalTime.MIN);
//                endTime = LocalDateTime.of(LocalDate.from(now.with(TemporalAdjusters.lastDayOfYear())), LocalTime.MAX);
//                break;
//            case month:
//                startTime = LocalDateTime.of(LocalDate.from(now.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
//                endTime = LocalDateTime.of(LocalDate.from(now.with(TemporalAdjusters.lastDayOfMonth())), LocalTime.MAX);
//                break;
//            case day:
//                startTime = LocalDateTime.of(LocalDate.from(now), LocalTime.MIN);
//                endTime = LocalDateTime.of(LocalDate.from(now), LocalTime.MAX);
//                break;
//            default:
//                throw new DataValidException("请正确选择日期");
//        }


        List<Statistics> all = getStatistics(starTimeStr, endTimeStr);
        List<GridVO> gridAll = new ArrayList<>();
        if (StringUtils.isNotEmpty(gridId)){
            Grid grid = gridService.findOne(gridId);
            gridAll.add(GridMapper.INSTANCE.gridToGridVO(grid));
        }else {
            gridAll = gridService.findGridAll();
        }

        /*处置数*/
        List<Statistics> disposeList = all.stream().filter(s -> null != s.getDispose()).filter(s -> 1 == s.getDispose()).collect(Collectors.toList());
        /*应处置数*/
        List<Statistics> needDisposeList = all.stream().filter(s -> null != s.getNeedDispose()).filter(s -> 1 == s.getNeedDispose()).collect(Collectors.toList());
        /*按时处置数*/
        List<Statistics> inTimeDisposeList = all.stream().filter(s -> null != s.getNeedDispose()).filter(s -> 1 == s.getInTimeDispose()).collect(Collectors.toList());
        /*返工数*/
        List<Statistics> reworkList = all.stream().filter(s -> null != s.getRework()).filter(s -> s.getRework() == 1).collect(Collectors.toList());
        List<ComprehensiveVO> disposeRanking = disposeRanking(disposeList, gridAll);
        List<ComprehensiveVO> disposeRate = disposeRate(disposeList,needDisposeList, gridAll);
        List<ComprehensiveVO> inTimeDisposeRate = inTimeDisposeRate(inTimeDisposeList,needDisposeList, gridAll);
        List<ComprehensiveVO> reworkRate = reworkRate(reworkList,disposeList, gridAll);

        map.put("disposeRanking",disposeRanking);
        map.put("disposeRate",disposeRate);
        map.put("inTimeDisposeRate",inTimeDisposeRate);
        map.put("reworkRate",reworkRate);
        return map;
    }
    /**
     * 返工率排行榜
     */
    private List<ComprehensiveVO> reworkRate(List<Statistics> reworkList, List<Statistics> disposeList, List<GridVO> gridAll) {
        List<ComprehensiveVO> list = new ArrayList<>();
        List<ComprehensiveVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                ComprehensiveVO c = new ComprehensiveVO();
                c.setDeptName(d.getDeptName());
                Integer reworkNum = this.reworkNum(reworkList, d.getId());
                Integer disposeNum = this.disposeNum(disposeList, d.getId());
                c.setReworkRateNum(disposeNum == 0 ? 0 : new BigDecimal(reworkNum).divide(new BigDecimal(disposeNum), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                c.setDeptName(d.getDeptName());
                list.add(c);
            }
        }
        //处置数比较排序
        sortedList = list.stream().sorted((s1, s2) -> s1.getReworkRateNum().compareTo(s2.getReworkRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setReworkRate(sortedList.get(i).getReworkRateNum() == 0 ? "0.0%" : sortedList.get(i).getReworkRateNum() * 100 + "%");
        }
        return sortedList;
    }

    /**
     * 按时处置率排行榜
     */
    private List<ComprehensiveVO> inTimeDisposeRate(List<Statistics> inTimeDisposeList, List<Statistics> needDisposeList, List<GridVO> gridAll) {
        List<ComprehensiveVO> list = new ArrayList<>();
        List<ComprehensiveVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                ComprehensiveVO c = new ComprehensiveVO();
                Integer inTimeDisposeNum = this.inTimeDisposeNum(inTimeDisposeList, d.getId());
                Integer needDisposeNum = this.needDisposeNum(needDisposeList, d.getId());
                c.setInTimeDisposeRateNum(needDisposeNum == 0 ? 0 : new BigDecimal(inTimeDisposeNum).divide(new BigDecimal(needDisposeNum), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                c.setDeptName(d.getDeptName());
                list.add(c);
            }
        }
        //处置数比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getInTimeDisposeRateNum().compareTo(s1.getInTimeDisposeRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setInTimeDisposeRate(sortedList.get(i).getInTimeDisposeRateNum() == 0 ? "0.0%" : sortedList.get(i).getInTimeDisposeRateNum() * 100 + "%");
        }
        return sortedList;
    }

    /**
     * 处置率排行榜
     */
    private List<ComprehensiveVO> disposeRate(List<Statistics> disposeList, List<Statistics> needDisposeList, List<GridVO> gridAll) {
        List<ComprehensiveVO> list = new ArrayList<>();
        List<ComprehensiveVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                ComprehensiveVO c = new ComprehensiveVO();
                c.setDeptName(d.getDeptName());
                Integer disposeNum = this.disposeNum(disposeList, d.getId());
                Integer needDisposeNum = this.needDisposeNum(needDisposeList, d.getId());
                c.setDisposeRateNum(needDisposeNum == 0 ? 0 : new BigDecimal(disposeNum).divide(new BigDecimal(needDisposeNum), 2, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
                c.setDeptName(d.getDeptName());
                c.setDisposeNum(disposeNum);
                list.add(c);
            }
        }
        //处置数比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getDisposeRateNum().compareTo(s1.getDisposeRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setDisposeRate(sortedList.get(i).getDisposeRateNum() == 0 ? "0.0%" : sortedList.get(i).getDisposeRateNum() * 100 + "%");
        }
        return sortedList;
    }

    /**
     * 处置数排行榜
     */
    private List<ComprehensiveVO> disposeRanking(List<Statistics> disposeList, List<GridVO> gridAll) {
        List<ComprehensiveVO> list = new ArrayList<>();
        List<ComprehensiveVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                ComprehensiveVO comprehensiveVO = new ComprehensiveVO();
                comprehensiveVO.setDeptName(d.getDeptName());
                /*处置数*/
                comprehensiveVO.setDisposeNum(this.disposeNum(disposeList, d.getId()));
                list.add(comprehensiveVO);
            }
        }

        //处置数比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getDisposeNum().compareTo(s1.getDisposeNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
        }
        return sortedList;
    }

    /**
     *岗位评价-受理员列表查询
     * @param startTime
     * @param endTime
     * @param gridId
     * @return
     */
    public List<SupervisorEvaluateVO> supervisorEvaluationSearch(String startTime, String endTime, String gridId) {
        List<Statistics> all = getStatistics(startTime, endTime);
        List<SupervisorEvaluateVO> list = new ArrayList<>();
        /*上报数*/
        List<Statistics> patrolReportList = all.stream().filter(s -> null != s.getPatrolReport()).filter(s -> 1 == s.getPatrolReport()).collect(Collectors.toList());
        /*有效上报数*/
        List<Statistics>validReportList = all.stream().filter(s -> null != s.getValidReport()).filter(s -> 1 == s.getValidReport()).collect(Collectors.toList());
        /*按时核查数*/
        List<Statistics>intimeCheckList = all.stream().filter(s -> null != s.getInTimeCheck()).filter(s -> 1 == s.getInTimeCheck()).collect(Collectors.toList());
        /*按时核实数*/
        List<Statistics> intimeVerifyList = all.stream().filter(s -> null != s.getInTimeVerify()).filter(s -> 1 == s.getInTimeVerify()).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(gridId)) {
            List<Dept> subDeptList = deptService.findAllByGridId(gridId);
            subDeptList.forEach(d->{
                d.getUserList().forEach(user -> {
                    SupervisorEvaluateVO vo = new SupervisorEvaluateVO();
                    /*上报数*/
                    vo.setPatrolReport(Math.toIntExact(patrolReportList.stream().filter(s -> s.getReportPatrolName().getId().equals(user.getId())).count()));
                    /*有效上报数*/
                    vo.setValidPatrolReport(Math.toIntExact(validReportList.stream().filter(s -> s.getReportPatrolName().getId().equals(user.getId())).count()));
                    /*按时核查数*/
                    vo.setInTimeCheck(Math.toIntExact(intimeCheckList.stream().filter(s -> s.getCheckPatrolName().getId().equals(user.getId())).count()));
                    /*按时核实数*/
                    vo.setIntimeVerify(Math.toIntExact(intimeVerifyList.stream().filter(s -> s.getVerifyPatrolName().getId().equals(user.getId())).count()));
                    vo.setSupervisorName(user.getName());
                    list.add(vo);
                });
            });
        }else {
            for (GridVO gridVO : gridService.findGridAll()) {
                for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                    d.getUserList().forEach(user -> {
                        SupervisorEvaluateVO vo = new SupervisorEvaluateVO();
                        /*上报数*/
                        vo.setPatrolReport(Math.toIntExact(patrolReportList.stream().filter(s -> s.getReportPatrolName().getId().equals(user.getId())).count()));
                        /*有效上报数*/
                        vo.setValidPatrolReport(Math.toIntExact(validReportList.stream().filter(s -> s.getInstHumanName().getId().equals(user.getId())).count()));
                        /*按时核查数*/
                        vo.setInTimeCheck(Math.toIntExact(intimeCheckList.stream().filter(s -> s.getCheckPatrolName().getId().equals(user.getId())).count()));
                        /*按时核实数*/
                        vo.setIntimeVerify(Math.toIntExact(intimeVerifyList.stream().filter(s -> s.getVerifyPatrolName().getId().equals(user.getId())).count()));
                        vo.setSupervisorName(user.getName());
                        list.add(vo);
                    });
                }
            }
        }
        return list;
    }
    /**
     *岗位评价-受理员岗位评价列表查询
     * @param startTime
     * @param endTime
     * @param gridId
     * @return
     */
    public List<AcceptorEvaluateVO> operatorEvaluationSearch(String startTime, String endTime, String gridId) {
        List<Statistics> all = getStatistics(startTime, endTime);
        List<AcceptorEvaluateVO> list = new ArrayList<>();
        /*受理数*/
        List<Statistics> operateList = all.stream().filter(s -> null != s.getOperate()).filter(s -> 1 == s.getOperate()).collect(Collectors.toList());
        /*按时核实派发数*/
        List<Statistics> inTimeSendVerifyList = all.stream().filter(s -> null != s.getInTimeSendVerify()).filter(s -> 1 == s.getInTimeSendVerify()).collect(Collectors.toList());
        /*核实应派发数*/
        List<Statistics> needSendVerifyList = all.stream().filter(s -> null != s.getNeedSendVerify()).filter(s -> 1 == s.getNeedSendVerify()).collect(Collectors.toList());
        /*按时核查派发数*/
        List<Statistics> inTimeSendCheckList = all.stream().filter(s -> null != s.getInTimeSendCheck()).filter(s -> 1 == s.getInTimeSendCheck()).collect(Collectors.toList());
        /*核查应派发数*/
        List<Statistics> needSendCheckList = all.stream().filter(s -> null != s.getNeedSendCheck()).filter(s -> 1 == s.getNeedSendCheck()).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(gridId)) {
            List<Dept> subDeptList = deptService.findAllByGridId(gridId);
            subDeptList.forEach(d->{
                d.getUserList().forEach(user -> {
                    AcceptorEvaluateVO vo = new AcceptorEvaluateVO();
                    /*受理数*/
                    vo.setOperate(Math.toIntExact(operateList.stream().filter(s -> s.getOperateHumanName().getId().equals(user.getId())).count()));
                    /*按时核实派发数*/
                    vo.setOperate(Math.toIntExact(inTimeSendVerifyList.stream().filter(s -> s.getSendCheckHumanName().getId().equals(user.getId())).count()));
                    /*核实应派发数*/
                    vo.setOperate(Math.toIntExact(needSendVerifyList.stream().filter(s -> s.getSendCheckHumanName().getId().equals(user.getId())).count()));
                    /*按时核查派发数*/
                    vo.setOperate(Math.toIntExact(inTimeSendCheckList.stream().filter(s -> s.getSendCheckHumanName().getId().equals(user.getId())).count()));
                    /*核查应派发数*/
                    vo.setOperate(Math.toIntExact(needSendCheckList.stream().filter(s -> s.getSendCheckHumanName().getId().equals(user.getId())).count()));
                    vo.setOperateHumanName(user.getName());
                    list.add(vo);
                });
            });
        }else {
            for (GridVO gridVO : gridService.findGridAll()) {
                for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                    d.getUserList().forEach(user -> {
                        AcceptorEvaluateVO vo = new AcceptorEvaluateVO();
                        /*受理数*/
                        vo.setOperate(Math.toIntExact(operateList.stream().filter(s -> s.getOperateHumanName().getId().equals(user.getId())).count()));
                        /*按时核实派发数*/
                        vo.setIntimeSendVerify(Math.toIntExact(inTimeSendVerifyList.stream().filter(s -> s.getSendVerifyHumanName().getId().equals(user.getId())).count()));
                        /*核实应派发数*/
                        vo.setNeedSendVerify(Math.toIntExact(needSendVerifyList.stream().filter(s -> s.getSendVerifyHumanName().getId().equals(user.getId())).count()));
                        /*按时核查派发数*/
                        vo.setIntimeSendCheck(Math.toIntExact(inTimeSendCheckList.stream().filter(s -> s.getSendCheckHumanName().getId().equals(user.getId())).count()));
                        /*核查应派发数*/
                        vo.setNeedSendCheck(Math.toIntExact(needSendCheckList.stream().filter(s -> s.getSendCheckHumanName().getId().equals(user.getId())).count()));
                        vo.setOperateHumanName(user.getName());
                        list.add(vo);
                    });
                }
            }
        }
        return list;
    }
    /**
     *岗位评价-值班长岗位评价列表查询
     * @param startTime
     * @param endTime
     * @param gridId
     * @return
     */
    public List<ShiftForemanEvaluateVO> ShiftForemanEvaluateVO(String startTime, String endTime, String gridId) {
        List<Statistics> all = getStatistics(startTime, endTime);
        List<ShiftForemanEvaluateVO> list = new ArrayList<>();
        /*立案数*/
        List<Statistics> instList = all.stream().filter(s -> null != s.getInst()).filter(s -> 1 == s.getInst()).collect(Collectors.toList());
        /*按时立案数*/
        List<Statistics> inTimeInstList = all.stream().filter(s -> null != s.getInTimeInst()).filter(s -> 1 == s.getInTimeInst()).collect(Collectors.toList());
        /*废弃数*/
        List<Statistics> cancelList =  all.stream().filter(s -> null != s.getCancel()).collect(Collectors.toList());
        /*按时结案数*/
        List<Statistics> inTimeCloseList = all.stream().filter(s -> null != s.getInTimeClose()).filter(s -> 1 == s.getInTimeClose()).collect(Collectors.toList());
        /*结案数*/
        List<Statistics> colseList = all.stream().filter(s -> null != s.getClose()).filter(s -> 1 == s.getClose()).collect(Collectors.toList());
        /*待结案数*/
        List<Statistics> toColseList = all.stream().filter(s -> null != s.getToClose()).filter(s -> 1 == s.getToClose()).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(gridId)) {
            List<Dept> subDeptList = deptService.findAllByGridId(gridId);
            subDeptList.forEach(d->{
                d.getUserList().forEach(user -> {
                    ShiftForemanEvaluateVO vo = new ShiftForemanEvaluateVO();
                    /*立案数*/
                    vo.setInst(Math.toIntExact(instList.stream().filter(s -> s.getInstHumanName().getId().equals(user.getId())).count()));
                    /*按时立案数*/
                    vo.setIntimeInst(Math.toIntExact(inTimeInstList.stream().filter(s -> s.getInstHumanName().getId().equals(user.getId())).count()));
                    /*准确立案数(立案数  - 作废数)*/
                    vo.setExactInst(vo.getInst() - Math.toIntExact(cancelList.stream().filter(s -> s.getInstHumanName().getId().equals(user.getId())).count()));
                    /*按时结案数*/
                    vo.setInTimeClose(Math.toIntExact(inTimeCloseList.stream().filter(s -> s.getCloseHumanName().getId().equals(user.getId())).count()));
                    /*应结案数数(按时结案数+待结案数)*/
                    vo.setClose(vo.getInTimeClose() + Math.toIntExact(toColseList.stream().filter(s -> s.getCloseHumanName().getId().equals(user.getId())).count()));
                    vo.setInstHumanName(user.getName());
                    list.add(vo);
                });
            });
        }else {
            for (GridVO gridVO : gridService.findGridAll()) {
                for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                    d.getUserList().forEach(user -> {
                        ShiftForemanEvaluateVO vo = new ShiftForemanEvaluateVO();
                        /*立案数*/
                        vo.setInst(Math.toIntExact(instList.stream().filter(s -> s.getInstHumanName().getId().equals(user.getId())).count()));
                        /*按时立案数*/
                        vo.setIntimeInst(Math.toIntExact(inTimeInstList.stream().filter(s -> s.getInstHumanName().getId().equals(user.getId())).count()));
                        /*准确立案数(立案数  - 作废数)*/
                        vo.setExactInst(vo.getInst() - Math.toIntExact(cancelList.stream().filter(s -> s.getInstHumanName().getId().equals(user.getId())).count()));
                        /*按时结案数*/
                        vo.setInTimeClose(Math.toIntExact(inTimeCloseList.stream().filter(s -> s.getCloseHumanName().getId().equals(user.getId())).count()));
                        /*应结案数数(按时结案数+待结案数)*/
                        vo.setClose(vo.getInTimeClose() + Math.toIntExact(toColseList.stream().filter(s -> s.getCloseHumanName().getId().equals(user.getId())).count()));
                        vo.setInstHumanName(user.getName());
                        list.add(vo);
                    });
                }
            }
        }
        return list;
    }

    /**
     * 岗位评价-派遣员岗位评价列表查询
     * @param startTime
     * @param endTime
     * @param gridId
     * @return
     */
    public List<DispatcherEvaluateVO> dispatcherEvaluationSearch(String startTime, String endTime, String gridId) {
        List<Statistics> all = getStatistics(startTime, endTime);
        List<DispatcherEvaluateVO> list = new ArrayList<>();
        /*派遣数*/
        List<Statistics> toDispatchList = all.stream().filter(s -> null != s.getToDispatch()).filter(s -> 1 == s.getToDispatch()).collect(Collectors.toList());
        /*按时派遣数*/
        List<Statistics> inTimeDispatchList = all.stream().filter(s -> null != s.getInTimeDispatch()).filter(s -> 1 == s.getInTimeDispatch()).collect(Collectors.toList());
        /*应派遣数*/
        List<Statistics> needDispatchList = all.stream().filter(s -> null != s.getNeedDispatch()).filter(s -> 1 == s.getNeedDispatch()).collect(Collectors.toList());
        /*准确派遣数（派遣数-返工数）*/
        List<Statistics> reWorkList = all.stream().filter(s -> null != s.getRework()).filter(s -> 1 == s.getRework()).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(gridId)) {
            List<Dept> subDeptList = deptService.findAllByGridId(gridId);
            subDeptList.forEach(d->{
                d.getUserList().forEach(user -> {
                    DispatcherEvaluateVO vo = new DispatcherEvaluateVO();
                    /*派遣数*/
                    vo.setToDispatch(Math.toIntExact(toDispatchList.stream().filter(s -> s.getDispatchHumanName().getId().equals(user.getId())).count()));
                    /*按时派遣数*/
                    vo.setIntimeDispatch(Math.toIntExact(inTimeDispatchList.stream().filter(s -> s.getDispatchHumanName().getId().equals(user.getId())).count()));
                    /*应派遣数*/
                    vo.setNeedDispatch(Math.toIntExact(needDispatchList.stream().filter(s -> s.getDispatchHumanName().getId().equals(user.getId())).count()));
                    /*准确派遣数（派遣数-返工数）*/
                    vo.setAccuracyDispatch(vo.getToDispatch() - Math.toIntExact(reWorkList.stream().filter(s -> s.getDispatchHumanName().getId().equals(user.getId())).count()));
                    vo.setDispatch(user.getName());
                    list.add(vo);
                });
            });
        }else {
            for (GridVO gridVO : gridService.findGridAll()) {
                for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                    d.getUserList().forEach(user -> {
                        DispatcherEvaluateVO vo = new DispatcherEvaluateVO();
                        /*派遣数*/
                        vo.setToDispatch(Math.toIntExact(toDispatchList.stream().filter(s -> s.getDispatchHumanName().getId().equals(user.getId())).count()));
                        /*按时派遣数*/
                        vo.setIntimeDispatch(Math.toIntExact(inTimeDispatchList.stream().filter(s -> s.getDispatchHumanName().getId().equals(user.getId())).count()));
                        /*应派遣数*/
                        vo.setNeedDispatch(Math.toIntExact(needDispatchList.stream().filter(s -> s.getDispatchHumanName().getId().equals(user.getId())).count()));
                        /*准确派遣数（派遣数-返工数）*/
                        vo.setAccuracyDispatch(vo.getToDispatch() - Math.toIntExact(reWorkList.stream().filter(s -> s.getDispatchHumanName().getId().equals(user.getId())).count()));
                        vo.setDispatch(user.getName());
                        list.add(vo);
                    });
                }
            }
        }
        return list;
    }

    public Map<String, Object> supervisorEvaluationRankingList(String startTime, String endTime, String gridId) {
        Map<String,Object> map  = new HashMap<>();
        List<Statistics> all = getStatistics(startTime, endTime);
        List<GridVO> gridAll = new ArrayList<>();
        if (StringUtils.isNotEmpty(gridId)){
            Grid grid = gridService.findOne(gridId);
            gridAll.add(GridMapper.INSTANCE.gridToGridVO(grid));
        }else {
            gridAll = gridService.findGridAll();
        }

        /*上报数*/
        List<Statistics> patrolReportList = all.stream().filter(s -> null != s.getPatrolReport()).filter(s -> 1 == s.getPatrolReport()).collect(Collectors.toList());
        /*有效上报数*/
        List<Statistics> validPatrolReportList = all.stream().filter(s -> null != s.getValidPatrolReport()).filter(s -> 1 == s.getValidPatrolReport()).collect(Collectors.toList());
        /*按时核实数*/
        List<Statistics> intimeVerifyList = all.stream().filter(s -> null != s.getInTimeVerify()).filter(s -> 1 == s.getInTimeVerify()).collect(Collectors.toList());
        /*应核实数*/
        List<Statistics> needVerifyList = all.stream().filter(s -> null != s.getNeedVerify()).filter(s -> 1 == s.getNeedVerify()).collect(Collectors.toList());
        /*按时核查数*/
        List<Statistics> intimeCheckList = all.stream().filter(s -> null != s.getInTimeCheck()).filter(s -> 1 == s.getInTimeCheck()).collect(Collectors.toList());
        /*应核查数*/
        List<Statistics> needCheckList = all.stream().filter(s -> null != s.getNeedCheck()).filter(s -> 1 == s.getNeedCheck()).collect(Collectors.toList());

        List<SupervisorEvaluateVO> patrolReportRanking = patrolReportRanking(patrolReportList, gridAll);
        List<SupervisorEvaluateVO> reportVaildNumRate = reportVaildNumRate(patrolReportList,validPatrolReportList, gridAll);
        List<SupervisorEvaluateVO> inTimeVerifyRate = inTimeVerifyRate(intimeVerifyList,needVerifyList, gridAll);
        List<SupervisorEvaluateVO> inTimeCheckRate = inTimeCheckRate(intimeCheckList,needCheckList, gridAll);

        map.put("patrolReportRanking",patrolReportRanking);
        map.put("reportVaildNumRate",reportVaildNumRate);
        map.put("inTimeVerifyRate",inTimeVerifyRate);
        map.put("inTimeCheckRate",inTimeCheckRate);
        return map;
    }

    /**
     * 按期核查率排行榜
     * @param intimeCheckList
     * @param needCheckList
     * @param gridAll
     * @return
     */
    private List<SupervisorEvaluateVO> inTimeCheckRate(List<Statistics> intimeCheckList, List<Statistics> needCheckList, List<GridVO> gridAll) {
        List<SupervisorEvaluateVO> list = new ArrayList<>();
        List<SupervisorEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    SupervisorEvaluateVO supervisorEvaluateVO = new SupervisorEvaluateVO();
                    supervisorEvaluateVO.setSupervisorName(user.getName());
                    Integer intimeCheckNum = Math.toIntExact(intimeCheckList.stream().filter(s -> user.getId().equals(s.getCheckPatrolName().getId())).count());
                    Integer needCheckNum = Math.toIntExact(needCheckList.stream().filter(s -> user.getId().equals(s.getCheckPatrolName().getId())).count());
                    supervisorEvaluateVO.setInTimeCheckRateNum(Double.valueOf(getRate(intimeCheckNum,needCheckNum)));
                    list.add(supervisorEvaluateVO);
                });
            }
        }
        //核查率比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getInTimeCheckRateNum().compareTo(s1.getInTimeCheckRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setInTimeCheckRate(sortedList.get(i).getInTimeCheckRateNum() == 0 ? "0.0%" : sortedList.get(i).getInTimeCheckRateNum() * 100 + "%");
        }
        return sortedList;
    }

    /**
     * 按期核实率排行榜
     * @param intimeVerifyList
     * @param needVerifyList
     * @param gridAll
     * @return
     */
    private List<SupervisorEvaluateVO> inTimeVerifyRate(List<Statistics> intimeVerifyList, List<Statistics> needVerifyList, List<GridVO> gridAll) {
        List<SupervisorEvaluateVO> list = new ArrayList<>();
        List<SupervisorEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    SupervisorEvaluateVO supervisorEvaluateVO = new SupervisorEvaluateVO();
                    supervisorEvaluateVO.setSupervisorName(user.getName());
                    Integer intimeVerifyNum = Math.toIntExact(intimeVerifyList.stream().filter(s -> user.getId().equals(s.getVerifyPatrolName().getId())).count());
                    Integer needVerifyNum = Math.toIntExact(needVerifyList.stream().filter(s -> user.getId().equals(s.getVerifyPatrolName().getId())).count());
                    supervisorEvaluateVO.setInTimeVerifyRateNum(Double.valueOf(getRate(intimeVerifyNum,needVerifyNum)));
                    list.add(supervisorEvaluateVO);
                });
            }
        }
        //核实率比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getInTimeVerifyRateNum().compareTo(s1.getInTimeVerifyRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setInTimeVerifyRate(sortedList.get(i).getInTimeVerifyRateNum() == 0 ? "0.0%" : sortedList.get(i).getInTimeVerifyRateNum() * 100 + "%");
        }
        return sortedList;
    }

    /**
     * 有效上报率排行榜
     * @param patrolReportList
     * @param validPatrolReportList
     * @param gridAll
     * @return
     */
    private List<SupervisorEvaluateVO> reportVaildNumRate(List<Statistics> patrolReportList, List<Statistics> validPatrolReportList, List<GridVO> gridAll) {
        List<SupervisorEvaluateVO> list = new ArrayList<>();
        List<SupervisorEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    SupervisorEvaluateVO supervisorEvaluateVO = new SupervisorEvaluateVO();
                    supervisorEvaluateVO.setSupervisorName(user.getName());
                    Integer patrolReportNum = Math.toIntExact(patrolReportList.stream().filter(s -> user.getId().equals(s.getOperateHumanName().getId())).count());
                    Integer validPatrolReportNum = Math.toIntExact(validPatrolReportList.stream().filter(s -> user.getId().equals(s.getOperateHumanName().getId())).count());
                    supervisorEvaluateVO.setReportVaildNumRateNum(Double.valueOf(getRate(patrolReportNum,validPatrolReportNum)));
                    list.add(supervisorEvaluateVO);
                });
            }
        }
        //上报率比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getReportVaildNumRateNum().compareTo(s1.getReportVaildNumRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setReportVaildNumRate(sortedList.get(i).getReportVaildNumRateNum() == 0 ? "0.0%" : sortedList.get(i).getReportVaildNumRateNum() * 100 + "%");
        }
        return sortedList;
    }

    /**
     * 上报数排行榜
     * @param patrolReportList
     * @param gridAll
     * @return
     */
    private List<SupervisorEvaluateVO> patrolReportRanking(List<Statistics> patrolReportList, List<GridVO> gridAll) {
        List<SupervisorEvaluateVO> list = new ArrayList<>();
        List<SupervisorEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    SupervisorEvaluateVO supervisorEvaluateVO = new SupervisorEvaluateVO();
                    supervisorEvaluateVO.setSupervisorName(user.getName());
                    /*上报数*/
                    supervisorEvaluateVO.setPatrolReport(Math.toIntExact(patrolReportList.stream().filter(s -> user.getId().equals(s.getOperateHumanName().getId())).count()));
                    list.add(supervisorEvaluateVO);
                });

            }
        }

        //上报数比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getPatrolReport().compareTo(s1.getPatrolReport())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
        }
        return sortedList;
    }

    /**
     * 岗位评价-受理员排行榜
     * @param startTime
     * @param endTime
     * @param gridId
     * @return
     */
    public Map<String, Object> operatorEvaluationRankingList(String startTime, String endTime, String gridId) {
        Map<String,Object> map  = new HashMap<>();
        List<Statistics> all = getStatistics(startTime, endTime);
        List<GridVO> gridAll = new ArrayList<>();
        if (StringUtils.isNotEmpty(gridId)){
            Grid grid = gridService.findOne(gridId);
            gridAll.add(GridMapper.INSTANCE.gridToGridVO(grid));
        }else {
            gridAll = gridService.findGridAll();
        }

        /*受理数*/
        List<Statistics> operateList = all.stream().filter(s -> null != s.getOperate()).filter(s -> 1 == s.getOperate()).collect(Collectors.toList());
        /*按时核实派发数*/
        List<Statistics> inTimeSendVerifyList = all.stream().filter(s -> null != s.getInTimeSendVerify()).filter(s -> 1 == s.getInTimeSendVerify()).collect(Collectors.toList());
        /*应核实派发数*/
        List<Statistics> needSendVerifyList = all.stream().filter(s -> null != s.getNeedSendVerify()).filter(s -> 1 == s.getNeedSendVerify()).collect(Collectors.toList());
        /*按时核查派发数*/
        List<Statistics> inTimeSendCheckList = all.stream().filter(s -> null != s.getInTimeSendCheck()).filter(s -> 1 == s.getInTimeSendCheck()).collect(Collectors.toList());
        /*核查应派发数*/
        List<Statistics> needSendCheckList = all.stream().filter(s -> null != s.getNeedSendCheck()).filter(s -> 1 == s.getNeedSendCheck()).collect(Collectors.toList());

        List<AcceptorEvaluateVO> operateRanking = operateRanking(operateList, gridAll);
        List<AcceptorEvaluateVO> sendVerifyRate = sendVerifyRate(inTimeSendVerifyList,needSendVerifyList, gridAll);
        List<AcceptorEvaluateVO> needSendCheckRate = needSendCheckRate(inTimeSendCheckList,needSendCheckList, gridAll);

        map.put("operateRanking",operateRanking);
        map.put("sendVerifyRate",sendVerifyRate);
        map.put("needSendCheckRate",needSendCheckRate);
        return map;
    }

    private List<AcceptorEvaluateVO> needSendCheckRate(List<Statistics> inTimeSendCheckList, List<Statistics> needSendCheckList, List<GridVO> gridAll) {
        List<AcceptorEvaluateVO> list = new ArrayList<>();
        List<AcceptorEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    AcceptorEvaluateVO vo = new AcceptorEvaluateVO();
                    vo.setOperateHumanName(user.getName());
                    Integer num1 = Math.toIntExact(inTimeSendCheckList.stream().filter(s -> user.getId().equals(s.getSendCheckHumanName().getId())).count());
                    Integer num2 = Math.toIntExact(needSendCheckList.stream().filter(s -> user.getId().equals(s.getSendCheckHumanName().getId())).count());
                    vo.setNeedSendCheckRateNum(Double.valueOf(getRate(num1,num2)));
                    list.add(vo);
                });
            }
        }
        //按时派发核实率比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getNeedSendCheckRateNum().compareTo(s1.getNeedSendCheckRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setNeedSendCheckRate(sortedList.get(i).getNeedSendCheckRateNum() == 0 ? "0.0%" : sortedList.get(i).getNeedSendCheckRateNum() * 100 + "%");
        }
        return sortedList;
    }

    /**
     * 按时派发核实率排行榜
     * @param inTimeSendVerifyList
     * @param needSendVerifyList
     * @param gridAll
     * @return
     */
    private List<AcceptorEvaluateVO> sendVerifyRate(List<Statistics> inTimeSendVerifyList, List<Statistics> needSendVerifyList, List<GridVO> gridAll) {
        List<AcceptorEvaluateVO> list = new ArrayList<>();
        List<AcceptorEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    AcceptorEvaluateVO vo = new AcceptorEvaluateVO();
                    vo.setOperateHumanName(user.getName());
                    Integer num1 = Math.toIntExact(inTimeSendVerifyList.stream().filter(s -> user.getId().equals(s.getOperateHumanName().getId())).count());
                    Integer num2 = Math.toIntExact(needSendVerifyList.stream().filter(s -> user.getId().equals(s.getSendVerifyHumanName().getId())).count());
                    vo.setSendVerifyRateNum(Double.valueOf(getRate(num1,num2)));
                    list.add(vo);
                });
            }
        }
        //按时派发核实率比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getSendVerifyRateNum().compareTo(s1.getSendVerifyRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setSendVerifyRate(sortedList.get(i).getSendVerifyRateNum() == 0 ? "0.0%" : sortedList.get(i).getSendVerifyRateNum() * 100 + "%");
        }
        return sortedList;
    }

    /**
     * 受理数排行榜
     * @param operateList
     * @param gridAll
     * @return
     */
    private List<AcceptorEvaluateVO> operateRanking(List<Statistics> operateList, List<GridVO> gridAll) {
        List<AcceptorEvaluateVO> list = new ArrayList<>();
        List<AcceptorEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    AcceptorEvaluateVO vo = new AcceptorEvaluateVO();
                    vo.setOperateHumanName(user.getName());
                    /*受理数*/
                    vo.setOperate(Math.toIntExact(operateList.stream().filter(s -> user.getId().equals(s.getOperateHumanName().getId())).count()));
                    list.add(vo);
                });

            }
        }

        //上报数比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getOperate().compareTo(s1.getOperate())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
        }
        return sortedList;
    }

    /**
     * 岗位评价-值班长排行榜
     * @param startTime
     * @param endTime
     * @param gridId
     * @return
     */
    public Map<String, Object> instHumanEvaluationRankingList(String startTime, String endTime, String gridId) {
        Map<String,Object> map  = new HashMap<>();
        List<Statistics> all = getStatistics(startTime, endTime);
        List<GridVO> gridAll = new ArrayList<>();
        if (StringUtils.isNotEmpty(gridId)){
            Grid grid = gridService.findOne(gridId);
            gridAll.add(GridMapper.INSTANCE.gridToGridVO(grid));
        }else {
            gridAll = gridService.findGridAll();
        }

        /*立案数*/
        List<Statistics> instList = all.stream().filter(s -> null != s.getInst()).filter(s -> 1 == s.getInst()).collect(Collectors.toList());
        /*按时立案数*/
        List<Statistics> inTimeInstList = all.stream().filter(s -> null != s.getInTimeInst()).filter(s -> 1 == s.getInTimeInst()).collect(Collectors.toList());
        /*废弃数*/
        List<Statistics> cancelList =  all.stream().filter(s -> null != s.getCancel()).collect(Collectors.toList());
        /*按时结案数*/
        List<Statistics> inTimeCloseList = all.stream().filter(s -> null != s.getInTimeClose()).filter(s -> 1 == s.getInTimeClose()).collect(Collectors.toList());
        /*结案数*/
        List<Statistics> colseList = all.stream().filter(s -> null != s.getClose()).filter(s -> 1 == s.getClose()).collect(Collectors.toList());
        /*待结案数*/
        List<Statistics> toColseList = all.stream().filter(s -> null != s.getToClose()).filter(s -> 1 == s.getToClose()).collect(Collectors.toList());
        /*立案数*/
        List<ShiftForemanEvaluateVO> instRanking = instRanking(instList, gridAll);
        /*按时立案率(按时立案数/立案数×100%)*/
        List<ShiftForemanEvaluateVO> inTimeInstRate = inTimeInstRate(inTimeInstList,instList, gridAll);
        /*准确立案率(准确立案数(立案数  - 作废数)/立案数×100%)*/
        List<ShiftForemanEvaluateVO> exactInstRate = exactInstRate(instList,cancelList, gridAll);
        /*按时结案率（按时结案数/结案数×100%）*/
        List<ShiftForemanEvaluateVO> inTimeCloseRate = inTimeCloseRate(inTimeCloseList,colseList, gridAll);

        map.put("instRanking",instRanking);
        map.put("inTimeInstRate",inTimeInstRate);
        map.put("exactInstRate",exactInstRate);
        map.put("inTimeCloseRate",inTimeCloseRate);
        return map;
    }

    private List<ShiftForemanEvaluateVO> inTimeCloseRate(List<Statistics> inTimeCloseList, List<Statistics> colseList, List<GridVO> gridAll) {
        List<ShiftForemanEvaluateVO> list = new ArrayList<>();
        List<ShiftForemanEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    ShiftForemanEvaluateVO vo = new ShiftForemanEvaluateVO();
                    vo.setInstHumanName(user.getName());
                    Integer num1 = Math.toIntExact(inTimeCloseList.stream().filter(s -> user.getId().equals(s.getCloseHumanName().getId())).count());
                    Integer num2 = Math.toIntExact(colseList.stream().filter(s -> user.getId().equals(s.getCloseHumanName().getId())).count());
                    vo.setInTimeCloseRateNum(Double.valueOf(getRate(num1, num2)));
                    list.add(vo);
                });
            }
        }
        //准确立案率比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getInTimeCloseRateNum().compareTo(s1.getInTimeCloseRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setExactInstRate(sortedList.get(i).getInTimeCloseRateNum() == 0 ? "0.0%" : sortedList.get(i).getInTimeCloseRateNum() * 100 + "%");
        }
        return sortedList;
    }


    private List<ShiftForemanEvaluateVO> exactInstRate(List<Statistics> instList, List<Statistics> cancelList, List<GridVO> gridAll) {
        List<ShiftForemanEvaluateVO> list = new ArrayList<>();
        List<ShiftForemanEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    ShiftForemanEvaluateVO vo = new ShiftForemanEvaluateVO();
                    vo.setInstHumanName(user.getName());
                    Integer num1 = Math.toIntExact(cancelList.stream().filter(s -> user.getId().equals(s.getInstHumanName().getId())).count());
                    Integer num2 = Math.toIntExact(instList.stream().filter(s -> user.getId().equals(s.getInstHumanName().getId())).count());
                    vo.setExactInstRateNum(Double.valueOf(getRate(num2-num1,num2)));
                    list.add(vo);
                });
            }
        }
        //准确立案率比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getExactInstRateNum().compareTo(s1.getExactInstRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setExactInstRate(sortedList.get(i).getExactInstRateNum() == 0 ? "0.0%" : sortedList.get(i).getExactInstRateNum() * 100 + "%");
        }
        return sortedList;
    }

    private List<ShiftForemanEvaluateVO> inTimeInstRate(List<Statistics> inTimeInstList, List<Statistics> instList, List<GridVO> gridAll) {
        List<ShiftForemanEvaluateVO> list = new ArrayList<>();
        List<ShiftForemanEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    ShiftForemanEvaluateVO vo = new ShiftForemanEvaluateVO();
                    vo.setInstHumanName(user.getName());
                    Integer num1 = Math.toIntExact(inTimeInstList.stream().filter(s -> user.getId().equals(s.getInstHumanName().getId())).count());
                    Integer num2 = Math.toIntExact(instList.stream().filter(s -> user.getId().equals(s.getInstHumanName().getId())).count());
                    vo.setIntimeInstRateNum(Double.valueOf(getRate(num1,num2)));
                    list.add(vo);
                });
            }
        }
        //按时派发核实率比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getIntimeInstRateNum().compareTo(s1.getIntimeInstRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setIntimeInstRate(sortedList.get(i).getIntimeInstRateNum() == 0 ? "0.0%" : sortedList.get(i).getIntimeInstRateNum() * 100 + "%");
        }
        return sortedList;
    }

    private List<ShiftForemanEvaluateVO> instRanking(List<Statistics> instList, List<GridVO> gridAll) {
        List<ShiftForemanEvaluateVO> list = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    ShiftForemanEvaluateVO vo = new ShiftForemanEvaluateVO();
                    vo.setInstHumanName(user.getName());
                    /*立案数*/
                    vo.setInst(Math.toIntExact(instList.stream().filter(s -> user.getId().equals(s.getInstHumanName().getId())).count()));
                    list.add(vo);
                });
            }
        }
        return list;
    }
    public Map<String, Object> dispatcherEvaluationRankingList(String startTime, String endTime, String gridId) {
        Map<String,Object> map  = new HashMap<>();
        List<Statistics> all = getStatistics(startTime, endTime);
        List<GridVO> gridAll = new ArrayList<>();
        if (StringUtils.isNotEmpty(gridId)){
            Grid grid = gridService.findOne(gridId);
            gridAll.add(GridMapper.INSTANCE.gridToGridVO(grid));
        }else {
            gridAll = gridService.findGridAll();
        }

        /*派遣数*/
        List<Statistics> dispatchList = all.stream().filter(s -> null != s.getDispatch()).filter(s -> 1 == s.getInst()).collect(Collectors.toList());
        /*按时派遣数*/
        List<Statistics> inTImeDispatchList = all.stream().filter(s -> null != s.getInTimeDispatch()).filter(s -> 1 == s.getInst()).collect(Collectors.toList());
        /*应派遣数*/
        List<Statistics> needDispatchList = all.stream().filter(s -> null != s.getNeedDispatch()).filter(s -> 1 == s.getInst()).collect(Collectors.toList());
        /*准确派遣数*/
        List<Statistics> accuracyDispatchList = all.stream().filter(s -> null != s.getAccuracyDispatch()).filter(s -> 1 == s.getInst()).collect(Collectors.toList());

        /*派遣数排行榜*/
        List<DispatcherEvaluateVO> dispatchRanking = dispatchRanking(dispatchList, gridAll);
        /*按时派遣率(按时派遣数/应派遣数×100%)排行榜*/
        List<DispatcherEvaluateVO> accuracyDispatchRate = accuracyDispatchRate(inTImeDispatchList,needDispatchList, gridAll);
        /*准确派遣率(准确派遣数/应派遣数×100%)排行榜*/
        List<DispatcherEvaluateVO> intimeDispatchRate = intimeDispatchRate(accuracyDispatchList,needDispatchList, gridAll);
        map.put("dispatchRanking",dispatchRanking);
        map.put("accuracyDispatchRate",accuracyDispatchRate);
        map.put("intimeDispatchRate",intimeDispatchRate);
        return map;
    }

    private List<DispatcherEvaluateVO> intimeDispatchRate(List<Statistics> accuracyDispatchList, List<Statistics> needDispatchList, List<GridVO> gridAll) {
        List<DispatcherEvaluateVO> list = new ArrayList<>();
        List<DispatcherEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    DispatcherEvaluateVO vo = new DispatcherEvaluateVO();
                    vo.setDispatch(user.getName());
                    Integer num1 = Math.toIntExact(accuracyDispatchList.stream().filter(s -> user.getId().equals(s.getDispatchHumanName().getId())).count());
                    Integer num2 = Math.toIntExact(needDispatchList.stream().filter(s -> user.getId().equals(s.getDispatchHumanName().getId())).count());
                    vo.setAccuracyDispatchRateNum(Double.valueOf(getRate(num1,num2)));
                    list.add(vo);
                });
            }
        }
        //按时派发核实率比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getAccuracyDispatchRateNum().compareTo(s1.getAccuracyDispatchRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setAccuracyDispatchRate(sortedList.get(i).getAccuracyDispatchRateNum() == 0 ? "0.0%" : sortedList.get(i).getAccuracyDispatchRateNum() * 100 + "%");
        }
        return sortedList;
    }

    private List<DispatcherEvaluateVO> accuracyDispatchRate(List<Statistics> inTImeDispatchList, List<Statistics> needDispatchList, List<GridVO> gridAll) {
        List<DispatcherEvaluateVO> list = new ArrayList<>();
        List<DispatcherEvaluateVO> sortedList = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    DispatcherEvaluateVO vo = new DispatcherEvaluateVO();
                    vo.setDispatch(user.getName());
                    Integer num1 = Math.toIntExact(inTImeDispatchList.stream().filter(s -> user.getId().equals(s.getDispatchHumanName().getId())).count());
                    Integer num2 = Math.toIntExact(needDispatchList.stream().filter(s -> user.getId().equals(s.getDispatchHumanName().getId())).count());
                    vo.setAccuracyDispatchRateNum(Double.valueOf(getRate(num1,num2)));
                    list.add(vo);
                });
            }
        }
        //按时派发核实率比较排序
        sortedList = list.stream().sorted((s1, s2) -> s2.getAccuracyDispatchRateNum().compareTo(s1.getAccuracyDispatchRateNum())).collect(Collectors.toList());
        for (int i = 0; i < sortedList.size(); i++) {
            sortedList.get(i).setIndex(String.valueOf(i + 1));
            sortedList.get(i).setAccuracyDispatchRate(sortedList.get(i).getAccuracyDispatchRateNum() == 0 ? "0.0%" : sortedList.get(i).getAccuracyDispatchRateNum() * 100 + "%");
        }
        return sortedList;
    }

    private List<DispatcherEvaluateVO> dispatchRanking(List<Statistics> dispatchList, List<GridVO> gridAll) {
        List<DispatcherEvaluateVO> list = new ArrayList<>();
        for (GridVO gridVO : gridAll) {
            for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                d.getUserList().forEach(user -> {
                    DispatcherEvaluateVO vo = new DispatcherEvaluateVO();
                    vo.setDispatch(user.getName());
                    /*派遣数*/
                    vo.setToDispatch(Math.toIntExact(dispatchList.stream().filter(s -> user.getId().equals(s.getDispatchHumanName().getId())).count()));
                    list.add(vo);
                });
            }
        }
        return list;
    }
}
