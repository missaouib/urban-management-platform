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
    @Password(message = "密码必须是包含大写字母、小写字母、数字、特殊符号（不是字母，数字，下划线，汉字的字符）的8到16位的组合!")
    private String newPassword;

    /**
     * 确认密码 判断与刚刚输入的新密码是否一致
     */
    private String configNewPassword;


}
