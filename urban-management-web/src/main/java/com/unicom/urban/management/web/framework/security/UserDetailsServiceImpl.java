package com.unicom.urban.management.web.framework.security;

import com.unicom.urban.management.common.exception.authentication.NotDeptException;
import com.unicom.urban.management.dao.user.UserRepository;
import com.unicom.urban.management.pojo.Delete;
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
        User user = userRepository.findByUsernameAndDeleted(username, Delete.NORMAL);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }

        Dept dept = user.getDept();
        if (dept == null) {
            throw new NotDeptException("该用户没有配置部门");
        }

        return new SecurityUserBean(user);

    }


}
