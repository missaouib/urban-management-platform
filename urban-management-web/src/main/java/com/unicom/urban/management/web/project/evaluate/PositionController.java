package com.unicom.urban.management.web.project.evaluate;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.vo.AcceptorEvaluateVO;
import com.unicom.urban.management.pojo.vo.DispatcherEvaluateVO;
import com.unicom.urban.management.pojo.vo.ShiftForemanEvaluateVO;
import com.unicom.urban.management.pojo.vo.SupervisorEvaluateVO;
import com.unicom.urban.management.service.evaluate.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 岗位评价
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/evaluate")
public class PositionController {

    @Autowired
    private PositionService positionService;

    /**
     * 单元网格区域评价
     *
     * @return 页面
     */
    @GetMapping("/toPosition")
    public ModelAndView toCellGridRegion() {
        return new ModelAndView(SystemConstant.PAGE + "/evaluate/position");
    }

    public Result Search() {

        return Result.success();
    }

    /**
     * 监督员岗位评价
     *
     * @return
     */
    @GetMapping("/supervisorEvaluation")
    public List<SupervisorEvaluateVO> supervisorEvaluation(String startTime, String endTime) {
        List<SupervisorEvaluateVO> list = positionService.findSupervisorEvaluateByCondition(startTime, endTime);
        return list;
    }

    /**
     * 受理员岗位评价
     *
     * @return
     */
    @GetMapping("/acceptanceEvaluation")
    public List<AcceptorEvaluateVO> acceptanceEvaluation(String startTime, String endTime) {
        List<AcceptorEvaluateVO> list = positionService.findAcceptorEvaluateByCondition(startTime, endTime);
        return list;

    }

    /**
     * 值班长岗位评价
     *
     * @return
     */
    @GetMapping("/shiftForemanEvaluation")
    public List<ShiftForemanEvaluateVO> shiftForemanEvaluation(String startTime, String endTime) {
        List<ShiftForemanEvaluateVO> list = positionService.findShiftForemanEvaluateByCondition(startTime, endTime);
        return list;

    }

    /**
     * 派遣员岗位评价
     *
     * @return
     */
    @GetMapping("/dispatcherEvaluation")
    public List<DispatcherEvaluateVO> dispatcherEvaluation(String startTime, String endTime) {
        List<DispatcherEvaluateVO> list = positionService.findDispatcherEvaluateByCondition(startTime, endTime);
        return list;

    }

}
