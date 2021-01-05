package com.unicom.urban.management.service.comprehensiveevaluation;

import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.dao.statistics.StatisticsRepository;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.vo.DeptEvaluate;
import com.unicom.urban.management.pojo.vo.ComprehensiveVO;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.grid.GridService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.text.NumberFormat;
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

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<ComprehensiveVO> list = new ArrayList<>();
        List<GridVO> gridVOS = gridService.findGridAll();
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
        final String year = "year";
        final String month = "month";
        final String day = "day";
        String time = month;
        LocalDateTime startTime;
        LocalDateTime endTime;
        LocalDateTime now = LocalDateTime.now();
        switch (time) {
            case year:
                startTime = LocalDateTime.of(LocalDate.from(now.with(TemporalAdjusters.firstDayOfYear())), LocalTime.MIN);
                endTime = LocalDateTime.of(LocalDate.from(now.with(TemporalAdjusters.lastDayOfYear())), LocalTime.MAX);
                break;
            case month:
                startTime = LocalDateTime.of(LocalDate.from(now.with(TemporalAdjusters.firstDayOfMonth())), LocalTime.MIN);
                endTime = LocalDateTime.of(LocalDate.from(now.with(TemporalAdjusters.lastDayOfMonth())), LocalTime.MAX);
                break;
            case day:
                startTime = LocalDateTime.of(LocalDate.from(now), LocalTime.MIN);
                endTime = LocalDateTime.of(LocalDate.from(now), LocalTime.MAX);
                break;
            default:
                throw new DataValidException("请正确选择日期");
        }


        List<ComprehensiveVO> list = new ArrayList<>();
        List<GridVO> gridVOS = gridService.findGridAll();
        List<Statistics> all = statisticsRepository.findAll((Specification<Statistics>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list1 = new ArrayList<>();
            //查询更新时间在此时间范围内的所有spu对象
                list1.add(criteriaBuilder.greaterThanOrEqualTo(root.get("event").get("createTime").as(LocalDateTime.class), startTime));
                list1.add(criteriaBuilder.lessThanOrEqualTo(root.get("event").get("createTime").as(LocalDateTime.class), endTime));
            Predicate[] p = new Predicate[list1.size()];
            return criteriaBuilder.and(list1.toArray(p));
        });
        List<GridVO> gridAll = gridService.findGridAll();
        /*处置数*/
        List<Statistics> disposeList = all.stream().filter(s -> null != s.getDispose()).filter(s -> 1 == s.getDispose()).collect(Collectors.toList());
        /*应处置数*/
        List<Statistics> needDisposeList = all.stream().filter(s -> null != s.getNeedDispose()).filter(s -> 1 == s.getNeedDispose()).collect(Collectors.toList());
        /*超期处置数*/
        List<Statistics> overTimeDisposeList = all.stream().filter(s -> null != s.getRework()).filter(s -> s.getOvertimeDispose() == 1).collect(Collectors.toList());
        /*返工数*/
        List<Statistics> reworkList = all.stream().filter(s -> null != s.getRework()).filter(s -> s.getRework() == 1).collect(Collectors.toList());
        List<ComprehensiveVO> disposeRanking = disposeRanking(disposeList, gridAll,gridId);
        map.put("disposeRanking",disposeRanking);
        return map;
    }
    /**
     * 处置数排行榜
     */
    private List<ComprehensiveVO> disposeRanking(List<Statistics> disposeList, List<GridVO> gridAll,String gridId) {
        List<ComprehensiveVO> list = new ArrayList<>();
        List<ComprehensiveVO> sortedList = new ArrayList<>();
        Integer indexNum = 1;
        Integer patrolReportNum = 1;
        if (StringUtils.isNotEmpty(gridId)) {
            List<Dept> subDeptList = deptService.findAllByGridId(gridId);
            subDeptList.forEach(d -> {
                ComprehensiveVO comprehensiveVO = new ComprehensiveVO();
                Integer disposeNum = this.disposeNum(disposeList, d.getId());
                comprehensiveVO.setDeptName(d.getDeptName());
                comprehensiveVO.setDisposeNum(disposeNum);
                list.add(comprehensiveVO);
            });
        }else {
            for (GridVO gridVO : gridAll) {
                for (Dept d : deptService.findAllByGridId(gridVO.getId())) {
                    ComprehensiveVO comprehensiveVO = new ComprehensiveVO();
                    comprehensiveVO.setDeptName(d.getDeptName());
                    /*处置数*/
                    comprehensiveVO.setDisposeNum(this.disposeNum(disposeList,d.getId()));
                    list.add(comprehensiveVO);
                }
            }
        }
        //处置数比较排序
        sortedList = list.stream().sorted((s1, s2) -> s1.getDisposeNum().compareTo(s2.getDisposeNum())).collect(Collectors.toList());
        sortedList.forEach(s-> System.out.println(s.getDisposeNum()));
        return sortedList;
    }
}
