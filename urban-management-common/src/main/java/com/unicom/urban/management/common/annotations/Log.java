package com.unicom.urban.management.common.annotations;

import java.lang.annotation.*;

/**
 * 记录日志
 *
 * @author liukai
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Log {

//    @AliasFor("value")
//    String logType() default "LOG_BASE_AUDIT";
//
//    String operationType() default "";
//
//    String logSubType() default "";
//
//    String details() default "";
//
//    @AliasFor("logType")
//    String value() default "LOG_BASE_AUDIT";

    String name() default "";


}
