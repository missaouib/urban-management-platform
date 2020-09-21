package com.unicom.urban.management.web.framework.logininfo.web;

import com.unicom.urban.management.common.constant.CaptchaConstant;
import com.unicom.urban.management.common.constant.SystemConstant;
import com.unicom.urban.management.common.util.RSAUtil;
import com.unicom.urban.management.web.framework.security.captcha.Captcha;
import com.unicom.urban.management.web.framework.security.captcha.CaptchaGenerator;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录控制器
 *
 * @author liukai
 */
@Controller
public class LoginController {

    /**
     * 跳转到登录页面
     */
    @GetMapping("/login")
    public String login(Model model) {
        return alreadyLogin() ? toIndexPage() : toLoginPage(model);
    }

    private String toLoginPage(Model model) {
        model.addAttribute("PUBLIC_KEY", RSAUtil.PUBLIC_KEY);
        return SystemConstant.PAGE + "/login";
    }

    private String toIndexPage() {
        return "redirect:/index";
    }

    private boolean alreadyLogin() {
        return !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken);
    }

    @GetMapping("/captcha.jpeg")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Captcha captcha = CaptchaGenerator.generate(CaptchaConstant.CAPTCHA_LENGTH, CaptchaConstant.CAPTCHA_EXPIRE_TIME);
        request.getSession().setAttribute(CaptchaConstant.CAPTCHA_SESSION_KEY, captcha);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        ImageIO.write(captcha.getBufferedImage(), "JPEG", response.getOutputStream());
    }


//    private String generateVerifyRand() {
//        return RandomUtil.getRandom(SystemConstant.VERIFY_RAND_LENGTH);
//    }

}
