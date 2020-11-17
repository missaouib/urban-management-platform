package com.unicom.urban.management.web.project.evaluate;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.vo.SupervisorEvaluateVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
    private EventService eventService;
    @Autowired
    private StatisticsService statisticsService;
    /**
     * 单元网格区域评价
     *
     * @return 页面
     */
    @GetMapping("/toPosition")
    public ModelAndView toCellGridRegion() {
        return new ModelAndView(SystemConstant.PAGE + "/evaluate/position");
    }

    public Result Search(){

        return Result.success();
    }

    /**
     * 监督员岗位评价
     * @return
     */
    @GetMapping("/toSupervisorEvaluate")
    public List<SupervisorEvaluateVO> SupervisorEvaluation(){
        List<SupervisorEvaluateVO> list = null;
        return list;

    }
//    /**
//     * 受理员岗位评价
//     * @return
//     */
//    public List<PositionVO> AcceptanceEvaluation(){
//        List<PositionVO> list = null;
//        return list;
//
//    }
//    /**
//     * 值班长岗位评价
//     * @return
//     */
//    public List<PositionVO> MonitorEvaluation(){
//        List<PositionVO> list = null;
//        return list;
//
//    }
//    /**
//     * 派遣员岗位评价
//     * @return
//     */
//    public List<PositionVO> dispatcherEvaluation(){
//        List<PositionVO> list = null;
//        return list;
//
//    }

}
