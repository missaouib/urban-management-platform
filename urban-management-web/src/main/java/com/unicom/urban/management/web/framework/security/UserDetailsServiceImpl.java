package com.unicom.urban.management.web.framework.security;

import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.pojo.SecurityUserBean;
import com.unicom.urban.management.pojo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        SecurityUserBean userBean = new SecurityUserBean();

        userBean.setId(user.getId());

        userBean.setUsername(user.getName());

        userBean.setPassword(user.getPassword());

        return userBean;

    }


}
