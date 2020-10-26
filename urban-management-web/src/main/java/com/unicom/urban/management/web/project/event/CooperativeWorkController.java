package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 案件处理
     *
     * @return 页面
     */
    @GetMapping("/toCooperativeWorkList")
    public ModelAndView toCooperativeWorkList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/cooperativeWork/list");
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
        return new ModelAndView(SystemConstant.PAGE + "/event/closedCases/list");
    }

    /**
     * 案件查询
     *
     * @return 页面
     */
    @GetMapping("/toCaseInquiryList")
    public ModelAndView toCaseInquiryList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/caseInquiry/list");
    }

    /**
     * 督办案件
     *
     * @return 督办页面
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
