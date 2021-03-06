package com.unicom.urban.management.web.project.supervisioncommandsubsystem;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.vo.*;
import com.unicom.urban.management.service.bigscreen.IndexService;
import com.unicom.urban.management.service.comprehensiveevaluation.ComprehensiveEvaluationService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import com.unicom.urban.management.service.trajectory.TrajectoryService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    @Autowired
    private TrajectoryService trajectoryService;

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

    /**
     * 岗位评价页面跳转
     * @return 页面
     */
    @GetMapping("/toPositionEvaluation")
    public ModelAndView toSupervisorEvaluation() {
        return new ModelAndView(SystemConstant.PAGE + "/command/positionEvaluation");
    }
    @GetMapping("/trajectory")
    public ModelAndView trajectory(String id) {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/command/trajectory");
        modelAndView.addObject("userId", id);
        return modelAndView;
    }

    @GetMapping("/gridInformation")
    public ModelAndView gridInformation() {
        return new ModelAndView(SystemConstant.PAGE + "/command/gridInformation");
    }

    @GetMapping("/toIndexDetails")
    public ModelAndView toEventConditionList(String eventId) {
        if (StringUtils.isBlank(eventId)) {
            throw new DataValidException("数据异常，请先选择事件");
        }
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/command/indexDetails");
        modelAndView.addObject("eventId", eventId);
        return modelAndView;
    }

    @GetMapping("/toRegionalEvaluationOne")
    public ModelAndView toRegionalEvaluationOne() {
        return new ModelAndView(SystemConstant.PAGE + "/command/regionalEvaluationOne");
    }

    @GetMapping("/toRegionalEvaluationTwo")
    public ModelAndView toRegionalEvaluationTwo() {
        return new ModelAndView(SystemConstant.PAGE + "/command/regionalEvaluationTwo");
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
    public Result getCaseAnalysisTitle(EventDTO eventDTO) {
        List<Map<String, Object>> caseAnalysis = indexService.caseAnalysisList(eventDTO);
        return Result.success(caseAnalysis);
    }

    /**
     * 综合评价-列表
     *
     * @return PageImpl
     */
    @GetMapping("/comprehensiveEvaluationSearch")
    public PageImpl<ComprehensiveVO> comprehensiveEvaluationSearch(String startTime,
                                                                   String endTime, String gridId, @PageableDefault Pageable pageable) {
        List<ComprehensiveVO> list = comprehensiveEvaluationService.search(startTime, endTime, gridId);
        return new PageImpl<>(list, pageable, 0);
    }

    /**
     * 综合评价-排行榜
     *
     * @return 数据
     */
    @GetMapping("/comprehensiveEvaluationRankingList")
    public Map<String, Object> comprehensiveEvaluationRankingList(String startTime, String endTime, String gridId) {
        return comprehensiveEvaluationService.findRankingList(startTime, endTime, gridId);
    }
    /**
     * 岗位评价-监督员列表查询
     *
     * @return 数据
     */
    @GetMapping("/supervisorEvaluationSearch")
    public PageImpl<SupervisorEvaluateVO> supervisorEvaluationSearch(String startTime,
                                                                     String endTime, String gridId, @PageableDefault Pageable pageable) {
        List<SupervisorEvaluateVO> list = comprehensiveEvaluationService.supervisorEvaluationSearch(startTime, endTime,gridId);
        return new PageImpl<>(list, pageable, 0);
    }
    /**
     * 岗位评价-受理员列表查询
     * @return 数据
     */
    @GetMapping("/operatorEvaluationSearch")
    public PageImpl<AcceptorEvaluateVO> operatorEvaluationSearch(String startTime,
                                                                 String endTime, String gridId, @PageableDefault Pageable pageable) {
        List<AcceptorEvaluateVO> list = comprehensiveEvaluationService.operatorEvaluationSearch(startTime, endTime,gridId);
        return new PageImpl<>(list, pageable, 0);
    }
    /**
     * 岗位评价-值班长列表查询
     * @return 数据
     */
    @GetMapping("/instHumanEvaluationSearch")
    public PageImpl<ShiftForemanEvaluateVO> instHumanEvaluationSearch(String startTime,
                                                                      String endTime, String gridId, @PageableDefault Pageable pageable) {
        List<ShiftForemanEvaluateVO> list = comprehensiveEvaluationService.instHumanEvaluationSearch(startTime, endTime,gridId);
        return new PageImpl<>(list, pageable, 0);
    }
    /**
     * 岗位评价-派遣员列表查询
     * @return 数据
     */
    @GetMapping("/dispatcherEvaluationSearch")
    public PageImpl<DispatcherEvaluateVO> dispatcherEvaluationSearch(String startTime,
                                                                     String endTime, String gridId, @PageableDefault Pageable pageable) {
        List<DispatcherEvaluateVO> list = comprehensiveEvaluationService.dispatcherEvaluationSearch(startTime, endTime,gridId);
        return new PageImpl<>(list, pageable, 0);
    }

    /**
     * 岗位评价-监督员排行榜
     *
     * @return 数据
     */
    @GetMapping("/supervisorEvaluationRankingList")
    public Map<String,Object> supervisorEvaluationRankingList(String startTime,String endTime, String gridId){
        return comprehensiveEvaluationService.supervisorEvaluationRankingList(startTime,endTime, gridId);
    }
    /**
     * 岗位评价-受理员排行榜
     * @return 数据
     */
    @GetMapping("/operatorEvaluationRankingList")
    public Map<String,Object> operatorEvaluationRankingList(String startTime,String endTime, String gridId){
        return comprehensiveEvaluationService.operatorEvaluationRankingList(startTime,endTime, gridId);
    }
    /**
     * 岗位评价-值班长排行榜
     * @return 数据
     */
    @GetMapping("/instHumanEvaluationRankingList")
    public Map<String,Object> instHumanEvaluationRankingList(String startTime,String endTime, String gridId){
        return comprehensiveEvaluationService.instHumanEvaluationRankingList(startTime,endTime, gridId);
    }
    /**
     * 岗位评价-派遣员列表查询
     * @return 数据
     */
    @GetMapping("/dispatcherEvaluationRankingList")
    public Map<String,Object> dispatcherEvaluationRankingList(String startTime,String endTime, String gridId){
        return comprehensiveEvaluationService.dispatcherEvaluationRankingList(startTime,endTime, gridId);
    }

    /**
     * 区域评价 1类
     *
     * @return 数据
     */
    @GetMapping("/regionalEvaluationOne")
    public Page<CellGridRegionVO> cellGridRegionOne(String startTime, String endTime, String gridId, @PageableDefault Pageable pageable) {
        return statisticsService.findAllForRegionalEvaluation(startTime, endTime, "一类区域", gridId, pageable);
    }

    /**
     * 区域评价 2类
     *
     * @return 数据
     */
    @GetMapping("/regionalEvaluationTwo")
    public Page<CellGridRegionVO> cellGridRegionTwo(String startTime, String endTime, String gridId, @PageableDefault Pageable pageable) {
        return statisticsService.findAllForRegionalEvaluation(startTime, endTime, "二类区域", gridId, pageable);
    }

    /**
     * 轨迹记录-列表
     *
     * @return 成功
     */
    @GetMapping("/getTrackLogForOne")
    public Result getTrackLogForOne(String id, String startTime, String endTime) {
        if (StringUtils.isBlank(id)) {
            return Result.fail(500, "请选择巡查人员");
        }
        List<TrajectoryVO> trajectoryForOne = trajectoryService.getTrajectoryForOne(id, startTime, endTime);
        return Result.success(trajectoryForOne);
    }

}
