package com.unicom.urban.management.web.project.time;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.TimePlanDTO;
import com.unicom.urban.management.pojo.vo.TimeVO;
import com.unicom.urban.management.service.time.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 计时管理
 *
 * @author liukai
 */
@RestController
@RequestMapping("/time")
@ResponseResultBody
public class TimeController {

    @Autowired
    private TimeService timeService;


    @GetMapping
    public ModelAndView time() {
        return new ModelAndView(SystemConstant.PAGE + "/time/time");
    }

    @GetMapping("/search")
    public Page<TimeVO> search(@PageableDefault Pageable pageable) {
        return timeService.search(pageable);
    }

    @GetMapping("/add")
    public ModelAndView add() {
        return new ModelAndView(SystemConstant.PAGE + "/time/time");
    }

    @PostMapping("/save")
    public void save(TimePlanDTO timePlanDTO) {
        timeService.save(timePlanDTO);
    }

    @GetMapping("/addday")
    public ModelAndView addDay() {
        return new ModelAndView(SystemConstant.PAGE + "/time/timeAdd");
    }

}
