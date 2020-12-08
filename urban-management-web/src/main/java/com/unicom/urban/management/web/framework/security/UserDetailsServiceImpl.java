package com.unicom.urban.management.web.framework.security;

import com.unicom.urban.management.common.exception.authentication.NotDeptException;
import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.pojo.SecurityDeptBean;
import com.unicom.urban.management.pojo.SecurityRoleBean;
import com.unicom.urban.management.pojo.SecurityUserBean;
import com.unicom.urban.management.pojo.entity.Dept;
import com.unicom.urban.management.pojo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

/**
 * 登录时查询用户
 *
 * @author liukai
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        SecurityUserBean userBean = new SecurityUserBean();

        userBean.setId(user.getId());

        userBean.setUsername(user.getName());

        userBean.setPassword(user.getPassword());

        userBean.setRoleList(user.getRoleList().stream().map(role -> {
            SecurityRoleBean securityUserBean = new SecurityRoleBean();
            securityUserBean.setId(role.getId());
            securityUserBean.setRoleName(role.getName());
            return securityUserBean;
        }).collect(Collectors.toSet()));

        SecurityDeptBean deptBean = new SecurityDeptBean();
        Dept dept = user.getDept();
        if (dept == null) {
            throw new NotDeptException("该用户没有配置部门");
        }
        deptBean.setId(dept.getId());
        deptBean.setDeptName(dept.getDeptName());
        userBean.setDept(deptBean);
        return userBean;

    }


}
