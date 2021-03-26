package com.unicom.urban.management.api.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.EventFile;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.EventOneVO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.event.TaskProcessingService;
import com.unicom.urban.management.service.eventfile.EventFileService;
import com.unicom.urban.management.service.role.RoleService;
import com.unicom.urban.management.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * 监督受理子系统
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/api/event")
public class SupervisionAcceptanceController {

    @Autowired
    private EventService eventService;
    @Autowired
    private EventFileService eventFileService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TaskProcessingService taskProcessingService;

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
    public Result receive(@Valid EventDTO eventDTO) {
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
    public Result dispatch(@Valid EventDTO eventDTO) {
        eventDTO.setInitSts(3);
        eventService.save(eventDTO);
        return Result.success();
    }

    /**
     * 案件登记-修改
     *
     * @return Result
     */
    @PostMapping("/registerUpdate")
    public Result registerUpdate(@Valid EventDTO eventDTO) {
        eventService.registerUpdate(eventDTO);
        return Result.success();
    }

    /**
     * 获取自处理案件集合
     *
     * @param eventDTO 查询条件
     * @param pageable 分页
     * @return page
     */
    @GetMapping("/supervisionAcceptanceList")
    public Page<EventVO> supervisionAcceptanceList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Collections.singletonList(EventConstant.SELF_PROCESSING_AUDIT));
        return eventService.search(eventDTO, pageable);
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
        eventDTO.setTaskName(Arrays.asList(
                EventConstant.ACCEPTANCE_CASE_REGISTRATION,
                EventConstant.ACCEPTANCE_SEND_VERIFICATION,
                EventConstant.ACCEPTANCE_SEND_CHECK,
                EventConstant.ACCEPTANCE));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 待办案件列表
     *
     * @return list
     */
    @GetMapping("/sendVerificationList")
    public Page<EventVO> sendVerificationList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Collections.singletonList(EventConstant.ACCEPTANCE_SEND_VERIFICATION));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 待核查列列表
     *
     * @return list
     */
    @GetMapping("/sendCheckList")
    public Page<EventVO> sendCheckList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Arrays.asList(EventConstant.ACCEPTANCE_SEND_CHECK, EventConstant.ACCEPTANCE));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 任务处理待办列表
     *
     * @return list
     */
    @GetMapping("/taskProcessingList")
    public Page<EventVO> taskProcessingList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Arrays.asList(EventConstant.SHIFT_LEADER,
                EventConstant.DISPATCHER,
                EventConstant.PROFESSIONAL_AGENCY,
                EventConstant.CLOSE_TASK,
                EventConstant.ME_CLOSE_TASK));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 督办列表
     */
    @GetMapping("/superviseCasesList")
    public Page<EventVO> superviseCasesList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        Stream<String> stream = SecurityUtil.getRoleId().stream();
        if (stream.anyMatch(r -> r.equals(KvConstant.DISPATCHER_ROLE) || r.equals(User.ADMIN_USER_ID))) {
            eventDTO.setSupervision("1");
        }
        eventDTO.setTaskName(Collections.singletonList(EventConstant.PROFESSIONAL_AGENCY));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 案件查询列表
     *
     * @return list
     */
    @GetMapping("/eventList")
    public Page<EventVO> eventList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 已结案件查询列表
     *
     * @return list
     */
    @GetMapping("/closeEventList")
    public Page<EventVO> closeEventList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setClose(Arrays.asList(KvConstant.CLOS_ETESK, KvConstant.TO_VOID));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 已办案件查询列表
     *
     * @return list
     */
    @GetMapping("/meEventList")
    public Page<EventVO> meEventList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setMe("1");
        return eventService.search(eventDTO, pageable);
    }


    /**
     * 受理员完成任务 并且 激活监督员(领取任务)核实
     * button 11
     *
     * @param eventDTO 事件id 指派的人的id 按钮
     */
    @PostMapping("/completeByReceptionistWithSendVerification")
    public Result completeByReceptionistWithSendVerification(EventDTO eventDTO) {
        List<EventFile> eventFileList = new ArrayList<>();
        List<EventFile> eventFileListImage = eventFileService.joinEventFileListToObjet(eventDTO.getImageUrlList(), EventFile.FileType.IMAGE);
        List<EventFile> eventFileListVideo = eventFileService.joinEventFileListToObjet(eventDTO.getVideoUrlList(), EventFile.FileType.VIDEO);
        List<EventFile> eventFileListMusic = eventFileService.joinEventFileListToObjet(eventDTO.getMusicUrlList(), EventFile.FileType.AUDIO);
        eventFileList.addAll(eventFileListImage);
        eventFileList.addAll(eventFileListVideo);
        eventFileList.addAll(eventFileListMusic);
        eventDTO.setEventFileList(eventFileList);
        switch (eventDTO.getButton()) {
            /*
             * 受理员完成任务 并且 激活监督员(领取任务)核实
             * button 11
             */
            case "11":
                eventService.completeByReceptionist(eventDTO);
                break;
            /*
             * 1首先受理员领取任务
             * 2然后完成任务
             * 3最后
             * 3.1激活监督员(领取任务)重新核实
             * 3.2值班长受理
             * 3.3不予受理(流程结束)
             */
            case "2":
                eventService.completeByReceptionistForNotDo(eventDTO);
                break;
            case "3":
                eventService.completeByReceptionistForDo(eventDTO);
                break;
            case "13":
                eventService.completeByReceptionist(eventDTO);
                break;
            /*
             * 受理员完成任务 并且 激活监督员(领取任务)核查
             */
            case "14":
                eventService.completeByReceptionistWithClaim(eventDTO);
                break;
            case "10":
                eventService.completeByReceptionistForDo(eventDTO);
                break;
            case "16":
                eventService.completeByReceptionist(eventDTO);
                break;
            case "17":
                eventService.completeByReceptionistForDo(eventDTO);
                break;
            default:
                return Result.fail(500, "未检测到应有的步骤");
        }
        return Result.success();
    }

    /**
     * 自处理审核
     *
     * @param eventDTO 按钮 / eventId
     * @return 结果
     */
    @PostMapping("/completeBySelfProcessingAudit")
    public Result completeBySelfProcessingAudit(EventDTO eventDTO) {
        List<EventFile> eventFileList = eventFileService.joinEventFileListToObjet(eventDTO.getImageUrlList(), EventFile.FileType.IMAGE);
        eventDTO.setEventFileList(eventFileList);
        switch (eventDTO.getButton()) {
            case "20":
                eventService.completeForInvalidCases(eventDTO);
                break;
            case "21":
                eventService.completeForClosingAndFiling(eventDTO);
                break;
            default:
                return Result.fail(500, "未检测到应有的步骤");
        }
        return Result.success();
    }

    /**
     * 获取有监督员角色的人
     *
     * @return 人
     */
    @GetMapping("/getUserListForSupervisor")
    public Result getUserListForSupervisor(String gridId) {
        List<UserVO> userList = roleService.findUserListForSupervision(KvConstant.SUPERVISOR_ROLE, gridId);
        return Result.success(userList);
    }


    /**
     * 获取环节名称
     */
    @GetMapping("/getTaskName")
    public Result getTaskName(){
        return Result.success(taskProcessingService.findTaskNames());
    }

}
