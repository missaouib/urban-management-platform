package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.mapper.EventMapper;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.entity.EventType;
import com.unicom.urban.management.pojo.vo.DeptTimeLimitVO;
import com.unicom.urban.management.pojo.vo.EventConditionVO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.service.depttimelimit.DeptTimeLimitService;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private DeptTimeLimitService depTimeLimitService;
    @Autowired
    private GridService gridService;
    @Autowired
    private KVService kvService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private DeptTimeLimitService deptTimeLimitService;
    @GetMapping("/toWirelessAcquisitionList")
    public ModelAndView toWirelessAcquisitionList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/list");
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
        //获取当前登录人
//        String userId = SecurityUtil.getUserId();
//        User user = userService.findOne(userId);
//        model.addObject("userName",user.getUsername());
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
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 获取立案区域
     * @param eventTypeId
     * @return
     */
    @GetMapping("/findEventConditionByEventType/{eventTypeId}")
    public Result findEventConditionByEventType(@PathVariable String eventTypeId){
        List<EventConditionVO> list =  eventService.findEventConditionByEventType(eventTypeId);
        return Result.success(list);
    }


    /**
     * 获取立案时限分类
     * @param conditionId
     * @return
     */
    @GetMapping("/findDeptTimeLimitByCondition/{conditionId}")
    public Result findDeptTimeLimitByCondition(@PathVariable String conditionId){
        List<DeptTimeLimitVO> list = eventService.findDeptTimeLimitByCondition(conditionId);
        return Result.success(list);
    }

    /**
     * 获取立案时限
     * @param deptTimeLimitId
     * @return
     */
    @GetMapping("/findDeptTimeLimit/{deptTimeLimitId}")
    public Result findDeptTimeLimit(@PathVariable String deptTimeLimitId){
        DeptTimeLimitVO vo = eventService.findDeptTimeLimit(deptTimeLimitId);
        return Result.success(vo);
    }

    /**
     * 所属区域获取网格
     * @param parentId 区域
     */
    @GetMapping("/findAllByParentId/{parentId}")
    public Result findAllByParentId(@PathVariable String parentId) {
        List<GridVO> list = gridService.findAllByParentId(parentId);
        return Result.success(list);
    }
    /**
     * 保存
     * @param event
     */
    @RequestMapping("/save")
    public void save(Event event){
        event.setSts(0);
        eventService.save(event);
    }
    /**
     * 准备上报
     * @param event
     */
    @RequestMapping("/preReport")
    public void preReport(Event event){
        event.setSts(1);
        eventService.save(event);
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
}
