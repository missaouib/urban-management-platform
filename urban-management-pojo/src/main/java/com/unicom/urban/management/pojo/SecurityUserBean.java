package com.unicom.urban.management.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicom.urban.management.pojo.entity.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

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

    public SecurityUserBean(String id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public SecurityUserBean(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.sts = user.getSts();
        this.roleList = (user.getRoleList().stream().map(SecurityRoleBean::new).collect(Collectors.toSet()));
        this.dept = new SecurityDeptBean(user.getDept());
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
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }


}
