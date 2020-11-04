package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
    private EventService eventService;

    @Autowired
    private KVService kvService;
    @Autowired
    private GridService gridService;

    @GetMapping("/toSupervisionAcceptanceList")
    public ModelAndView toSupervisionAcceptanceList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/supervisionAcceptance/list");
    }

    @GetMapping("/toSupervisionAcceptanceSave")
    public ModelAndView toSupervisionAcceptanceSave() {
        ModelAndView model = new ModelAndView(SystemConstant.PAGE + "/event/supervisionAcceptance/save");
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
        EventVO eventVO = new EventVO();
        eventVO.setCreateTime(LocalDateTime.now());
        model.addObject("eventVO", eventVO);
        return model;
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

    /**
     * 登记
     *
     * @return Result
     */
    @RequestMapping("register")
    public Result register(EventDTO event) {
        event.setSts(2);
        eventService.save(event);
        return Result.success();
    }

    @GetMapping("/supervisionAcceptanceList")
    public Page<EventVO> supervisionAcceptanceList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        return eventService.search(eventDTO, pageable);
    }


    @GetMapping("/test")
    public void test(){
//        eventService.chuli("cb00e285-ef81-47f5-b708-c783b81506f8", Collections.singletonList("1"),"1");
//        eventService.chuli("cb00e285-ef81-47f5-b708-c783b81506f8", Collections.singletonList("1"),"3");
//        eventService.chuli("cb00e285-ef81-47f5-b708-c783b81506f8", Collections.singletonList("1"),"4");
//        eventService.chuli("cb00e285-ef81-47f5-b708-c783b81506f8", Collections.singletonList("1"),"6");
        eventService.chuli("cb00e285-ef81-47f5-b708-c783b81506f8", Collections.singletonList("1"),"7");
    }

}
