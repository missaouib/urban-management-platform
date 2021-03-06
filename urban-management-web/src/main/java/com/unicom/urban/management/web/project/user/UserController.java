package com.unicom.urban.management.web.project.user;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.util.AESUtil;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.ChangePasswordDTO;
import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.dto.UserIdListDTO;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户管理controller
 *
 * @author liukai
 */
@RestController
@ResponseResultBody
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ModelAndView user() {
        return new ModelAndView(SystemConstant.PAGE + "/user/user");
    }

    @GetMapping("/add")
    public ModelAndView add() {
        return new ModelAndView(SystemConstant.PAGE + "/user/add");
    }


    @GetMapping("/search")
    public Page<UserVO> search(UserDTO userDTO, @PageableDefault Pageable pageable) {
        return userService.search(userDTO, pageable);
    }


    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable String id, Model model) {
        UserVO user = userService.findById(id);
        user.setPhone(StringUtils.isNotBlank(user.getPhone()) ? AESUtil.decrypt(user.getPhone()) : "");
        model.addAttribute("user", user);

        return new ModelAndView(SystemConstant.PAGE + "/user/edit");
    }

    @PostMapping("/update")
    public void updateUser(User user) {
        userService.updateUser(user);
    }

    @PostMapping("/save")
    public void saveUser(@Valid UserDTO userDTO) {
        userService.saveUser(userDTO);
    }

    @PostMapping("/remove")
    public void deleteUser(String ids) {
        userService.removeUser(ids);
    }

    @PostMapping("/existsByUsername")
    public void existsByUsername(String username) {
        userService.usernameAlreadyExists(username);
    }


    /**
     * 修改个人密码
     */
    @PostMapping("/changePassword")
    public void changePassword(@Valid ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO);
    }

    @GetMapping("/profile")
    public ModelAndView profile() {
        return new ModelAndView(SystemConstant.PAGE + "/user/profile/profile");
    }

    @GetMapping("/activation")
    public void activation(String id) {
        userService.activation(id);
    }

    @GetMapping("/initialization")
    public void initialization(String id) {
        userService.initialization(id);
    }

    @PostMapping("/saveBatch")
    public Result saveBatch(@Valid UserIdListDTO userIdListDTO) {
        userService.saveBatchUser(userIdListDTO);
        return Result.success();
    }

    @GetMapping("/findOne")
    public Result findOne(String userId) {
        UserVO userVO = userService.findById(userId);
        return Result.success(userVO);
    }

    @GetMapping("/findUserByDept")
    public Result findUserByDept(String deptId) {
        List<UserVO> list = userService.findUserByDept(deptId);
        return Result.success(list);
    }
}
