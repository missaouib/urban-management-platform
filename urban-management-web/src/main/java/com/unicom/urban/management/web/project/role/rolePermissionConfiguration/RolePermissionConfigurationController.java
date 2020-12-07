package com.unicom.urban.management.web.project.role.rolePermissionConfiguration;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.service.role.RolePermissionConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色权限配置
 * @author liubozhi
 */
@RestController
@ResponseResultBody
@RequestMapping("/rolePerConfig")
public class RolePermissionConfigurationController {
    @Autowired
    RolePermissionConfigurationService rolePermissionConfigurationService;
    public Result rolePerIndex(){
            
        return Result.success();
    }
}
