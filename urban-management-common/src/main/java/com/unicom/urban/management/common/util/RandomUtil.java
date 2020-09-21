package com.unicom.urban.management.common.util;

import java.util.Random;

/**
 * 随机数工具类
 *
 * @author liukai
 */
public class RandomUtil {

    /**
     * 生成随机数
     *
     * @param length 生成多少位的随机数
     */
    public static String getRandom(int length) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int nextInt = random.nextInt(10);
            code.append(nextInt);
        }
        return code.toString();
    }
}
