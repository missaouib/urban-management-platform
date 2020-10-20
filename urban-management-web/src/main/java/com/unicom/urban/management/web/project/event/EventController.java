package com.unicom.urban.management.web.project.event;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.EventDTO;
import com.unicom.urban.management.pojo.vo.EventVO;
import com.unicom.urban.management.service.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 案件
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/toEventList")
    public ModelAndView toEventList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/list");
    }

    @GetMapping("/toEventSave")
    public ModelAndView toEventSave() {
        return new ModelAndView(SystemConstant.PAGE + "/event/save");
    }

    @GetMapping("/toEventUpdate")
    public ModelAndView toEventUpdate() {
        return new ModelAndView(SystemConstant.PAGE + "/event/update");
    }

    @GetMapping("/eventList")
    public Page<EventVO> eventList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        return eventService.search(eventDTO, pageable);
    }

}
