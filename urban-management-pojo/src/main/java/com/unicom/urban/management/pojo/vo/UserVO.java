package com.unicom.urban.management.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserVO {

    private String id;

    private String name;

    private String username;

    private String phone;

    private String deptName;

    private String deptId;

    private String roles;

    private List<String> roleIdList;

    private List<String> roleNameList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String system;

    private Integer sts;

    private String sex;

    private String birth;

    private Integer checkbox;

    private String officePhone;

    private String email;

    private String post;

    private Integer sort;
}
