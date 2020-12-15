package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventConditionDTO;
import com.unicom.urban.management.pojo.entity.EventCondition;
import com.unicom.urban.management.pojo.vo.EventConditionVO;
import com.unicom.urban.management.service.eventcondition.EventConditionService;
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
 * 区域维护Controller
 * @author liubozhi
 */
@RestController
@RequestMapping("/eventCondition")
@ResponseResultBody
public class EventConditionController {
    @Autowired
    private EventConditionService eventConditionService;

    @GetMapping("/toEventConditionList")
    public ModelAndView toEventConditionList() {

        return new ModelAndView(SystemConstant.PAGE + "/eventCondition/list");
    }
    @GetMapping("/toAdd")
    public ModelAndView toAdd() {
        return new ModelAndView(SystemConstant.PAGE + "/eventCondition/add");
    }
    @GetMapping("/toUpdate")
    public ModelAndView toUpdate() {
        return new ModelAndView(SystemConstant.PAGE + "/eventCondition/update");
    }
    @GetMapping("/search")
    public Page<EventConditionVO> search(EventConditionVO eventConditionVO, @PageableDefault Pageable pageable) {
        return eventConditionService.search(eventConditionVO,pageable);
    }

    /**
     * 新增
     * @param eventConditionDTO
     * @return
     */
    @PostMapping("/save")
    public Result save(EventConditionDTO eventConditionDTO){
        return Result.success();
    }

    /**
     * 修改
     *
     * @param eventConditionDTO
     * @return
     */
    @PostMapping("/update")
    public Result update(EventConditionDTO eventConditionDTO) {
        eventConditionService.update(eventConditionDTO);
        return Result.success();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @PostMapping("del")
    public Result del(String id) {
        eventConditionService.del(id);
        return Result.success();
    }
}
