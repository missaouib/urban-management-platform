package com.unicom.urban.management.pojo.entity;

import com.unicom.urban.management.pojo.annotations.validation.Password;
import lombok.Data;

/**
 * @author liukai
 */
@Data
public class ChangePasswordDTO {

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    @Password
    private String newPassword;

    /**
     * 确认密码 判断与刚刚输入的新密码是否一致
     */
    private String configNewPassword;


}
