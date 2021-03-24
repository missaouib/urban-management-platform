package com.unicom.urban.management.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unicom.urban.management.pojo.entity.Menu;
import com.unicom.urban.management.pojo.entity.Role;
import com.unicom.urban.management.pojo.entity.User;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

    private Set<SecurityAuthorityBean> authorities;

    private Integer sts;

    public SecurityUserBean() {

    }

    public SecurityUserBean(String id, String username, String name) {
        this.id = id;
        this.username = username;
        this.name = name;
    }

    public SecurityUserBean(String id, String username, String name, Set<SecurityRoleBean> roles) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.roleList = roles;
    }

    public SecurityUserBean(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.sts = user.getSts();
        this.roleList = (user.getRoleList().stream().map(SecurityRoleBean::new).collect(Collectors.toSet()));
        this.dept = new SecurityDeptBean(user.getDept());
        this.authorities = obtainAuthorities(user.getRoleList());
    }


    private Set<SecurityAuthorityBean> obtainAuthorities(List<Role> roleList) {
        Set<SecurityAuthorityBean> grantedAuthorities = new HashSet<>();
        for (Role role : roleList) {
            List<Menu> menuList = role.getMenuList();
            for (Menu menu : menuList) {
                grantedAuthorities.add(new SecurityAuthorityBean(menu));
            }
        }
        return grantedAuthorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
