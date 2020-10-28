package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.Event;
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
        model.addObject("recType", kvService.findByTableNameAndFieldName("event","recType"));
        //获取当前登录人
//        String userId = SecurityUtil.getUserId();
//        User user = userService.findOne(userId);
//        model.addObject("userName",user.getUsername());
        return model;
    }

    @GetMapping("/toWirelessAcquisitionUpdate/{id}")
    public ModelAndView toWirelessAcquisitionListUpdate(@PathVariable String id, Model model) {
        model.addAttribute("id", id);
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
    @RequestMapping("/findEventConditionByEventType/{eventTypeId}")
    private List<EventConditionVO> findEventConditionByEventType(@PathVariable String eventTypeId){
        return eventService.findEventConditionByEventType(eventTypeId);
    }

    /**
     * 获取立案条件
     * @param region
     * @return
     */
    @RequestMapping("/findConditionValueByRegion/{region}")
    private List<EventConditionVO> findConditionValueByRegion(@PathVariable String region){
        return eventService.findConditionValueByRegion(region);
    }

    /**
     * 获取立案时限分类
     * @param condition
     * @return
     */
    @RequestMapping("/findDeptTimeLimitByCondition/{condition}")
    private List<DeptTimeLimitVO> findDeptTimeLimitByCondition(@PathVariable String condition){
        return eventService.findDeptTimeLimitByCondition(condition);
    }

    /**
     * 获取立案时限
     * @param deptTimeLimit
     * @return
     */
    @RequestMapping("/findDeptTimeLimit/{deptTimeLimit}")
    private DeptTimeLimitVO findDeptTimeLimit(@PathVariable String deptTimeLimit){
        return eventService.findDeptTimeLimit(deptTimeLimit);
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
    public List<GridVO> findAllByKvId(@PathVariable String kvId) {
        List<GridVO> gridVOList = new ArrayList<>();
        GridVO gridVO1 = new GridVO();
        gridVO1.setId("15d84eb0-83d7-46a4-a8aa-fcfdad5aefee");
        gridVO1.setGridName("2");
        GridVO gridVO2 = new GridVO();
        gridVO2.setId("b279dad8-df58-4276-9fc9-91d9180981bd");
        gridVO2.setGridName("1");
        gridVOList.add(gridVO1);
        gridVOList.add(gridVO2);
        return gridVOList;
    }

    /**
     * 保存
     * @param event
     */
    @RequestMapping("/save")
    public void save(Event event){
        event.setCreateTime(LocalDateTime.now());
        event.setSts(EventConstant.SUPERVISE_SAVE);
        eventService.save(event);
    }
    /**
     * 准备上报
     * @param event
     */
    @RequestMapping("/preReport")
    public void preReport(Event event){
        event.setCreateTime(LocalDateTime.now());
        event.setSts(EventConstant.SUPERVISE_REPORTING);
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
