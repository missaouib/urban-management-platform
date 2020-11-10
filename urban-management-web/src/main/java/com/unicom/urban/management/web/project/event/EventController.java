package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.KvConstant;
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
    public Map<String, String> localReverseGeocoding(double x, double y){
        RestReturn body = RestTemplateUtil.get(KvConstant.GIS_URL + "/restApi/LocalReverseGeocoding?x=" + x +"&y="+y, RestReturn.class).getBody();
        assert body != null;
        return (Map<String, String>) body.getData();
    }

}
