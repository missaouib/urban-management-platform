package com.unicom.urban.management.api.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;

/**
 * 协同工作子系统
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/api/event")
public class CooperativeWorkController {

    @Autowired
    private EventService eventService;

    @GetMapping("/cooperativeWorkList")
    public Page<EventVO> wirelessAcquisitionList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        return eventService.search(eventDTO, pageable);
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

}
