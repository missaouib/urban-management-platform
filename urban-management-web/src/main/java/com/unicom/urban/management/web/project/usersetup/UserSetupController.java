package com.unicom.urban.management.web.project.usersetup;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.exception.DataValidException;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.vo.DeptVO;
import com.unicom.urban.management.service.dept.DeptService;
import com.unicom.urban.management.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * 人员设置
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/userSetup")
public class UserSetupController {

    @Autowired
    private DeptService deptService;
    @Autowired
    private UserService userService;

    @GetMapping("/index")
    public ModelAndView index() {
        return new ModelAndView(SystemConstant.PAGE + "/userSetup/index");
    }

    @GetMapping("/getAllAndUserForTree")
    public Result getAllAndUserForTree() {
        List<DeptVO> allAndRoleForTree = deptService.getAllAndUserForTree();
        return Result.success(allAndRoleForTree);
    }

    @PostMapping("/saveUser")
    public Result saveRole(@Valid UserDTO userDTO) {
        userService.saveUserByDeptId(userDTO);
        return Result.success("新增成功");
    }

    @PostMapping("/updateUser")
    public Result updateUser(@Valid UserDTO userDTO) {
        userService.updateUserByDeptId(userDTO);
        return Result.success("修改成功");
    }

    @PostMapping("/deleteUser")
    public Result deleteUser(String id) {
        userService.deleteUserByDeptId(id);
        return Result.success("删除成功");
    }

    @GetMapping("/getUserSortByDeptId")
    public Result getUserSortByDeptId(String deptId) {
        if (StringUtils.isBlank(deptId)) {
            throw new DataValidException("请选择正确的部门");
        }
        Integer userSortByDeptId = deptService.getUserSortByDeptId(deptId);
        return Result.success(userSortByDeptId);
    }

}
