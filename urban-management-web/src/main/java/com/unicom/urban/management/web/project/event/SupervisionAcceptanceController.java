package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.service.kv.KVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

/**
 * 监督受理子系统
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/event")
public class SupervisionAcceptanceController {

    @Autowired
    private KVService kvService;

    @GetMapping("/toSupervisionAcceptanceList")
    public ModelAndView toSupervisionAcceptanceList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/supervisionAcceptance/list");
    }

    @GetMapping("/toSupervisionAcceptanceSave")
    public ModelAndView toSupervisionAcceptanceSave() {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/event/supervisionAcceptance/save");
        //问题来源
        modelAndView.addObject("eventSource", kvService.findByTableNameAndFieldName("event","eventSource"));
        EventVO eventVO = new EventVO();
        eventVO.setCreateTime(LocalDateTime.now());
        modelAndView.addObject("eventVO",eventVO);
        return modelAndView;
    }

    @GetMapping("/toSupervisionAcceptanceUpdate/{id}")
    public ModelAndView toSupervisionAcceptanceUpdate(@PathVariable String id, Model model) {
        model.addAttribute("id", id);
        return new ModelAndView(SystemConstant.PAGE + "/event/supervisionAcceptance/update");
    }

    /**
     * 自处理案件列表
     *
     * @return list
     */
    @GetMapping("/toSelfProcessingList")
    public ModelAndView toSelfProcessingList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/selfProcessing/list");
    }

    /**
     * 案件派核实列表
     *
     * @return list
     */
    @GetMapping("/toSendVerificationList")
    public ModelAndView toSendVerificationList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/sendVerification/list");
    }

    /**
     * 案件派核查列表
     *
     * @return list
     */
    @GetMapping("/toSendCheckList")
    public ModelAndView toSendCheckList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/sendCheck/list");
    }

}
