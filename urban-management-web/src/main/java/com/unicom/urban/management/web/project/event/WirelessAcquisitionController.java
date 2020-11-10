package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.dto.StatisticsDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.vo.DeptTimeLimitVO;
import com.unicom.urban.management.pojo.vo.EventConditionVO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.service.depttimelimit.DeptTimeLimitService;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

/**
 * 无线采集子系统
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/event")
public class WirelessAcquisitionController {

    @Autowired
    private EventService eventService;
    @Autowired
    private GridService gridService;
    @Autowired
    private KVService kvService;
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private DeptTimeLimitService deptTimeLimitService;
    @Autowired
    private StatisticsService statisticsService;
    @GetMapping("/toWirelessAcquisitionList")
    public ModelAndView toWirelessAcquisitionList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/list");
    }
    @GetMapping("/toCaseHistoryList")
    public ModelAndView toCaseHistoryList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/caseHistory/list");
    }
    @GetMapping("/toCaseInspectList")
    public ModelAndView toCaseInspectList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/caseInspect/list");
    }
    @GetMapping("/toCaseInvalidList")
    public ModelAndView toCaseInvalidList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/caseInvalid/list");
    }
    @GetMapping("/toCaseVerifyList")
    public ModelAndView toCaseVerifyList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/caseVerify/list");
    }

    @GetMapping("/toWirelessAcquisitionSave")
    public ModelAndView toWirelessAcquisitionListSave() {
        ModelAndView model = new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/save");
        //案件等级
        model.addObject("level", kvService.findByTableNameAndFieldName("deptTimeLimit","level"));
        //所属区域
        model.addObject("region", kvService.findByTableNameAndFieldName("event","region"));
        //问题来源
        model.addObject("eventSource", kvService.findByTableNameAndFieldName("event","eventSource"));
        //案件类型
        model.addObject("recType", kvService.findByTableNameAndFieldName("event","recType"));
        //所在区域
        model.addObject("gridList",gridService.findAllByParentIsNull());
        return model;
    }

    @GetMapping("/toWirelessAcquisitionUpdate/{id}")
    public ModelAndView toWirelessAcquisitionListUpdate(@PathVariable String id, Model model) {
        Event event = eventService.findOne(id);
        model.addAttribute("event", event);
        String dtlId = event.getTimeLimit().getId();
        DeptTimeLimitVO deptTimeLimitVO = deptTimeLimitService.findDeptTimeLimit(dtlId);
        //问题类型回显
        EventType eventType = eventTypeService.getEventType(event.getEventType().getId());
        model.addAttribute("eventTypeId",eventType.getName());
        //立案区域回显
        model.addAttribute("region",event.getEventCondition().getId());
        //案件时限分类回显
        model.addAttribute("timeType",event.getTimeLimit().getId());
        //案件等级回显
        //level
        //所在区域回显
        model.addAttribute("grid1",event.getGrid().getParent().getParent().getParent().getId());
        //所属街道回显
        model.addAttribute("grid2",event.getGrid().getParent().getParent().getId());
        //所属社区回显
        model.addAttribute("grid3",event.getGrid().getParent().getId());
        //所属网格回显
        model.addAttribute("grid4",event.getGrid().getId());
        //问题来源回显
        model.addAttribute("eventSource",event.getEventSource().getId());
        //案件类型回显
        model.addAttribute("recTypeId",event.getRecType().getId());
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/update");
    }

    @GetMapping("/wirelessAcquisitionList")
    public Page<EventVO> wirelessAcquisitionList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Collections.singletonList(EventConstant.ACCEPTANCE_CASE_VERIFICATION));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 案件核查
     * @param eventDTO
     * @param pageable
     * @return
     */
    @GetMapping("/caseInspectList")
    public Page<EventVO> caseInspectList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Collections.singletonList(EventConstant.ACCEPTANCE_CASE_INSPECT));
        return eventService.search(eventDTO, pageable);
    }


    /**
     * 获取立案区域
     *
     * @param eventTypeId
     * @return
     */
    @GetMapping("/findEventConditionByEventType")
    public Result findEventConditionByEventType(String eventTypeId) {
        List<EventConditionVO> list = eventService.findEventConditionByEventType(eventTypeId);
        return Result.success(list);
    }

    /**
     * 获取立案时限分类
     *
     * @param conditionId
     * @return
     */
    @GetMapping("/findDeptTimeLimitByCondition")
    public Result findDeptTimeLimitByCondition(String conditionId) {
        List<DeptTimeLimitVO> list = eventService.findDeptTimeLimitByCondition(conditionId);
        return Result.success(list);
    }

    /**
     * 获取立案条件
     *
     * @param conditionId id
     * @return
     */
    @GetMapping("/getEventCondition")
    public Result getEventCondition(String conditionId) {
        List<EventConditionVO> conditionValueByRegion = eventService.findConditionValueByRegion(conditionId);
        return Result.success(conditionValueByRegion);
    }

    /**
     * 获取立案时限
     *
     * @param deptTimeLimitId
     * @return
     */
    @GetMapping("/findDeptTimeLimit")
    public Result findDeptTimeLimit(String deptTimeLimitId) {
        DeptTimeLimitVO vo = eventService.findDeptTimeLimit(deptTimeLimitId);
        return Result.success(vo);
    }

    /**
     * 所属区域获取网格
     * @param parentId 区域
     */
    @GetMapping("/findAllByParentId")
    public Result findAllByParentId(String parentId) {
        List<GridVO> list = gridService.findAllByParentId(parentId);
        return Result.success(list);
    }
    /**
     * 保存
     * @param eventDTO
     */
    @RequestMapping("/save")
    public void save(EventDTO eventDTO){
        eventService.save(eventDTO);
    }
    /**
     * 上报
     * @param eventDTO
     */
    @RequestMapping("/preReport")
    public void preReport(EventDTO eventDTO){
        eventService.saveAutoReport(eventDTO);
    }

    /**
     * 生成案卷号（案卷号：系统自动生成，生成规则：部件（简称C）或事件（E）+大类代码+小类代码+××××××××××（年：4位，月：2位，日：2位，序号：2位）即C01012019041101）
     * @param eventTypeId
     * @return
     */
    @RequestMapping("/createEventCode")
    public Result createEventCode(String eventTypeId) {
        return Result.success(eventService.createCode(eventTypeId));

    }

    /**
     * 核实反馈保存
     * @param statistics
     * @return
     */
    @PostMapping("/verify")
    public Result verify(Statistics statistics){
        statisticsService.update(statistics);
        return Result.success();
    }

    /**
     * 监督员信息核实
     *
     * @param statisticsDTO
     */
    @PostMapping("/completeByVerification")
    public Result completeByVerification(StatisticsDTO statisticsDTO) {
        String eventId = statisticsDTO.getEventId();
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        statistics.setOpinions(statisticsDTO.getOpinions());
        statisticsService.update(statistics);
        eventService.completeByVerification(statisticsDTO.getEventId(), null, statisticsDTO.getButtonText());
        return Result.success();
    }
    /**
     * 监督员案件核查
     *
     * @param statisticsDTO
     */
    @PostMapping("/completeByInspect")
    public Result completeByInspect(StatisticsDTO statisticsDTO) {
        String eventId = statisticsDTO.getEventId();
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        statistics.setOpinions(statisticsDTO.getOpinions());
        statisticsService.update(statistics);
        eventService.completeByInspect(statisticsDTO.getEventId(), null, statisticsDTO.getButtonText());
        return Result.success();
    }

    @GetMapping("getUserName")
    public Result getUserName(){
        return Result.success(SecurityUtil.getUsername());
    }
}
