package com.unicom.urban.management.api.project.user;

import com.unicom.urban.management.common.annotations.ResponseResultBody;
import com.unicom.urban.management.pojo.Result;
import com.unicom.urban.management.pojo.dto.ChangePasswordDTO;
import com.unicom.urban.management.pojo.dto.UserDTO;
import com.unicom.urban.management.pojo.vo.AddressBookVO;
import com.unicom.urban.management.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 用户
 *
 * @author jiangwen
 */
@RestController
@ResponseResultBody
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 手机端 获取通讯录
     *
     * @return 通讯录
     */
    @GetMapping("/getAddressBook")
    public Result getAddressBook() {
        List<AddressBookVO> addressBook = userService.getAddressBook();
        return Result.success(addressBook);
    }

    /**
     * 修改密码
     *
     * @param changePasswordDTO 旧密码 新密码 确认密码
     */
    @PostMapping("/changePassword")
    public Result changePassword(@Valid ChangePasswordDTO changePasswordDTO) {
        userService.changePassword(changePasswordDTO);
        return Result.success("修改成功");
    }

    /**
     * 修改用户信息
     *
     * @param userDTO 修改内容
     * @return 成功
     */
    @PostMapping("/updateUser")
    public Result updateUser(@Valid UserDTO userDTO) {
        userService.updateUserByDeptId(userDTO);
        return Result.success("修改成功");
    }

    /**
     * 重置密码
     *
     * @param id userId
     */
    @GetMapping("/initialization")
    public Result initialization(String id){
        userService.initialization(id);
        return Result.success("重置成功");
    }

}
