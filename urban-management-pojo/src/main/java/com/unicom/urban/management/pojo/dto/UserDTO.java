package com.unicom.urban.management.pojo.dto;

import com.unicom.urban.management.pojo.annotations.validation.MobileNumber;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class UserDTO {

    private String id;

    @NotBlank(message = "姓名不能为空")
    private String name;

    private String username;

    @MobileNumber
    private String mobileNumber;

    private String deptId;

    private String sex;

    private String birth;

    private List<String> roleList;

    private String roleId;

    private Integer sts;

}
