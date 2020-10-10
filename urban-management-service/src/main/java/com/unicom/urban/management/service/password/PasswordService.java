package com.unicom.urban.management.service.password;

/**
 * @author liukai
 */
public interface PasswordService {

    /**
     * 获取默认密码
     *
     * @return 密码
     */
    String getDefaultPassword();


    /**
     * 对密码进行加密
     *
     * @return 加密过的密码
     */
    String encode(String password);


    /**
     * 判断密码是否匹配
     *
     * @param rawPassword     明文密码
     * @param encodedPassword 加密之后的密码
     * @return 匹配为true
     */
    boolean matches(CharSequence rawPassword, String encodedPassword);


}
