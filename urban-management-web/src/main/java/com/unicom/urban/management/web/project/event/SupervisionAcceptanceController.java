package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.Event;
import com.unicom.urban.management.pojo.vo.EventOneVO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.Collections;

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
        model.addObject("level", kvService.findByTableNameAndFieldName("deptTimeLimit", "level"));
        //所属区域
        model.addObject("region", kvService.findByTableNameAndFieldName("event", "region"));
        //问题来源
        model.addObject("eventSource", kvService.findByTableNameAndFieldName("event", "eventSource"));
        //案件类型
        model.addObject("recType", kvService.findByTableNameAndFieldName("event", "recType"));
        //所在区域
        model.addObject("gridList", gridService.findAllByParentIsNull());
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

    /* ----------------------------------------------------------------- */

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

    /**
     * 案件受理-核实
     *
     * @return Result
     */
    @PostMapping("/receive")
    public Result Receive(EventDTO eventDTO) {
        eventDTO.setInitSts(2);
        eventService.save(eventDTO);
        return Result.success();
    }

    /**
     * 案件受理-保存
     *
     * @return Result
     */
    @PostMapping("/dispatch")
    public Result dispatch(EventDTO eventDTO) {
        eventDTO.setInitSts(3);
        eventService.save(eventDTO);
        return Result.success();
    }

    @GetMapping("/supervisionAcceptanceList")
    public Page<EventVO> supervisionAcceptanceList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        return eventService.search(eventDTO, pageable);
    }


    @GetMapping("/test")
    public void test() {
        Event one = eventService.findOne("3038fb44-bc37-4030-aec3-479385d39905");
        int i = 0;
    }

    @GetMapping("/findOne")
    public EventOneVO findOne(String eventId) {
        return eventService.findOneToVo(eventId);
    }

    /**
     * 公众信息待办列表
     *
     * @return list
     */
    @GetMapping("/selfProcessingList")
    public Page<EventVO> selfProcessingList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Collections.singletonList(EventConstant.ACCEPTANCE_CASE_REGISTRATION));
        return eventService.search(eventDTO, pageable);
    }

}
