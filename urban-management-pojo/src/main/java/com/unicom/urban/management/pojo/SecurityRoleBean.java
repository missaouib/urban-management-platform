package com.unicom.urban.management.pojo;

import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

/**
 * 存放在SpringSecurity中的实体角色对象
 *
 * @author liukai
 */
@Data
@ToString
public class SecurityRoleBean implements GrantedAuthority {

    private String id;

    @Override
    public String getAuthority() {
        return null;
    }


}
