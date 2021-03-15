package com.unicom.urban.management.annotation;

import com.unicom.urban.management.security.WithMockCustomUserSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 用来做单元测试使用
 *
 * @author liukai
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    /**
     * 用户的ID
     */
    String id() default "";

    /**
     * 用户账号
     */
    String username() default "";

    /**
     * 姓名
     */
    String name() default "";


}
