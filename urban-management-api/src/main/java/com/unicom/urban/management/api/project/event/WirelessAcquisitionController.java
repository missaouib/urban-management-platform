package com.unicom.urban.management.api.project.event;

import cn.hutool.json.JSONObject;
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
import org.apache.commons.lang3.StringUtils;
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
 * ?????????????????????
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
     * ??????????????????
     *
     * @return ??????
     */
    @GetMapping("/allEventType")
    public List<EventTypeVO> allEventType() {
        return eventTypeService.getEventTypeList(2);
    }

    /**
     * ?????????????????????????????????????????????????????????????????????????????????C???????????????E???+????????????+????????????+?????????????????????????????4????????????2????????????2???????????????2?????????C01012019041101???
     *
     * @param eventTypeId id
     * @return ?????????
     */
    @GetMapping("/createEventCode")
    public Result createEventCode(String eventTypeId) {
        if (StringUtils.isNotEmpty(eventTypeId)) {
            return Result.success(eventService.createCode(eventTypeId));
        }
        return Result.fail(500, "????????????????????????");

    }

    /**
     * ??????????????????
     *
     * @param eventTypeId id
     * @return ????????????
     */
    @GetMapping("/findEventConditionByEventType")
    public Result findEventConditionByEventType(String eventTypeId) {
        List<EventConditionVO> list = eventService.findEventConditionByEventType(eventTypeId);
        return Result.success(list);
    }

    /**
     * ????????????????????????
     *
     * @param conditionId id
     * @return ??????????????????
     */
    @GetMapping("/findDeptTimeLimitByCondition")
    public Result findDeptTimeLimitByCondition(String conditionId) {
        List<DeptTimeLimitVO> list = eventService.findDeptTimeLimitByCondition(conditionId);
        return Result.success(list);
    }

    /**
     * ??????????????????
     *
     * @param conditionId id
     * @return ????????????
     */
    @GetMapping("/getEventCondition")
    public Result getEventCondition(String conditionId) {
        List<EventConditionVO> conditionValueByRegion = eventService.findConditionValueByRegion(conditionId);
        return Result.success(conditionValueByRegion);
    }

    /**
     * ??????????????????
     *
     * @param deptTimeLimitId id
     * @return ????????????
     */
    @GetMapping("/findDeptTimeLimit")
    public Result findDeptTimeLimit(String deptTimeLimitId) {
        DeptTimeLimitVO vo = eventService.findDeptTimeLimit(deptTimeLimitId);
        return Result.success(vo);
    }

    /**
     * ????????????????????????
     *
     * @param parentId ??????
     */
    @GetMapping("/findAllByParentId")
    public Result findAllByParentId(String parentId) {
        List<GridVO> list = gridService.findAllByParentId(parentId);
        return Result.success(list);
    }

    /**
     * ??????????????????
     *
     * @return ??????
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
        List<KV> getEventSourceForWirelessAcquisition = kvService.findByTableNameAndFieldNameAndValue("event", "eventSource", "???????????????");
        return Result.success(getEventSourceForWirelessAcquisition);
    }

    @GetMapping("/getEventSourceForHotLine")
    public Result getEventSourceForHotLine() {
        List<KV> getEventSourceForWirelessAcquisition = kvService.findByTableNameAndFieldNameAndValue("event", "eventSource", "????????????");
        return Result.success(getEventSourceForWirelessAcquisition);
    }

    /**
     * ?????????????????? - ??????
     *
     * @param eventAppDTO ??????
     */
    @PostMapping("/delete/saveTemp")
    public Result saveTemp(@Valid EventAppDTO eventAppDTO) {
        EventDTO eventDTO = new EventDTO();
        BeanUtils.copyProperties(eventAppDTO, eventDTO);
        eventDTO.setSts(EventConstant.SUPERVISE_SAVE);
        eventService.saveTempForApi(eventDTO);
        return Result.success("????????????");
    }

    /**
     * ??????????????????
     *
     * @param eventDTO ??????
     */
    @PostMapping("/saveTemp")
    public Result saveTemp(EventDTO eventDTO) {
        eventDTO.setSts(EventConstant.SUPERVISE_SAVE);
        eventService.uploadFiles(eventDTO);
        eventService.saveTemp(eventDTO);
        return Result.success("????????????");
    }

    /**
     * ???????????????????????????
     *
     * @param ids ????????????
     */
    @PostMapping("/reportOnList")
    public Result reportOnList(String ids) {
        eventService.reportOnList(ids);
        return Result.success("????????????");
    }

    /**
     * ????????????
     *
     * @param eventDTO ????????????
     * @param pageable ??????
     * @return list
     */
    @GetMapping("/caseVerifyList")
    public Page<EventVO> caseVerifyList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Collections.singletonList(EventConstant.ACCEPTANCE_CASE_VERIFICATION));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * ????????????
     *
     * @param eventId ??????id
     * @return ??????
     */
    @GetMapping("/eventFindOne")
    public Result findOne(String eventId) {
        EventOneVO oneToVo = eventService.findOneToVo(eventId);
        return Result.success(oneToVo);
    }

    /**
     * ??????????????????????????????
     *
     * @param eventId ??????id
     * @return button
     */
    @GetMapping("/getButton/{eventId}")
    public Result getButton(@PathVariable String eventId) {
        List<EventButtonVO> button = eventService.getButton(eventId);
        return Result.success(button);
    }

    /**
     * ?????????????????????
     *
     * @param statisticsDTO ??????
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
        return Result.success("??????");
    }

    /**
     * ????????????
     *
     * @param eventDTO ????????????
     * @param pageable ??????
     * @return list
     */
    @GetMapping("/caseInspectList")
    public Page<EventVO> caseInspectList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setTaskName(Collections.singletonList(EventConstant.ACCEPTANCE_CASE_INSPECT));
        return eventService.search(eventDTO, pageable);
    }

    /**
     * ?????????????????????
     *
     * @param statisticsDTO ??????
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
        return Result.success("??????");
    }

    /**
     * ????????????
     *
     * @param eventDTO ????????????
     * @param pageable ??????
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
     * ????????????
     *
     * @param eventDTO ????????????
     * @param pageable ??????
     * @return list
     */
    @GetMapping("/caseHistoryList")
    public Page<EventVO> caseHistoryList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        return eventService.search(eventDTO, pageable);
    }

    /**
     * ????????????-??????
     *
     * @return ??????
     */
    @PostMapping("/saveTrackLog")
    public Result saveTrackLog(TrajectoryDTO trajectoryDTO) {
        SecurityUserBean user = SecurityUtil.getUser();
        RestReturn body = RestTemplateUtil.post(gisServiceProperties.getUrl() + "/portTransformation",getPointJson(trajectoryDTO.getX(),trajectoryDTO.getY()), RestReturn.class).getBody();
        assert body != null;
        Map<String,Object> map = (Map<String, Object>) body.getData();
        trajectoryDTO.setX((Double) map.get("longitude"));
        trajectoryDTO.setY((Double) map.get("latitude"));
        trajectoryService.saveTrajectory(trajectoryDTO, user.castToUser());
        return Result.success("??????");
    }

    private JSONObject getPointJson(Double x, Double y){
        Map<String, Object> point = new HashMap<>(3);
        point.put("longitude",x);
        point.put("latitude",y);
        point.put("epsg","4326");
        Map<String, Object> pointApiParam = new HashMap<>(2);
        pointApiParam.put("point",point);
        pointApiParam.put("toEPSG","4552");
        return new JSONObject(pointApiParam);
    }

    /**
     * ????????????-??????
     *
     * @return ??????
     */
    @GetMapping("/getTrackLogForOne")
    public Result getTrackLogForOne(String id, String startTime, String endTime) {
        if (StringUtils.isBlank(id)) {
            return Result.fail(500, "?????????????????????");
        }
        List<TrajectoryVO> trajectoryForOne = trajectoryService.getTrajectoryForOne(id, startTime, endTime);
        return Result.success(trajectoryForOne);
    }

    /**
     * ??????????????? - ?????????
     */
    @GetMapping("localReverseGeocoding")
    public Map<String, String> localReverseGeocoding(double x, double y) throws DataAccessException {
        RestReturn body = RestTemplateUtil.get(gisServiceProperties.getUrl() + "/restApi/LocalReverseGeocoding?x=" + x + "&y=" + y, RestReturn.class).getBody();
        assert body != null;
        return (Map<String, String>) body.getData();
    }

    /**
     * ???????????? - ?????????
     */
    @GetMapping("localGeocoding")
    public Object localGeocoding(String address) throws DataAccessException {
        RestReturn body = RestTemplateUtil.get(gisServiceProperties.getUrl() + "/restApi/LocalGeocoding?address=" + address, RestReturn.class).getBody();
        assert body != null;
        return body.getData();
    }

    /**
     * ????????????????????????
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
     * ?????????????????????
     *
     * @param eventDTO ????????????
     * @param pageable ??????
     * @return page
     */
    @GetMapping("/wirelessAcquisitionList")
    public Page<EventVO> wirelessAcquisitionList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        eventDTO.setSts(EventConstant.SUPERVISE_SAVE);
        return eventService.search(eventDTO, pageable);
    }

    /**
     * ??????????????????
     *
     * @param eventDTO ????????????
     */
    @PostMapping("/updateTemp")
    public Result updateTemp(EventDTO eventDTO) {
        eventService.uploadFiles(eventDTO);
        eventService.updateTemp(eventDTO);
        return Result.success("????????????");
    }

    /**
     * ????????????????????????
     *
     * @param ids id
     */
    @PostMapping("/remove")
    public Result remove(String ids) {
        eventService.remove(ids);
        return Result.success("????????????");
    }

    /**
     * ??????????????????
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
     * ???????????????
     *
     * @return ??????
     */
    @GetMapping("/getNodeName")
    public Result getNodeName(String eventId) {
        int nodeName = processService.findAllByNodeNameAndParentIsNotNull(eventId);
        return Result.success(nodeName);
    }

    /**
     * ?????????
     *
     * @return list
     */
    @GetMapping("/getIdioms")
    public Result getIdiomsList() {
        List<String> list = idiomsService.findAllIdiomsValue();
        return Result.success(list);
    }


    /**
     * ?????????????????????
     *
     * @param eventId id
     * @return ??????
     */
    @PostMapping("/urgent")
    public Result changeUrgentUrl(String eventId) {
        eventService.changeUrgent(eventId);
        return Result.success("??????");
    }

    @GetMapping("/findUrgent")
    public Result findUrgent(String eventId) {
        int type = eventService.findUrgent(eventId);
        return Result.success(type);
    }

}
