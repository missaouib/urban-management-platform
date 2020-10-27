package com.unicom.urban.management.web.project.tree;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.vo.TreeVO;
import com.unicom.urban.management.service.eventtype.EventTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author liukai
 */
@RestController
@ResponseResultBody
@RequestMapping("/tree")
public class TreeController {

    @Autowired
    private EventTypeService eventTypeService;


    @GetMapping("/eventtype")
    public ModelAndView add(Model model) {
        List<TreeVO> tree = eventTypeService.searchTree();
        model.addAttribute("tree", tree);
        return new ModelAndView(SystemConstant.PAGE + "/tree/eventtype");
    }


}
