package com.unicom.urban.management.security;

import com.unicom.urban.management.annotation.WithMockCustomUser;
import com.unicom.urban.management.pojo.SecurityUserBean;
import com.unicom.urban.management.web.framework.security.UsernamePasswordCaptchaAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        SecurityUserBean principal = new SecurityUserBean(annotation.id(), annotation.username());
        Authentication auth = new UsernamePasswordCaptchaAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }


}
