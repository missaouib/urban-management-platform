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
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 无线采集子系统
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/event")
public class WirelessAcquisitionController {

    @Autowired
    private EventService eventService;

    @GetMapping("/toWirelessAcquisitionList")
    public ModelAndView toWirelessAcquisitionList() {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/list");
    }

    @GetMapping("/toWirelessAcquisitionSave")
    public ModelAndView toWirelessAcquisitionListSave() {
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/save");
    }

    @GetMapping("/toWirelessAcquisitionUpdate/{id}")
    public ModelAndView toWirelessAcquisitionListUpdate(@PathVariable String id, Model model) {
        model.addAttribute("id", id);
        return new ModelAndView(SystemConstant.PAGE + "/event/wirelessAcquisition/update");
    }

    @GetMapping("/wirelessAcquisitionList")
    public Page<EventVO> wirelessAcquisitionList(EventDTO eventDTO, @PageableDefault Pageable pageable) {
        return eventService.search(eventDTO, pageable);
    }

}
