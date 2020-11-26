package com.unicom.urban.management.web.project.bigscreen;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.service.bigscreen.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author 顾志杰
 * @date 2020/11/25-14:43
 */
@RequestMapping("/bigscreen")
@RestController
@ResponseResultBody
public class IndexRestController {

    @Autowired
    private IndexService indexService;

    @GetMapping("/count")
    public Map<String,Object> count(String timeType){
        return indexService.count(timeType);
    }
}
