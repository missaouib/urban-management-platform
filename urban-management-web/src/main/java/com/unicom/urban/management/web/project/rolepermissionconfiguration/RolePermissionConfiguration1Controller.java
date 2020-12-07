package com.unicom.urban.management.web.project.rolepermissionconfiguration;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.service.menu.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * 角色权限配置
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/rolePermissionConfiguration")
public class RolePermissionConfiguration1Controller {

    @Autowired
    private MenuService menuService;

    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView(SystemConstant.PAGE + "/rolePermissionConfiguration/index");
    }

}
