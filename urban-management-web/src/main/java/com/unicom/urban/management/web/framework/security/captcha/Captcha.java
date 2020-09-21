package com.unicom.urban.management.web.framework.security.captcha;

import lombok.Getter;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * 图形验证码实体
 *
 * @author liukai
 */
public class Captcha {

    @Getter
    private BufferedImage bufferedImage;

    /**
     * 验证码的值
     */
    @Getter
    private String code;

    /**
     * 验证码失效时间
     */
    private LocalDateTime expireTime;


    public Captcha(BufferedImage bufferedImage, String code, int expireSecond) {
        this.bufferedImage = bufferedImage;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireSecond);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }

}
