package com.unicom.urban.management.web.project.time;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.DayDTO;
import com.unicom.urban.management.pojo.dto.TimePlanDTO;
import com.unicom.urban.management.pojo.entity.time.TimePlan;
import com.unicom.urban.management.pojo.vo.DayVo;
import com.unicom.urban.management.pojo.vo.TimeSchemeVO;
import com.unicom.urban.management.pojo.vo.TimeVO;
import com.unicom.urban.management.service.time.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
        return new ModelAndView(SystemConstant.PAGE + "/time/add");
    }

    /**
     * 跳转到新增计时管理页面
     */
    @GetMapping("/addtime")
    public ModelAndView addTime() {
        return new ModelAndView(SystemConstant.PAGE + "/time/addtime");
    }


    /**
     * 新增计时管理
     */
    @PostMapping("/addtime")
    public void save(@Validated TimePlanDTO timePlanDTO) {
        timeService.save(timePlanDTO);
    }

    /**
     * 启用或禁用
     */
    @GetMapping("/activation")
    public void activation(String id, TimePlan.Status status) {
        timeService.activation(id, status);
    }

    @PostMapping("/addday")
    public void addDay(DayDTO dayDTO) {
        timeService.saveDay(dayDTO);
    }

    @PostMapping("/updateday")
    public void updateDay(DayDTO dayDTO) {
        timeService.updateDay(dayDTO);
    }

    /**
     * 保存设置时间
     *
     * @param id   计时方案id
     * @param time 时间 逗号分割
     */
    @PostMapping("/settime")
    public void setTime(@RequestParam(name = "id") String id, @RequestParam(name = "time") String time) {
        timeService.setTime(id, time);
    }

    /**
     * 跳转到设置时间页面
     */
    @GetMapping("/settime")
    public ModelAndView setTime(@RequestParam(name = "id") String id, Model model) {
        List<TimeSchemeVO> timeSchemeVOS = timeService.queryTimeScheme(id);
        model.addAttribute("list", timeSchemeVOS);
        model.addAttribute("id", id);
        return new ModelAndView(SystemConstant.PAGE + "/time/timeSet");
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable("id") String id, Model model) {
        model.addAttribute("id", id);
        return new ModelAndView(SystemConstant.PAGE + "/time/timeAdd");
    }


    @GetMapping("/day/search")
    public Page<DayVo> search(String id, @PageableDefault Pageable pageable) {
        return timeService.search(id, pageable);
    }

    @GetMapping("/addday")
    public ModelAndView addDay() {
        return new ModelAndView(SystemConstant.PAGE + "/time/timeAdd");
    }

}
