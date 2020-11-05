package com.unicom.urban.management.web.project.index;

import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.service.menu.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liukai
 */
@Controller
public class IndexController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/index")
    public String index() {
        return SystemConstant.PAGE + "/index";
    }

}
