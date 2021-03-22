package com.unicom.urban.management.api.project.gis;

import com.unicom.urban.management.common.properties.GisServiceProperties;
import com.unicom.urban.management.common.util.RestTemplateUtil;
import com.unicom.urban.management.pojo.RestReturn;
import com.unicom.urban.management.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
