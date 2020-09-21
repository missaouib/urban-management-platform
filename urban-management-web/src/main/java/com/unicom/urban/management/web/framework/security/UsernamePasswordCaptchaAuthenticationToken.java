package com.unicom.urban.management.web.framework.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 用来封装登录页面传来的账号 密码 和验证码
 *
 * @author liukai
 */
public class UsernamePasswordCaptchaAuthenticationToken extends UsernamePasswordAuthenticationToken {

    @Getter
    private String captcha;


    public UsernamePasswordCaptchaAuthenticationToken(Object principal, Object credentials, String captcha) {
        super(principal, credentials);
        this.captcha = captcha;
    }

    public UsernamePasswordCaptchaAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }


}
