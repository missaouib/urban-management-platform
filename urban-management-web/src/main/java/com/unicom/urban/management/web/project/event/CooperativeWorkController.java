package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.event.TaskProcessingService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collections;

/**
 * 协同工作子系统
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/event")
public class CooperativeWorkController {

    @Autowired
    private EventService eventService;
    @Autowired
    private KVService kvService;
    @Autowired
    private GridService gridService;
    @Autowired
    private TaskProcessingService taskProcessingService;

    /**
     * 案件处理
     *
     * @return 页面
     */
    @GetMapping("/toCooperativeWorkList")
    public ModelAndView toCooperativeWorkList(Model model, String eventId) {
        //问题来源
        model.addAttribute("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        model.addAttribute("gridList", gridService.searchAll());
        // 从工作台跳转过来传递的eventId
        model.addAttribute("eventId", eventId);
        return new ModelAndView(SystemConstant.PAGE + "/event/cooperativeWork/list");
    }

    @GetMapping("/cooperativeWorkList")
    public Page<EventVO> wirelessAcquisitionList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 已办案件
     *
     * @return 页面
     */
    @GetMapping("/toCasesHandledList")
    public ModelAndView toCasesHandledList() {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/event/closedCases/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        return new ModelAndView(SystemConstant.PAGE + "/event/casesHandled/list");
    }

    /**
     * 已结案件
     *
     * @return 页面
     */
    @GetMapping("/toClosedCasesList")
    public ModelAndView toClosedCasesList() {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/event/closedCases/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        return modelAndView;
    }

    /**
     * 挂账列表
     *
     * @return 页面
     */
    @GetMapping("/toOnAccountList")
    public ModelAndView toOnAccount(Model model, String eventId) {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/event/onAccount/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        // 从工作台跳转过来传递的eventId
        model.addAttribute("eventId", eventId);
        return modelAndView;
    }

    /**
     * 挂账列表
     *
     * @param eventDTO 条件
     * @param pageable 分页
     * @return list
     */
    @GetMapping("/onAccountList")
    public Page<EventVO> onAccountList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Collections.singletonList(EventConstant.RECOVERY));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 作废列表
     *
     * @return 页面
     */
    @GetMapping("/toCancelList")
    public ModelAndView toCancel(Model model, String eventId) {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/event/cancel/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        // 从工作台跳转过来传递的eventId
        model.addAttribute("eventId", eventId);
        return modelAndView;
    }

    /**
     * 作废列表
     *
     * @param eventDTO 条件
     * @param pageable 分页
     * @return list
     */
    @GetMapping("/cancelList")
    public Page<EventVO> cancelList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setCancel("1");
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 授权列表
     *
     * @return 页面
     */
    @GetMapping("/toAuthorizationList")
    public ModelAndView toAuthorization(Model model, String eventId) {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/event/authorization/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        // 从工作台跳转过来传递的eventId
        model.addAttribute("eventId", eventId);
        return modelAndView;
    }

    /**
     * 授权列表
     *
     * @param eventDTO 条件
     * @param pageable 分页
     * @return list
     */
    @GetMapping("/authorizationList")
    public Page<EventVO> authorizationList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Arrays.asList(
                EventConstant.DISPATCHER_DELAYED_APPROVAL,
                EventConstant.DISPATCHER_BACK_OFF_APPROVAL,
                EventConstant.SHIFT_LEADER_TO_VOID_APPROVAL,
                EventConstant.DISPATCHER_ON_ACCOUNT_APPROVAL));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 案件查询
     *
     * @return 页面
     */
    @GetMapping("/toCaseInquiryList")
    public ModelAndView toCaseInquiryList() {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/event/caseInquiry/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        modelAndView.addObject("taskNames", taskProcessingService.findTaskNames());


        return modelAndView;
    }

    /**
     * 督办案件list
     *
     * @return 督办页面list
     */
    @GetMapping("/toSuperviseCasesList")
    public ModelAndView toSuperviseCasesList(Model model, String eventId) {
        // 从工作台跳转过来传递的eventId
        model.addAttribute("eventId", eventId);
        return new ModelAndView(SystemConstant.PAGE + "/event/superviseCases/list");
    }


}
