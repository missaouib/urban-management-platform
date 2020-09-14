package com.unicom.urban.management.common.annotations.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liukai
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileNumberConstraintValidator.class)
public @interface MobileNumber {

    String message() default "性别有误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
