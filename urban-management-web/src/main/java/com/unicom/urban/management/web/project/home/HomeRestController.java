package com.unicom.urban.management.web.project.home;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.service.home.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author 顾志杰
 * @date 2020/11/25-9:17
 */
@RestController
@ResponseResultBody
@RequestMapping("/home")
public class HomeRestController {


    @Autowired
    private HomeService homeService;


    @GetMapping("/unitCount")
    public List<Map<String,Object>> unitCount(){
        return homeService.eventTypeCount();
    }
}
