package com.unicom.urban.management.web.framework.security;


import com.unicom.urban.management.common.constant.CaptchaConstant;
import com.unicom.urban.management.common.util.RSAUtil;
import com.unicom.urban.management.web.framework.security.captcha.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * @author liukai
 */
@Slf4j
public class UserDetailsAuthenticationProvider extends DaoAuthenticationProvider {


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        UsernamePasswordCaptchaAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordCaptchaAuthenticationToken) authentication;

        Object principal = usernamePasswordAuthenticationToken.getPrincipal();

        Object credentials = usernamePasswordAuthenticationToken.getCredentials();

        Collection<GrantedAuthority> authorities = usernamePasswordAuthenticationToken.getAuthorities();


        String captcha = usernamePasswordAuthenticationToken.getCaptcha();

        checkCaptcha(captcha);

        String password = getOriginPassword(credentials.toString());


        return super.authenticate(new UsernamePasswordCaptchaAuthenticationToken(principal, password, authorities));

    }

    /**
     * 将前台传来的密码使用RSA解密
     */
    private String getOriginPassword(String decrypt) {
        return RSAUtil.decrypt(decrypt);
    }


    private void checkCaptcha(String captcha) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Captcha captchaInSession = (Captcha) request.getSession().getAttribute(CaptchaConstant.CAPTCHA_SESSION_KEY);
//        if (StringUtils.isEmpty(captcha)) {
//            throw new CaptchaException("验证码不能为空");
//        }
//        if (captchaInSession == null) {
//            throw new CaptchaException("验证码已经失效! 请重新输入");
//        }
//        if (captchaInSession.isExpired()) {
//            throw new CaptchaException("验证码已经失效! 请重新输入");
//        }
//        if (!captcha.equalsIgnoreCase(captchaInSession.getCode())) {
//            throw new CaptchaException("验证码错误! 请重新输入");
//        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordCaptchaAuthenticationToken.class.isAssignableFrom(authentication));
    }

}
