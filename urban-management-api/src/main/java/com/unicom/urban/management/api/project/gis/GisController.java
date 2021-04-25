package com.unicom.urban.management.api.project.gis;

import cn.hutool.json.JSONObject;
import com.unicom.urban.management.common.properties.GisServiceProperties;
import com.unicom.urban.management.common.util.RestTemplateUtil;
import com.unicom.urban.management.pojo.RestReturn;
import com.unicom.urban.management.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 顾志杰
 * @date 2021/3/22-14:28
 */
@RestController
@RequestMapping("/api/gis")
public class GisController {

    @Autowired
    private GisServiceProperties gisServiceProperties;




    @GetMapping("distance")
    public Result distance(String geometryBase, String geometry){
        RestReturn body = RestTemplateUtil.get(gisServiceProperties.getUrl() + "/distance?geometryBase=" + geometryBase + "&geometry=" + geometry, RestReturn.class).getBody();
        assert body != null;
        return Result.success(body.getData()) ;
    }



    @GetMapping("portTransformation")
    public Result portTransformation(String x, String y){
        RestReturn body = RestTemplateUtil.post(gisServiceProperties.getUrl() + "/portTransformation",getPointJson(x,y), RestReturn.class).getBody();
        assert body != null;
        return Result.success(body.getData()) ;
    }






    private JSONObject getPointJson(String x,String y){
        Map<String, Object> point = new HashMap<>(3);
        point.put("longitude",x);
        point.put("latitude",y);
        point.put("epsg","4326");
        Map<String, Object> pointApiParam = new HashMap<>(2);
        pointApiParam.put("point",point);
        pointApiParam.put("toEPSG","4552");
        return new JSONObject(pointApiParam);
    }
}
