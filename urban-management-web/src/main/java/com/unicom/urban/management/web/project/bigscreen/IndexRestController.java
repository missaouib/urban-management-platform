package com.unicom.urban.management.web.project.bigscreen;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.vo.DeptEvaluate;
import com.unicom.urban.management.pojo.vo.EventOneVO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.pojo.vo.SupervisorEvaluateVO;
import com.unicom.urban.management.service.bigscreen.IndexService;
import com.unicom.urban.management.service.deptevaluate.DeptEvaluateService;
import com.unicom.urban.management.service.evaluate.PositionService;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import com.unicom.urban.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 顾志杰
 * @date 2020/11/25-14:43
 */
@RequestMapping("/bigScreen")
@RestController
@ResponseResultBody
public class IndexRestController {

    @Autowired
    private IndexService indexService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private EventService eventService;
    @Autowired
    private DeptEvaluateService evaluateService;
    @Autowired
    private PositionService positionService;

    @Autowired
    private UserService userService;

    @GetMapping("/count")
    public Map<String, Object> count(String timeType) {
        return indexService.count(timeType);
    }

    @GetMapping("/showPoints")
    public List<Map<String, String>> showPoints(String timeType, String showType) {
        return indexService.showPoints(timeType, showType);
    }

    /**
     * 大屏数据展示
     * report 上报数
     * inst 立案数
     * dispatch 派遣数
     * close 结案数
     * operate 上报数
     * dispose 处置数
     *
     * @return 数据
     */
    @GetMapping("/getIndexValue")
    public Result getIndexValue() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monday = now.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).minusYears(5).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime sunday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1).withHour(23).withMinute(59).withSecond(59);
        Map<String, Object> indexValueByWeek = statisticsService.getIndexValueByWeek(monday, sunday);
        return Result.success(indexValueByWeek);
    }


    /**
     * 大屏 高发问题
     * name: "污水口监测站"
     * totalClose: 结案数
     * totalDispose: 处置数
     * totalInst: 立案数
     *
     * @return list
     */
    @GetMapping("/findHighIncidence")
    public Result findHighIncidence() {
        List<Map<String, String>> highIncidence = statisticsService.findHighIncidence("");
        return Result.success(highIncidence);
    }

    /**
     * 高发区域（大屏）
     * gridName: "污水口监测站"
     * totalClose: 结案数
     * totalDispose: 处置数
     * totalInst: 立案数
     *
     * @return
     */
    @GetMapping("/findHotGrid")
    public List<Map<String, Object>> findHotGrid() {
        return statisticsService.findHotGrid("");
    }

    /**
     * 角色数量（大屏）
     * professionalDepartments: 专业部门
     * supervisor: 监督员
     * shiftLeader: 值班长
     * dispatcher: 派遣员
     * receptionist: 受理员
     *
     * @return
     */
    @GetMapping("/findRoleCount")
    public Map<String, Object> findRoleCount() {
        return userService.getUserCount();
    }


    /**
     * 案件趋势
     * report 上报数
     * inst 立案数
     * dispatch 派遣数
     * close 结案数
     * operate 上报数
     * dispose 处置数
     *
     * @return 数据
     */
    @GetMapping("/getIndexValueByDay")
    public Map<String, Map<String, Object>> getIndexValueByDay() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime monday = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime sunday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).minusDays(1).withHour(23).withMinute(59).withSecond(59);
        Map<String, Map<String, Object>> map = new HashMap<>();
        map.put("one", statisticsService.getIndexValueByWeek(monday, sunday));
        map.put("two", statisticsService.getIndexValueByWeek(monday.minusDays(1), sunday));
        map.put("three", statisticsService.getIndexValueByWeek(monday.minusDays(2), sunday));
        map.put("four", statisticsService.getIndexValueByWeek(monday.minusDays(3), sunday));
        map.put("five", statisticsService.getIndexValueByWeek(monday.minusDays(4), sunday));
        map.put("six", statisticsService.getIndexValueByWeek(monday.minusDays(5), sunday));
        map.put("seven", statisticsService.getIndexValueByWeek(monday.minusDays(6), sunday));
        return map;
    }

    /**
     * 案件动态
     * 案件照片（无照片就默认图前端判断）、案件编号、类型、所属地区
     *
     * @return list
     */
    @GetMapping("/eventList")
    public Page<EventVO> eventList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        Page<EventVO> search = eventService.search(eventDTO, pageable);
        for (EventVO eventVO : search.getContent()) {
            EventOneVO oneToVo = eventService.findOneToVo(eventVO.getId());
            eventVO.setEventRegion(oneToVo.getEventRegion());
            eventVO.setFile(oneToVo.getFile());
        }
        return search;
    }

    /**
     * 部门考核
     * 查询考核评分前四部门 此处查询所有 应为是公用的接口，需要前端自行选取前四
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param pageable 无用
     * @return list
     */
    @GetMapping("/evaluate")
    public Page<DeptEvaluate> evaluates(String startTime,
                                        String endTime, @PageableDefault Pageable pageable) {
        List<DeptEvaluate> list = evaluateService.deptEvaluates(startTime, endTime);
        return new PageImpl<>(list, pageable, 0);
    }

    /**
     * 人员考核
     * 监督员岗位考核人员前四 此处查询所有 应为是公用的接口，需要前端自行选取前四
     *
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return list
     */
    @GetMapping("/supervisorEvaluation")
    public Result supervisorEvaluation(String startTime, String endTime) {
        List<SupervisorEvaluateVO> list = positionService.findSupervisorEvaluateByCondition(startTime, endTime);
        return Result.success(list);
    }

    /**
     * 大屏 问题来源
     * 上报来源
     *
     * @return 数量
     */
    @GetMapping("/getCountByEventSource")
    public Result getCountByEventSource(){
        Map<String, Object> countByEventSource = eventService.getCountByEventSource();
        return Result.success(countByEventSource);
    }

}
