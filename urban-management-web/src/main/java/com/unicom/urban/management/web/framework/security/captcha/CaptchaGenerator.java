package com.unicom.urban.management.web.framework.security.captcha;

import cn.hutool.captcha.ICaptcha;

/**
 * 验证码生成器
 *
 * @author liukai
 */
public class CaptchaGenerator {

    private CaptchaGenerator() {

    }

    /**
     * 生成验证码
     */
    public static ICaptcha generate(int width, int height, int length, long expireTime) {
        return new Captcha(width, height, length, expireTime);
    }

}
