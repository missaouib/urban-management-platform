package com.unicom.urban.management.security;

import com.unicom.urban.management.common.exception.authentication.NotDeptException;
import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.pojo.Delete;
import com.unicom.urban.management.pojo.SecurityUserBean;
import com.unicom.urban.management.pojo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        Optional<User> optionalUser = userRepository.findByUsernameAndDeleted(username, Delete.NORMAL);

        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("用户不存在"));

        if (user.noDept()) {
            throw new NotDeptException("该用户没有配置部门");
        }

        return new SecurityUserBean(user);

    }

}
