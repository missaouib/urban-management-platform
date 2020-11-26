package com.unicom.urban.management.web.project.supervisioncommandsubsystem;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * 大屏
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/commandSubsystem")
public class SupervisionCommandSubsystemController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView(SystemConstant.PAGE + "/command/index");
    }

    @GetMapping("/gridOwner")
    public ModelAndView gridOwner() {
        return new ModelAndView(SystemConstant.PAGE + "/command/gridOwner");
    }

    @GetMapping("/caseAnalysis")
    public ModelAndView caseAnalysis() {
        return new ModelAndView(SystemConstant.PAGE + "/command/caseAnalysis");
    }

    @GetMapping("/comprehensiveEvaluation")
    public ModelAndView comprehensiveEvaluation() {
        return new ModelAndView(SystemConstant.PAGE + "/command/comprehensiveEvaluation");
    }

    @GetMapping("/trajectory")
    public ModelAndView trajectory() {
        return new ModelAndView(SystemConstant.PAGE + "/command/trajectory");
    }

    @GetMapping("/gridInformation")
    public ModelAndView gridInformation() {
        return new ModelAndView(SystemConstant.PAGE + "/command/gridInformation");
    }

    /**
     * 大屏 趋势分析
     *
     * @param time 时间
     * @return list
     */
    @GetMapping("/getTrendAnalysis")
    public Result getTrendAnalysis(String time) {
        List<Map<String, Object>> trendAnalysis = statisticsService.getTrendAnalysis(time);
        return Result.success(trendAnalysis);
    }

    /**
     * 大屏 高发问题
     *
     * @param time 时间
     * @return list
     */
    @GetMapping("/findHighIncidence")
    public Result findHighIncidence(String time) {
        List<Map<String, String>> highIncidence = statisticsService.findHighIncidence(time);
        return Result.success(highIncidence);
    }

    /**
     * 案件分析 标题
     *
     * @return list
     */
    @GetMapping("/getCaseAnalysisTitle")
    public Result getCaseAnalysis() {
        List<Map<String, Object>> caseAnalysis = statisticsService.getCaseAnalysis();
        return Result.success(caseAnalysis);
    }


}
