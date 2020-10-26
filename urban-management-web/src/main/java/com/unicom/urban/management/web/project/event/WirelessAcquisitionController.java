package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.EventCondition;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.KV;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.pojo.vo.GridVO;
import com.unicom.urban.management.service.depttimelimit.DeptTimeLimitService;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
    @GetMapping("/toWirelessAcquisitionList")
    public ModelAndView toWirelessAcquisitionList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/list");
    }

    @GetMapping("/toWirelessAcquisitionSave")
    public ModelAndView toWirelessAcquisitionListSave() {
        ModelAndView model = new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/save");
        //立案条件
       // model.addObject("eventType",this.findEventConditionByEventType(id));
        //案件等级
        model.addObject("level", kvService.findByTableNameAndFieldName("deptTimeLimit","level"));
        //所属区域
        model.addObject("region", kvService.findByTableNameAndFieldName("event","region"));
        //问题来源
        model.addObject("eventSource", kvService.findByTableNameAndFieldName("event","eventSource"));
        //案件类型
        model.addObject("recTypeId", kvService.findByTableNameAndFieldName("event","recTypeId"));
        //获取当前登录人
//        String userId = SecurityUtil.getUserId();
//        User user = userService.findOne(userId);
//        model.addObject("userName",user.getUsername());
        return model;
    }

    @GetMapping("/toWirelessAcquisitionUpdate/{id}")
    public ModelAndView toWirelessAcquisitionListUpdate(@PathVariable String id) {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/update");
    }

    @GetMapping("/eventList")
    public Page<EventVO> eventList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 获取立案条件
     * @param eventTypeId
     * @return
     */
    @RequestMapping("/findEventConditionByEventType/{eventTypeId}")
    private List<EventCondition> findEventConditionByEventType(@PathVariable String eventTypeId){
        return eventService.findEventConditionByEventType(eventTypeId);
    }

    /**
     * 案件等级与时限
     * @param eventTypeId
     * @param levelId 案件等级
     * @return 处理时限
     */
    @RequestMapping("/getDeptTimeLimitByLevel/{eventTypeId}/{levelId}")
    public Integer getDeptTimeLimitByLevel(@PathVariable String eventTypeId, @PathVariable String levelId){
        return depTimeLimitService.findByEventType_IdAndLevel_Id(eventTypeId, levelId);
    }
    /**
     * 所属区域kvId获取网格
     * @param kvId 区域kvId
     */
    @RequestMapping("/findAllByKvId/{kvId}")
    public List<GridVO> findAllByKvId(@PathVariable String kvId){
        return gridService.findAllByKvId(kvId);
    }
}
