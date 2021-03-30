package com.unicom.urban.management.api.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.EventConstant;
import com.unicom.urban.management.common.properties.GisServiceProperties;
import com.unicom.urban.management.common.util.RestTemplateUtil;
import com.unicom.urban.management.pojo.RestReturn;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.SecurityUserBean;
import com.unicom.urban.management.pojo.dto.EventAppDTO;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.dto.StatisticsDTO;
import com.unicom.urban.management.pojo.dto.TrajectoryDTO;
import com.unicom.urban.management.pojo.entity.EventFile;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.entity.KV;
import com.unicom.urban.management.pojo.entity.Statistics;
import com.unicom.urban.management.pojo.vo.*;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.eventfile.EventFileService;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.idioms.IdiomsService;
import com.unicom.urban.management.service.kv.KVService;
import com.unicom.urban.management.service.process.ProcessService;
import com.unicom.urban.management.service.statistics.StatisticsService;
import com.unicom.urban.management.service.trajectory.TrajectoryService;
import com.unicom.urban.management.util.SecurityUtil;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * 无线采集子系统
 *
 * @author liubozhi
 */
@RestController
@ResponseResultBody
@RequestMapping("/api/event")
public class WirelessAcquisitionController {

    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private EventService eventService;
    @Autowired
    private GridService gridService;
    @Autowired
    private KVService kvService;
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private EventFileService eventFileService;
    @Autowired
    private TrajectoryService trajectoryService;
    @Autowired
    private GisServiceProperties gisServiceProperties;
    @Autowired
    private ProcessService processService;
    @Autowired
    private IdiomsService idiomsService;

    /**
     * 获取案件类型
     *
     * @return 地址
     */
    @GetMapping("/allEventType")
    public List<EventTypeVO> allEventType() {
        return eventTypeService.getEventTypeList(2);
    }

    /**
     * 生成案卷号（案卷号：系统自动生成，生成规则：部件（简称C）或事件（E）+大类代码+小类代码+××××××××××（年：4位，月：2位，日：2位，序号：2位）即C01012019041101）
     *
     * @param eventTypeId id
     * @return 案卷号
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
     * @param eventTypeId id
     * @return 立案区域
     */
    @GetMapping("/findEventConditionByEventType")
    public Result findEventConditionByEventType(String eventTypeId) {
        List<EventConditionVO> list = eventService.findEventConditionByEventType(eventTypeId);
        return Result.success(list);
    }

    /**
     * 获取立案时限分类
     *
     * @param conditionId id
     * @return 立案时限分类
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
     * @return 立案条件
     */
    @GetMapping("/getEventCondition")
    public Result getEventCondition(String conditionId) {
        List<EventConditionVO> conditionValueByRegion = eventService.findConditionValueByRegion(conditionId);
        return Result.success(conditionValueByRegion);
    }

    /**
     * 获取立案时限
     *
     * @param deptTimeLimitId id
     * @return 立案时限
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

    @GetMapping("/getEventSourceForHotLine")
    public Result getEventSourceForHotLine() {
        List<KV> getEventSourceForWirelessAcquisition = kvService.findByTableNameAndFieldNameAndValue("event", "eventSource", "热线上报");
        return Result.success(getEventSourceForWirelessAcquisition);
    }

    /**
     * 案件采集保存 - 弃用
     *
     * @param eventAppDTO 数据
     */
    @PostMapping("/delete/saveTemp")
    public Result saveTemp(@Valid EventAppDTO eventAppDTO) {
        EventDTO eventDTO = new EventDTO();
        BeanUtils.copyProperties(eventAppDTO, eventDTO);
        eventDTO.setSts(EventConstant.SUPERVISE_SAVE);
        eventService.saveTempForApi(eventDTO);
        return Result.success("保存成功");
    }

    /**
     * 案件采集保存
     *
     * @param eventDTO 参数
     */
    @PostMapping("/saveTemp")
    public Result saveTemp(EventDTO eventDTO){
        eventDTO.setSts(EventConstant.SUPERVISE_SAVE);
        eventService.uploadFiles(eventDTO);
        eventService.saveTemp(eventDTO);
        return Result.success("保存成功");
    }

