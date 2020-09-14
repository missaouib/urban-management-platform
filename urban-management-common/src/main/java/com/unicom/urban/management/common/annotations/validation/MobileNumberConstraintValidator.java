package com.unicom.urban.management.common.annotations.validation;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 校验手机号码
 *
 * @author liukai
 */
public class MobileNumberConstraintValidator implements ConstraintValidator<MobileNumber, String> {

    private final static String PATTERN = "0?(13|14|15|18|19|16|17)[0-9]{9}";

    @Override
    public void initialize(MobileNumber constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return Pattern.matches(PATTERN, value);
    }

}
