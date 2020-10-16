package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.annotations.validation.MobileNumber;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserDTO {

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "账号不能为空")
    private String username;

    @MobileNumber
    private String mobileNumber;

    private String deptId;

}
