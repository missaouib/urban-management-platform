package com.unicom.urban.management.web.framework.security;

import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.pojo.SecurityRoleBean;
import com.unicom.urban.management.pojo.SecurityUserBean;
import com.unicom.urban.management.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
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

//        userBean.setDeptId(user.getDept().getId());

//        userBean.setDeptName(user.getDept().getDeptName());

        return userBean;

    }


}
