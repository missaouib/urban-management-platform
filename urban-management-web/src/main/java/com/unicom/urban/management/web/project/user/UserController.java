package com.unicom.urban.management.web.project.user;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.entity.User;
import com.unicom.urban.management.pojo.vo.UserVO;
import com.unicom.urban.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@ResponseResultBody
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public ModelAndView user() {
        return new ModelAndView(SystemConstant.PAGE + "/user/user");
    }

    @GetMapping("/user/add")
    public ModelAndView add() {
        return new ModelAndView(SystemConstant.PAGE + "/user/add");
    }


    @GetMapping("/user/search")
    public Page<UserVO> search(UserDTO userDTO, @PageableDefault Pageable pageable) {
        return userService.search(userDTO, pageable);
    }


    @GetMapping("/user/edit/{id}")
    public ModelAndView edit(@PathVariable String id, Model model) {
        User user = userService.findById(id);

        model.addAttribute("user", user);

        return new ModelAndView(SystemConstant.PAGE + "/user/edit");
    }

    @PostMapping("/user/update")
    public void updateUser(User user) {
        userService.updateUser(user);
    }

    @PostMapping("/user/save")
    public void saveUser(@Valid UserDTO userDTO) {
        userService.saveUser(userDTO);
    }

    @PostMapping("/user/remove")
    public void deleteUser(String ids) {
        userService.removeUser(ids);
    }

    @PostMapping("/user/existsByUsername")
    public void existsByUsername(String username) {
        userService.usernameAlreadyExists(username);
    }


}
