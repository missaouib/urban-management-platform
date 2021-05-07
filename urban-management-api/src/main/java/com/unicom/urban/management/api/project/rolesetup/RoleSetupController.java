package com.unicom.urban.management.api.project.rolesetup;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 角色设置
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/api/roleSetup")
public class RoleSetupController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;

    @GetMapping("/getDeptAndRoleForTree")
    public Result getDeptAndRoleForTree() {
        List<DeptVO> allAndRoleForTree = deptService.getDeptAndRoleForTree();
        return Result.success(allAndRoleForTree);
    }
}
