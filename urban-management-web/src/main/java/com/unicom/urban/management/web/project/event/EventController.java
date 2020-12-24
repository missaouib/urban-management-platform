package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.KvConstant;
import com.unicom.urban.management.common.properties.GisServiceProperties;
import com.unicom.urban.management.common.util.RestTemplateUtil;
import com.unicom.urban.management.common.util.SecurityUtil;
import com.unicom.urban.management.pojo.RestReturn;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.entity.Grid;
import com.unicom.urban.management.pojo.vo.EventButtonVO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.grid.GridService;
import com.unicom.urban.management.service.process.ProcessService;
import com.unicom.urban.management.service.publish.PublishService;
import com.unicom.urban.management.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 协同工作子系统
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private PublishService publishService;
    @Autowired
    private GisServiceProperties gisServiceProperties;
    @Autowired
    private GridService gridService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProcessService processService;

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
     * 获取已发布网格的地址
     *
     * @return 地址
     */
    @GetMapping("/getGridUrl")
    public Result getGridUrl() {
        String gridUrl = publishService.getGridUrl();
        return Result.success(gridUrl);
    }

    /**
     * 逆地理编码
     */
    @GetMapping("localReverseGeocoding")
    public Map<String, String> localReverseGeocoding(double x, double y) throws DataAccessException {
        RestReturn body = RestTemplateUtil.get(gisServiceProperties.getUrl() + "/restApi/LocalReverseGeocoding?x=" + x + "&y=" + y, RestReturn.class).getBody();
        assert body != null;
        return (Map<String, String>) body.getData();
    }

    /**
     * 点击网格反差信息
     *
     * @param mongodbId mongoId
     */
    @GetMapping("/getGridByCheckLayer")
    public Result getGridByCheckLayer(String mongodbId) {
        RestReturn body = RestTemplateUtil.get(gisServiceProperties.getUrl() + "/queryMongoById?id=" + mongodbId, RestReturn.class).getBody();
        Map<String, String> dataMap = (Map<String, String>) body.getData();
        String objId = dataMap.get("objId");
        Grid byGridCode = gridService.findByGridCode(objId);
        List<Map<String, Object>> mapList = new ArrayList<>(4);
        Map<String, Object> map = new HashMap<>(3);
        map.put("id", byGridCode.getId());
        map.put("name", byGridCode.getGridName());
        map.put("level", byGridCode.getLevel());
        map.put("user", SecurityUtil.getUsername());
        mapList.add(map);
        gridService.addMapToList(byGridCode, mapList);
        return Result.success(mapList);
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
     * 获取有监督员角色的人
     *
     * @return 人
     */
    @GetMapping("/getNodeName")
    public Result getNodeName(String eventId) {
        int nodeName = processService.findAllByNodeNameAndParentIsNotNull(eventId);
        return Result.success(nodeName);
    }

    /**
     * 获取有监督员角色的人
     *
     * @return 人
     */
    @GetMapping("/getGridPolygon")
    public Result getGridPolygon(String eventId) {
        String gridPolygon = gridService.getGridPolygon(eventId);
        return Result.success(gridPolygon);
    }

    @GetMapping("/similarCasesForEvent")
    public Result similarCasesForEvent(double x, double y) {
        List<EventVO> eventVOList = eventService.similarCasesForEvent(x, y);
        return Result.success(eventVOList);
    }

    /**
     * 设置转应急状态
     * @param eventId
     * @return
     */
    @PostMapping("/urgent")
    public Result changeUrgentUrl(String eventId){
        eventService.changeUrgent(eventId);
        return Result.success();
    }
    @GetMapping("/findUrgent")
    public Result findUrgent(String eventId){
        int type = eventService.findUrgent(eventId);
        return Result.success(type);
    }
}
