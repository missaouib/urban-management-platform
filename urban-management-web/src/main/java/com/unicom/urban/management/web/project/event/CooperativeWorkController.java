package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    /**
     * 案件处理
     *
     * @return 页面
     */
    @GetMapping("/toCooperativeWorkList")
    public ModelAndView toCooperativeWorkList() {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/event/cooperativeWork/list");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //所属网格
        modelAndView.addObject("gridList", gridService.searchAll());
        return modelAndView;
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
    @GetMapping("/toOnAccount")
    public ModelAndView toOnAccount() {
        return new ModelAndView(SystemConstant.PAGE + "/event/onAccount/list");
    }

    /**
     * 作废列表
     *
     * @return 页面
     */
    @GetMapping("/toCancel")
    public ModelAndView toCancel() {
        return new ModelAndView(SystemConstant.PAGE + "/event/cancel/list");
    }

    /**
     * 授权批示
     *
     * @return 页面
     */
    @GetMapping("/toAuthorization")
    public ModelAndView toAuthorization() {
        return new ModelAndView(SystemConstant.PAGE + "/event/authorization/list");
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
        return modelAndView;
    }

    /**
     * 督办案件list
     *
     * @return 督办页面list
     */
    @GetMapping("/toSuperviseCasesList")
    public ModelAndView toSuperviseCasesList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/superviseCases/list");
    }

    /**
     * 督办案件
     *
     * @return 督办页面
     */
    @GetMapping("/toSupervise")
    public ModelAndView toSupervise() {
        return new ModelAndView(SystemConstant.PAGE + "/event/superviseCases/supervise");
    }

    /**
     * 督办案件
     *
     * @return 督办详情页面
     */
    @GetMapping("/toSuperviseCasesDetails")
    public ModelAndView toSuperviseCasesDetails() {
        return new ModelAndView(SystemConstant.PAGE + "/event/superviseCases/details");
    }

    /**
     * 督办案件
     *
     * @return 专业部门督办意见回复页面
     */
    @GetMapping("/toSuperviseCommentsReply")
    public ModelAndView toSuperviseCommentsReply() {
        return new ModelAndView(SystemConstant.PAGE + "/event/superviseCases/commentsReply");
    }

}
