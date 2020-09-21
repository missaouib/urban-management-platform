package com.unicom.urban.management.web.framework.security.captcha;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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
     *
     * @param length       验证码长度
     * @param expireSecond 过期时间
     */
    public static Captcha generate(int length, int expireSecond) {
        int width = 100;
        int height = 42;
        int lineCount = 50;

        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        char[] codeSequence = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};


        int x;
        int fontHeight;
        int codeY;

        x = width / (length + 2);
        fontHeight = height - 2;
        codeY = height - 4;
        Graphics2D g = buffImg.createGraphics();
        Random random = new Random();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
        g.setFont(font);

        for (int i = 0; i < lineCount; i++) {
            // 设置随机开始和结束坐标
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width / 8);
            int ye = ys + random.nextInt(height / 8);
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawLine(xs, ys, xe, ye);
        }

        StringBuilder randomCode = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            g.drawString(strRand, (i + 1) * x, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        g.dispose();
        return new Captcha(buffImg, randomCode.toString(), expireSecond);
    }

}
