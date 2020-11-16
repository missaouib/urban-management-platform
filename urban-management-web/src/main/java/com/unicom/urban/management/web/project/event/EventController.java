package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.properties.GisServiceProperties;
import com.unicom.urban.management.common.util.RestTemplateUtil;
import com.unicom.urban.management.pojo.RestReturn;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.vo.EventButtonVO;
import com.unicom.urban.management.service.event.EventService;
import com.unicom.urban.management.service.publish.PublishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Map<String, String> localReverseGeocoding(double x, double y) {

        try {
            RestReturn body = RestTemplateUtil.get(gisServiceProperties.getUrl() + "/restApi/LocalReverseGeocoding?x=" + x + "&y=" + y, RestReturn.class).getBody();
            assert body != null;
            return (Map<String, String>) body.getData();
        } catch (Exception e) {
            e.printStackTrace();
            Map<String,String> map= new HashMap<>();
            map.put("mongo","");
            return map;
        }
    }

    /**
     * 点击网格反差信息
     *
     * @param mongodbId mongoId
     */
    @GetMapping("/getGridByCheckLayer")
    public void getGridByCheckLayer(String mongodbId) {
        /*List<Map<String, Object>> valueMapList = new ArrayList<>(1);
        Map<String, Object> valueMap = new HashMap<>(2);
        valueMap.put("value", mongodbId);
        valueMap.put("queryCriteria", "regex");
        valueMapList.add(valueMap);
        List<Map<String, Object>> columnNameMapList = new ArrayList<>(1);
        Map<String, Object> columnNameMap = new HashMap<>(2);
        columnNameMap.put("columnName", "objId");
        columnNameMap.put("value", valueMapList);
        columnNameMapList.add(columnNameMap);
        Map<String, Object> map = new HashMap<>(2);
        map.put("layerId", KvConstant.KV_LAYER_GRID);
        map.put("columnName", columnNameMapList);*/
        RestReturn body = RestTemplateUtil.get(gisServiceProperties.getUrl() + "/queryMongoById?id=" + mongodbId, RestReturn.class).getBody();
        Map<String, String> dataMap = (Map<String, String>) body.getData();
        System.out.println(dataMap);
    }

}
