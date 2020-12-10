package com.unicom.urban.management.pojo;

import com.unicom.urban.management.pojo.entity.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * 存放在SpringSecurity中的实体用户对象
 *
 * @author liukai
 */
@Data
@ToString
public class SecurityUserBean implements UserDetails {

    private String id;

    private String name;

    private String username;

    private String password;

    private SecurityDeptBean dept;

    private Set<SecurityRoleBean> roleList;

    private Integer sts;

    public SecurityUserBean() {

    }

    public SecurityUserBean(String id, String username) {
        this.id = id;
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roleList;
    }

    public User castToUser() {
        User user = new User();
        user.setId(this.id);
        return user;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return sts == 0;
    }


}
