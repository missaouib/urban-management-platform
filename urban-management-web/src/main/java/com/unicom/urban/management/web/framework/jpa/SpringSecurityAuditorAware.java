package com.unicom.urban.management.web.framework.jpa;//package com.unicom.framework.security;


import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * JPA自动审批功能,从SecurityContextHolder拿出当前登录人的信息,
 * <p>
 * 通过@ {@link org.springframework.data.annotation.CreatedBy} 和@ {@link org.springframework.data.annotation.LastModifiedBy}
 * 在创建和修改实体的时候 自动保存当前登录人信息
 * </p>
 *
 * @author liukai
 * @date 2020/6/10 16:11
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .map(User::getUsername);
    }

}
