package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.annotations.validation.MobileNumber;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UserDTO {

    private String id;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotBlank(message = "帐号不能为空")
    private String username;

    @MobileNumber
    private String mobileNumber;

    private String officePhone;

    private String deptId;

    private String sex;

    private String birth;

    private List<String> roleList;

    @Email
    private String email;

    private String roleId;

    private Integer sts;

    private String post;

    private Integer sort;

}