    /**
     * 列表页案件采集上报
     * @param ids 主键集合
     */
    @PostMapping("/reportOnList")
    public Result reportOnList(String ids){
        eventService.reportOnList(ids);
        return Result.success("上报成功");
    }

    /**
     * 案件核实
     *
     * @param eventDTO 查询条件
     * @param pageable 分页
     * @return list
     */
    @GetMapping("/caseVerifyList")
    public Page<EventVO> caseVerifyList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Collections.singletonList(EventConstant.ACCEPTANCE_CASE_VERIFICATION));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 事件详情
     *
     * @param eventId 事件id
     * @return 数据
     */
    @GetMapping("/eventFindOne")
    public Result findOne(String eventId) {
        EventOneVO oneToVo = eventService.findOneToVo(eventId);
        return Result.success(oneToVo);
    }

    /**
     * 获取下一环节完成按钮
     *
     * @param eventId 事件id
     * @return button
     */
    @GetMapping("/getButton/{eventId}")
    public Result getButton(@PathVariable String eventId) {
        List<EventButtonVO> button = eventService.getButton(eventId);
        return Result.success(button);
    }

    /**
     * 监督员信息核实
     *
     * @param statisticsDTO 数据
     */
    @PostMapping("/completeByVerification")
    public Result completeByVerification(StatisticsDTO statisticsDTO) {
        String eventId = statisticsDTO.getEventId();
        List<EventFile> eventFileList = eventFileService.joinEventFileListToObjet(statisticsDTO.getImageUrlList(), EventFile.FileType.IMAGE);
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        statistics.setOpinions(statisticsDTO.getOpinions());
        statistics.setEventFileList(eventFileList);
        statisticsService.update(statistics);
        eventService.completeByVerification(statisticsDTO.getEventId(), statisticsDTO.getButtonText());
        return Result.success("成功");
    }

    /**
     * 案件核查
     *
     * @param eventDTO 查询条件
     * @param pageable 分页
     * @return list
     */
    @GetMapping("/caseInspectList")
    public Page<EventVO> caseInspectList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Collections.singletonList(EventConstant.ACCEPTANCE_CASE_INSPECT));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 监督员案件核查
     *
     * @param statisticsDTO 数据
     */
    @PostMapping("/completeByInspect")
    public Result completeByInspect(StatisticsDTO statisticsDTO) {
        String eventId = statisticsDTO.getEventId();
        Statistics statistics = statisticsService.findByEventIdAndEndTimeIsNull(eventId);
        List<EventFile> eventFileList = eventFileService.joinEventFileListToObjet(statisticsDTO.getImageUrlList(), EventFile.FileType.IMAGE);
        statistics.setOpinions(statisticsDTO.getOpinions());
        statistics.setEventFileList(eventFileList);
        statisticsService.update(statistics);
        eventService.completeByInspect(statisticsDTO.getEventId(), statisticsDTO.getButtonText());
        return Result.success("成功");
    }

    /**
     * 无效案件
     *
     * @param eventDTO 查询条件
     * @param pageable 分页
     * @return list
     */
    @GetMapping("/caseInvalidList")
    public Page<EventVO> caseInvalidList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setNotOperate(1);
        return eventService.search(eventDTO, pageable);
    }

    @GetMapping("/findOpinions")
    public Result findOpinions(String statisticsId) {
        StatisticsVO statisticsVO = statisticsService.findById(statisticsId);
        return Result.success(statisticsVO);
    }

    /**
     * 案件历史
     *
     * @param eventDTO 查询条件
     * @param pageable 分页
     * @return list
     */
    @GetMapping("/caseHistoryList")
    public Page<EventVO> caseHistoryList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 轨迹记录
     *
     * @return 成功
     */
    @PostMapping("/saveTrackLog")
    public Result saveTrackLog(TrajectoryDTO trajectoryDTO) {
        SecurityUserBean user = SecurityUtil.getUser();
        trajectoryService.saveTrajectory(trajectoryDTO, user.castToUser());
        return Result.success("成功");
    }

    /**
     * 逆地理编码 - 本地库
     */
    @GetMapping("localReverseGeocoding")
    public Map<String, String> localReverseGeocoding(double x, double y) throws DataAccessException {
        RestReturn body = RestTemplateUtil.get(gisServiceProperties.getUrl() + "/restApi/LocalReverseGeocoding?x=" + x + "&y=" + y, RestReturn.class).getBody();
        assert body != null;
        return (Map<String, String>) body.getData();
    }

    /**
     * 地理编码 - 本地库
     */
    @GetMapping("localGeocoding")
    public Object localGeocoding(String address) throws DataAccessException {
        RestReturn body = RestTemplateUtil.get(gisServiceProperties.getUrl() + "/restApi/LocalGeocoding?address=" + address, RestReturn.class).getBody();
        assert body != null;
        return body.getData();
    }

    /**
     * 点击网格反差信息
     */
    @GetMapping("/getGridByCheckLayer")
    public Result getGridByCheckLayer(String x, String y) {
        RestReturn body = RestTemplateUtil.get(gisServiceProperties.getUrl() + "/queryGridByXY?x=" + x + "&y=" + y, RestReturn.class).getBody();
        Map<String, String> dataMap = (Map<String, String>) body.getData();
        String objId = dataMap.get("id");
        String multiPolygon = dataMap.get("multiPolygon");
        Grid byGridCode = gridService.findByGridCode(objId);
        List<Map<String, Object>> mapList = new ArrayList<>(4);
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", byGridCode.getId());
        map.put("name", byGridCode.getGridName());
        map.put("level", byGridCode.getLevel());
        map.put("user", SecurityUtil.getUsername());
        mapList.add(map);
        gridService.addMapToList(byGridCode, mapList);
        Map<String, Object> newMap = new HashMap<>(2);
        newMap.put("multiPolygon", multiPolygon);
        newMap.put("mapList", mapList);
        return Result.success(newMap);
    }

    /**
     * 获取草稿箱列表
     *
     * @param eventDTO 查询条件
     * @param pageable 分页
     * @return page
     */
    @GetMapping("/wirelessAcquisitionList")
    public Page<EventVO> wirelessAcquisitionList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setSts(EventConstant.SUPERVISE_SAVE);
        return eventService.search(eventDTO, pageable);
    }

    /**
     * 案件采集修改
     *
     * @param eventDTO 修改数据
     */
    @PostMapping("/updateTemp")
    public Result updateTemp(EventDTO eventDTO) {
        eventService.uploadFiles(eventDTO);
        eventService.updateTemp(eventDTO);
        return Result.success("修改成功");
    }

    /**
     * 案件采集批量删除
     *
     * @param ids id
     */
    @PostMapping("/remove")
    public Result remove(String ids) {
        eventService.remove(ids);
        return Result.success("删除成功");
    }

    /**
     * 获取流转记录
     *
     * @param eventId eventId
     * @return list
     */
    @GetMapping("/byEventId")
    public Result statistics(String eventId) {
        List<StatisticsVO> statisticsVO = statisticsService.findByEventId(eventId);
        return Result.success(statisticsVO);
    }

    /**
     * 获取阶段图
     *
     * @return 编号
     */
    @GetMapping("/getNodeName")
    public Result getNodeName(String eventId) {
        int nodeName = processService.findAllByNodeNameAndParentIsNotNull(eventId);
        return Result.success(nodeName);
    }

    /**
     * 惯用语
     *
     * @return list
     */
    @GetMapping("/getIdioms")
    public Result getIdiomsList() {
        List<String> list = idiomsService.findAllIdiomsValue();
        return Result.success(list);
    }


    /**
     * 设置转应急状态
     *
     * @param eventId
     * @return
     */
    @PostMapping("/urgent")
    public Result changeUrgentUrl(String eventId) {
        eventService.changeUrgent(eventId);
        return Result.success();
    }

    @GetMapping("/findUrgent")
    public Result findUrgent(String eventId) {
        int type = eventService.findUrgent(eventId);
        return Result.success(type);
    }

}
