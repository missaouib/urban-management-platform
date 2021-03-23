package com.unicom.urban.management.api.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.vo.DeptEvaluate;
import com.unicom.urban.management.service.deptevaluate.DeptEvaluateService;
import com.unicom.urban.management.service.evaluate.PositionService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 领导通 评价
 *
 * @author jiangwen
 */
@RestController
@RequestMapping("/api/appraise")
@ResponseResultBody
public class AppraiseController {

    @Autowired
    private DeptEvaluateService evaluateService;
    @Autowired
    private PositionService positionService;
    @Autowired
    private StatisticsService statisticsService;

    /**
     * 专业部门评价
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageable  分页
     * @return 列表
     */
    @GetMapping("/evaluate")
    public Result evaluates(String startTime,
                            String endTime, @PageableDefault Pageable pageable) {
        List<DeptEvaluate> list = evaluateService.deptEvaluates(startTime, endTime);
        return Result.success(new PageImpl<>(list, pageable, 0));
    }

    /**
     * 监督员岗位评价
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @GetMapping("/supervisorEvaluation")
    public Result supervisorEvaluation(String startTime, String endTime) {
        return Result.success(positionService.findSupervisorEvaluateByCondition(startTime, endTime));
    }

    /**
     * 受理员岗位评价
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @GetMapping("/acceptanceEvaluation")
    public Result acceptanceEvaluation(String startTime, String endTime) {
        return Result.success(positionService.findAcceptorEvaluateByCondition(startTime, endTime));
    }

    /**
     * 值班长岗位评价
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @GetMapping("/shiftForemanEvaluation")
    public Result shiftForemanEvaluation(String startTime, String endTime) {
        return Result.success(positionService.findShiftForemanEvaluateByCondition(startTime, endTime));
    }

    /**
     * 派遣员岗位评价
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return list
     */
    @GetMapping("/dispatcherEvaluation")
    public Result dispatcherEvaluation(String startTime, String endTime) {
        return Result.success(positionService.findDispatcherEvaluateByCondition(startTime, endTime));
    }

    /**
     * 区域评价 1类
     *
     * @return 数据
     */
    @GetMapping("/cellGridRegionOne")
    public Result cellGridRegionOne(String startTime, String endTime, @PageableDefault Pageable pageable) {
        return Result.success(statisticsService.findAllForCellGridRegion(startTime, endTime, "一类区域", pageable));
    }

    /**
     * 区域评价 2类
     *
     * @return 数据
     */
    @GetMapping("/cellGridRegionTwo")
    public Result cellGridRegionTwo(String startTime, String endTime, @PageableDefault Pageable pageable) {
        return Result.success(statisticsService.findAllForCellGridRegion(startTime, endTime, "二类区域", pageable));
    }

}
