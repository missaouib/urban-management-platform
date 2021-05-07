package com.unicom.urban.management.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.unicom.urban.management.common.util.AESUtil;
import com.unicom.urban.management.pojo.SecurityDeptBean;
import com.unicom.urban.management.pojo.SecurityRoleBean;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.entity.User;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.swing.text.DateFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private Set<SecurityRoleBean> roleList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String system;

    private Integer sts;

    private String sex;

    private String birth;

    private String profilePhotoUrl;

    private Integer checkbox;

    private String officePhone;

    private String email;

    private String post;

    private Integer sort;

    private String iconSkin;


    public UserVO(User user) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.sts = user.getSts();
        this.roleList = user.getRoleList()!=null?(user.getRoleList().stream().map(SecurityRoleBean::new).collect(Collectors.toSet())):null;
        this.birth = user.getBirth()!=null?user.getBirth().format(fmt):null;
        this.email = user.getEmail();
        this.phone = StringUtils.isNotBlank(user.getPhone())? AESUtil.decrypt(user.getPhone()):"";
        this.profilePhotoUrl = user.getProfilePhotoUrl();
        this.sex = user.getSex();
        this.deptId = user.getDept().getId();
        this.deptName = user.getDept().getDeptName();
    }

    public UserVO() {

    }

}
