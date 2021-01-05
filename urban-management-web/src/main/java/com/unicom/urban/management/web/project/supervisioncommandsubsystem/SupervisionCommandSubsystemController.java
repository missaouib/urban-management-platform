package com.unicom.urban.management.web.project.supervisioncommandsubsystem;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.vo.ComprehensiveVO;
import com.unicom.urban.management.service.comprehensiveevaluation.ComprehensiveEvaluationService;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.service.bigscreen.IndexService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    @Autowired
    private ComprehensiveEvaluationService comprehensiveEvaluationService;
    @Autowired
    private IndexService indexService;
    @Autowired
    private GridService gridService;
    @Autowired
    private KVService kvService;

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
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/command/caseAnalysis");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        return modelAndView;
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

    @GetMapping("/toIndexDetails")
    public ModelAndView toEventConditionList(String eventId) {
        if(StringUtils.isBlank(eventId)){
            throw new DataValidException("数据异常，请先选择事件");
        }
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/command/indexDetails");
        modelAndView.addObject("eventId", eventId);
        return modelAndView;
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
    public Result getCaseAnalysis(EventDTO eventDTO) {
        List<Map<String, Object>> caseAnalysis = indexService.caseAnalysisList(eventDTO);
        return Result.success(caseAnalysis);
    }
    /**
     * 综合评价
     *
     * @return PageImpl
     */
    @GetMapping("/comprehensiveEvaluationSearch")
    public PageImpl<ComprehensiveVO> comprehensiveEvaluationSearch(String startTime,
                                                                   String endTime, String gridId, @PageableDefault Pageable pageable) {
        List<ComprehensiveVO> list = comprehensiveEvaluationService.search(startTime, endTime,gridId);
        return new PageImpl<ComprehensiveVO>(list, pageable, 0);
    }

}
