package com.unicom.urban.management.pojo.converter;

import com.unicom.urban.management.common.exception.BusinessException;
import com.unicom.urban.management.pojo.entity.BaseEnum;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 把前台传来的字符串转换成枚举类型
 *
 * @author liukai
 */
@SuppressWarnings("all")
public class IntegerStrToEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    private static final Map<Class, Converter> converterMap = new WeakHashMap<>();

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        Converter result = converterMap.get(targetType);
        if (result == null) {
            result = new IntegerStrToEnum<T>(targetType);
            converterMap.put(targetType, result);
        }
        return result;
    }

    static class IntegerStrToEnum<T extends BaseEnum> implements Converter<String, T> {

        private final Class<T> enumType;

        private final Map<String, T> enumMap = new HashMap<>();

        public IntegerStrToEnum(Class<T> enumType) {
            this.enumType = enumType;
            T[] enums = enumType.getEnumConstants();
            for (T e : enums) {
                enumMap.put(e.getValue() + "", e);
            }
        }


        @Override
        public T convert(String source) {
            T result = enumMap.get(source);
            if (result == null) {
                throw new BusinessException("No element matches " + source);
            }
            return result;
        }

    }
}