package com.unicom.urban.management.web.project.categoryconfiguration;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.EventConditionDTO;
import com.unicom.urban.management.pojo.dto.EventTypeDTO;
import com.unicom.urban.management.pojo.vo.EventConditionVO;
import com.unicom.urban.management.pojo.vo.EventTypeVO;
import com.unicom.urban.management.service.eventcondition.EventConditionService;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * 类别配置
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/categoryConfiguration")
public class CategoryConfigurationController {

    @Autowired
    private EventTypeService eventTypeService;
    @Autowired
    private EventConditionService eventConditionService;

    @GetMapping("/toList")
    public ModelAndView toList() {
        return new ModelAndView(SystemConstant.PAGE + "/categoryConfiguration/list");
    }

    @GetMapping("/toInsert")
    public ModelAndView toInsert(String id) {
        ModelAndView modelAndView = new ModelAndView(SystemConstant.PAGE + "/categoryConfiguration/insert");
        modelAndView.addObject("eventTypeId", id);
        return modelAndView;
    }

    @PostMapping("/saveEventType")
    public Result saveEventType(@Valid EventTypeDTO eventTypeDTO) {
        eventTypeService.saveEventType(eventTypeDTO);
        return Result.success("新增成功");
    }

    @PostMapping("/updateEventType")
    public Result updateEventType(@Valid EventTypeDTO eventTypeDTO) {
        eventTypeService.updateEventType(eventTypeDTO);
        return Result.success("修改成功");
    }

    @PostMapping("/deleteEventType")
    public Result deleteEventType(String id) {
        eventTypeService.deleteEventType(id);
        return Result.success("删除成功");
    }

    @GetMapping("/getEventTypeTree")
    public Result getEventTypeTree() {
        List<EventTypeVO> eventTypeTree = eventTypeService.getEventTypeTree();
        return Result.success(eventTypeTree);
    }

    @GetMapping("/eventConditionParentList")
    public Page<EventConditionVO> eventConditionParentList(EventConditionDTO eventConditionDTO, @PageableDefault Pageable pageable) {
        return eventConditionService.search(eventConditionDTO, pageable);
    }

}
