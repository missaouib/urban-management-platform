package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.common.annotations.validation.MobileNumber;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {

    private String name;

    @NotBlank(message = "账号不能为空")
    private String username;

    @NotBlank(message = "手机号码不能为空")
    @MobileNumber(message = "手机号码格式不正确")
    private String mobileNumber;

}
