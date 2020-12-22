package com.unicom.urban.management.api.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.entity.KV;
import com.unicom.urban.management.pojo.vo.*;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.kv.KVService;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 无线采集子系统
 *
 * @author liubozhi
 */
@RestController
@ResponseResultBody
@RequestMapping("/api/event")
public class  WirelessAcquisitionController {

    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private EventService eventService;
    @Autowired
    private GridService gridService;
    @Autowired
    private KVService kvService;

    /**
     * 获取案件类型
     *
     * @return 地址
     */
    @GetMapping("/allEventType")
    public List<EventTypeVO> aLLeventType() {
        return eventTypeService.getEventTypeList(2);
    }

    /**
     * 生成案卷号（案卷号：系统自动生成，生成规则：部件（简称C）或事件（E）+大类代码+小类代码+××××××××××（年：4位，月：2位，日：2位，序号：2位）即C01012019041101）
     *
     * @param eventTypeId
     * @return
     */
    @GetMapping("/createEventCode")
    public Result createEventCode(String eventTypeId) {
        if (StringUtils.isNotEmpty(eventTypeId)) {
            return Result.success(eventService.createCode(eventTypeId));
        }
        return Result.fail(500, "事件类型不能为空");

    }

    /**
     * 获取立案区域
     *
     * @param eventTypeId
     * @return
     */
    @GetMapping("/findEventConditionByEventType")
    public Result findEventConditionByEventType(String eventTypeId) {
        List<EventConditionVO> list = eventService.findEventConditionByEventType(eventTypeId);
        return Result.success(list);
    }

    /**
     * 获取立案时限分类
     *
     * @param conditionId
     * @return
     */
    @GetMapping("/findDeptTimeLimitByCondition")
    public Result findDeptTimeLimitByCondition(String conditionId) {
        List<DeptTimeLimitVO> list = eventService.findDeptTimeLimitByCondition(conditionId);
        return Result.success(list);
    }

    /**
     * 获取立案条件
     *
     * @param conditionId id
     * @return
     */
    @GetMapping("/getEventCondition")
    public Result getEventCondition(String conditionId) {
        List<EventConditionVO> conditionValueByRegion = eventService.findConditionValueByRegion(conditionId);
        return Result.success(conditionValueByRegion);
    }

    /**
     * 获取立案时限
     *
     * @param deptTimeLimitId
     * @return
     */
    @GetMapping("/findDeptTimeLimit")
    public Result findDeptTimeLimit(String deptTimeLimitId) {
        DeptTimeLimitVO vo = eventService.findDeptTimeLimit(deptTimeLimitId);
        return Result.success(vo);
    }

    /**
     * 所属区域获取网格
     *
     * @param parentId 区域
     */
    @GetMapping("/findAllByParentId")
    public Result findAllByParentId(String parentId) {
        List<GridVO> list = gridService.findAllByParentId(parentId);
        return Result.success(list);
    }

    /**
     * 获取所在区域
     *
     * @return 区域
     */
    @GetMapping("/getRegion")
    public Result getRegion() {
        List<GridVO> gridVOList = gridService.findAllByParentIsNull();
        return Result.success(gridVOList);
    }

    @GetMapping("/getEventType")
    public Result getEventType() {
        List<KV> eventTypeList = kvService.findByTableNameAndFieldName("event", "recType");
        return Result.success(eventTypeList);
    }

    @GetMapping("/tree/eventType")
    public Result eventTypeTree() {
        List<TreeVO> tree = eventTypeService.searchTree();
        return Result.success(tree);
    }

    @GetMapping("/getEventSourceForWirelessAcquisition")
    public Result getEventSourceForWirelessAcquisition() {
        List<KV> getEventSourceForWirelessAcquisition = kvService.findByTableNameAndFieldNameAndValue("event", "eventSource", "监督员上报");
        return Result.success(getEventSourceForWirelessAcquisition);
    }

    /**
     * 案件采集保存
     *
     * @param eventDTO 数据
     */
    @PostMapping("/saveTemp")
    public Result saveTemp(@Valid EventDTO eventDTO) {
        eventDTO.setSts(EventConstant.SUPERVISE_SAVE);
        eventService.saveTemp(eventDTO);
        return Result.success("保存成功");
    }


}
