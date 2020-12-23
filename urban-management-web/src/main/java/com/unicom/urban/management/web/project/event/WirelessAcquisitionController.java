package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.dto.StatisticsDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.EventFile;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.vo.*;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.eventfile.EventFileService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private StatisticsService statisticsService;
    @Autowired
    private EventFileService eventFileService;
    @GetMapping("/toWirelessAcquisitionList")
    public ModelAndView toWirelessAcquisitionList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/list");
    }
    @GetMapping("/toCaseHistoryList")
    public ModelAndView toCaseHistoryList() {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/caseHistory/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        return modelAndView;
    }
    @GetMapping("/toCaseInspectList")
    public ModelAndView toCaseInspectList() {
        ModelAndView modelAndView =  new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/caseInspect/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        return modelAndView;
    }
    @GetMapping("/toCaseInvalidList")
    public ModelAndView toCaseInvalidList() {
        ModelAndView modelAndView =  new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/caseInvalid/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        return modelAndView;
    }
    @GetMapping("/toCaseVerifyList")
    public ModelAndView toCaseVerifyList() {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/caseVerify/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        return modelAndView;
    }

    @GetMapping("/toWirelessAcquisitionSave")
    public ModelAndView toWirelessAcquisitionListSave() {
        ModelAndView model = new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/save");
        //计时等级
        model.addObject("level", kvService.findByTableNameAndFieldName("deptTimeLimit","level"));
        //所在区域
        model.addObject("region", kvService.findByTableNameAndFieldName("event","region"));
        //问题来源
        model.addObject("eventSource", kvService.findByTableNameAndFieldName("event","eventSource"));
        //案件类型
        model.addObject("recType", kvService.findByTableNameAndFieldName("event","recType"));
        //所属社区
        model.addObject("gridList",gridService.findAllByParentIsNull());
        return model;
    }

    @GetMapping("/toWirelessAcquisitionUpdate/{id}")
    public ModelAndView toWirelessAcquisitionListUpdate(@PathVariable String id) {
        ModelAndView model = new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/update");
        EventOneVO vo= eventService.findOneToVo(id);
        Event event = eventService.findOne(id);
        model.addObject("eventOneVO",vo);
        //立案区域
        model.addObject("eventCondition",eventService.findEventConditionByEventType(event.getEventType().getId()));
        //立案条件
        model.addObject("eventTerm",eventService.findEventConditionByEventType(event.getCondition().getId()));
        //计时等级
        model.addObject("level", kvService.findByTableNameAndFieldName("deptTimeLimit","level"));
        //所在区域
        model.addObject("region", kvService.findByTableNameAndFieldName("event","region"));
        //事件来源
        model.addObject("eventSource", kvService.findByTableNameAndFieldName("event","eventSource"));
        //案件类型
        model.addObject("recType", kvService.findByTableNameAndFieldName("event","recType"));
        //所属社区
        model.addObject("gridList",gridService.findAllByParentIsNull());
        model.addObject("files",vo.getFile());
        return model;
    }

    @GetMapping("/wirelessAcquisitionList")
    public Page<EventVO> wirelessAcquisitionList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setSts(EventConstant.SUPERVISE_SAVE);
        return eventService.search(eventDTO, pageable);
    }
    /**
     * 案件核实
     * @param eventDTO
     * @param pageable
     * @return
     */
    @GetMapping("/caseVerifyList")
    public Page<EventVO> caseVerifyList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
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
     * 无效案件
     * @param eventDTO
     * @param pageable
     * @return
     */
    @GetMapping("/caseInvalidList")
    public Page<EventVO> caseInvalidList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        //查询不予受理案件
        eventDTO.setNotOperate(1);
        return eventService.search(eventDTO, pageable);
    }
    /**
     * 案件历史
     * @param eventDTO
     * @param pageable
     * @return
     */
    @GetMapping("/caseHistoryList")
    public Page<EventVO> caseHistoryList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
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
     * 案件采集保存
     * @param eventDTO
     */
    @RequestMapping("/saveTemp")
    public void saveTemp(EventDTO eventDTO){
        eventDTO.setSts(EventConstant.SUPERVISE_SAVE);
        uploadFiles(eventDTO);
        eventService.saveTemp(eventDTO);
    }

    /**
     * 案件采集修改
     * @param eventDTO
     */
    @RequestMapping("/updateTemp")
    public void updateTemp(EventDTO eventDTO){
        uploadFiles(eventDTO);
        eventService.updateTemp(eventDTO);
    }
    /**
     * 案件采集删除
     * @param id
     */
    @RequestMapping("/remove/{id}")
    public void remove(@PathVariable String id){
        Event event = eventService.findOne(id);
        event.setPetitioner(null);
        eventService.update(event);
        eventService.remove(id);
    }
    /**
     * 案件采集上报
     * @param eventDTO
     */
    @RequestMapping("/preReport")
    public void preReport(EventDTO eventDTO){
        eventDTO.setSts(null);
        uploadFiles(eventDTO);
        if (eventDTO.getDoBySelf()!=null) {
            eventService.saveAutoReport(eventDTO);
        }else {
            eventService.saveReport(eventDTO);
        }
    }

    private void uploadFiles(EventDTO eventDTO) {
        eventService.uploadFiles(eventDTO);

    }

    /**
     * 生成案卷号（案卷号：系统自动生成，生成规则：部件（简称C）或事件（E）+大类代码+小类代码+××××××××××（年：4位，月：2位，日：2位，序号：2位）即C01012019041101）
     * @param eventTypeId
     * @return
     */
    @RequestMapping("/createEventCode")
    public Result createEventCode(String eventTypeId) {
        if (StringUtils.isNotEmpty(eventTypeId)) {
            return Result.success(eventService.createCode(eventTypeId));
        }
        return Result.success();

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
        List<EventFile> eventFileList = eventFileService.joinEventFileListToObjet(statisticsDTO.getImageUrlList(),1);
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        statistics.setOpinions(statisticsDTO.getOpinions());
        statistics.setEventFileList(eventFileList);
        statisticsService.update(statistics);
        eventService.completeByVerification(statisticsDTO.getEventId(), statisticsDTO.getButtonText());
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
        List<EventFile> eventFileList = eventFileService.joinEventFileListToObjet(statisticsDTO.getImageUrlList(),1);
        statistics.setOpinions(statisticsDTO.getOpinions());
        statistics.setEventFileList(eventFileList);
        statisticsService.update(statistics);
        eventService.completeByInspect(statisticsDTO.getEventId(), statisticsDTO.getButtonText());
        return Result.success();
    }

    @GetMapping("getUserName")
    public Result getUserName(){
        return Result.success(SecurityUtil.getUsername());
    }

    /**
     * 列表页案件采集上报
     * @param id
     */
    @RequestMapping("reportOnList")
    public Result reportOnList(String id){
        eventService.reportOnList(id);
        return Result.success();

    }

    /**
     * 获取地图上的位置坐标
     * @return
     */
    @GetMapping("getPoint")
    public Result getShowPoint(String eventId){
        EventVO vo = eventService.getShowPoint(eventId);
        return Result.success(vo);
    }

}
