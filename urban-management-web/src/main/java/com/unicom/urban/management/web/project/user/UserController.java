package com.unicom.urban.management.web.project.user;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.entity.ChangePasswordDTO;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

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
        User user = userService.findById(id);

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


    @PostMapping("/changePassword")
    public void changePassword(@Valid ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO);
    }


}
