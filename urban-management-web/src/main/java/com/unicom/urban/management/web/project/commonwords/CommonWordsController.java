package com.unicom.urban.management.web.project.commonwords;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@ResponseResultBody
@RequestMapping("/commonWords")
public class CommonWordsController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/index")
    public ModelAndView role() {
        return new ModelAndView(SystemConstant.PAGE + "/commonWords/index");
    }

}
