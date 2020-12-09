package com.unicom.urban.management.web.project.rolePermissionConfiguration;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.MenuDTO;
import com.unicom.urban.management.pojo.dto.RoleDTO;
import com.unicom.urban.management.pojo.dto.RoleMenuDTO;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.pojo.vo.MenuVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.menu.MenuService;
import com.unicom.urban.management.service.role.RolePermissionConfigurationService;
import com.unicom.urban.management.service.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色权限配置
 * @author liubozhi
 */
@RestController
@ResponseResultBody
@RequestMapping("/rolePermissionConfiguration")
public class RolePermissionConfigurationController {
    @Autowired
    private MenuService menuService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;
    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView(SystemConstant.PAGE + "/rolePermissionConfiguration/index");
    }

    /**
     * 获取部门和角色树
     * @return
     */
    @GetMapping("/getDeptAndRoleTree")
    public Result getDeptAndRoleTree() {
        List<DeptVO> allAndRoleForTree = deptService.getAllAndRoleForTree();
        return Result.success(allAndRoleForTree);
    }
    /**
     * 获取菜单类型
     * @return
     */
    @GetMapping("/getMenuByMenuType")
    public Result getMenuByMenuType(String menuTypeId){
        List<MenuVO> menuVOList = menuService.getMenuByMenuType(menuTypeId);
        return Result.success(menuVOList);
    }
    /**
     * 保存角色菜单关系
     * @return
     */
    @PostMapping("save")
    public Result saveRoleAndMenu(String id,String[] menuIdList){
        RoleMenuDTO roleMenuDTO = new RoleMenuDTO();
        roleMenuDTO.setId(id);
        roleMenuDTO.setMenuIdList(menuIdList);
        roleService.saveRoleAndMenu(roleMenuDTO);
        return Result.success();
    }
}
