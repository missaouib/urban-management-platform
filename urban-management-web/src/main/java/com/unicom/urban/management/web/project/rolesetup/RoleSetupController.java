package com.unicom.urban.management.web.project.rolesetup;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.RoleDTO;
import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.dto.UserIdListDTO;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.role.RoleService;
import com.unicom.urban.management.service.user.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 角色设置
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/roleSetup")
public class RoleSetupController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private DeptService deptService;


    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView(SystemConstant.PAGE + "/roleSetup/index");
    }

    @GetMapping("/add")
    public ModelAndView add() {

        return new ModelAndView(SystemConstant.PAGE + "/roleSetup/add");
    }

    @PostMapping("/saveRole")
    public Result saveRole(@Valid RoleDTO roleDTO) {
        roleService.saveRoleByDeptId(roleDTO);
        return Result.success("新增成功");
    }

    @GetMapping("/getAllAndRoleForTree")
    public Result getAllAndRoleForTree() {
        List<DeptVO> allAndRoleForTree = deptService.getAllAndRoleForTree();
        return Result.success(allAndRoleForTree);
    }
    @GetMapping("/getDeptAndRoleForTree")
    public Result getDeptAndRoleForTree() {
        List<DeptVO> allAndRoleForTree = deptService.getDeptAndRoleForTree();
        return Result.success(allAndRoleForTree);
    }

    @PostMapping("/updateRole")
    public Result updateRoleUrl(@Valid RoleDTO roleDTO) {
        roleService.updateRoleByDeptId(roleDTO);
        return Result.success("修改成功");
    }

    @PostMapping("/deleteRole")
    public Result deleteRoleUrl(@Valid String id) {
        roleService.deleteRoleById(id);
        return Result.success("删除成功");
    }
    @GetMapping("/findUserByRole")
    public Result findUserByRole(String roleId){
        List<UserVO> list = roleService.findUserByRole(roleId);


        return Result.success(list);
    }
    /**
     * 保存用户角色关系
     * @param list
     * @return
     */
    @PostMapping("/saveUserAndRole")
    public Result saveUserByRole(@Valid UserIdListDTO userIdListDTO){
        List<Map<String,Object>> mapList = userIdListDTO.getUserIdList();
        roleService.saveUserByRole(userIdListDTO.getRoleId(),mapList);
        return Result.success();
    }
}
