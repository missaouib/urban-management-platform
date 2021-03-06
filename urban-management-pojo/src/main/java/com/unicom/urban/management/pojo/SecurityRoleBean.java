package com.unicom.urban.management.pojo;

import com.unicom.urban.management.pojo.entity.Role;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 存放在SpringSecurity中的实体角色对象
 *
 * @author liukai
 */
@Data
@ToString
public class SecurityRoleBean implements Serializable {

    private String id;

    private String roleName;

    public SecurityRoleBean() {

    }

    public SecurityRoleBean(Role role) {
        this.id = role.getId();
        this.roleName = role.getName();
    }

}
