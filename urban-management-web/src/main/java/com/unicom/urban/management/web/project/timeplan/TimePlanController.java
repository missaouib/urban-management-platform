package com.unicom.urban.management.web.project.timeplan;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.dto.TimePlanDTO;
import com.unicom.urban.management.service.timeplan.TimePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@ResponseResultBody
@RequestMapping("/timeplan")
public class TimePlanController {

    @Autowired
    private TimePlanService timePlanService;


    @PostMapping("/save")
    public void save(TimePlanDTO timePlanDTO) {
        timePlanService.save(timePlanDTO);
    }

}
