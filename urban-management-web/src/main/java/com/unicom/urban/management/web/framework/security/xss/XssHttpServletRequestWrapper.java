package com.unicom.urban.management.web.framework.security.xss;


import cn.hutool.http.HTMLFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * XSS过滤处理
 *
 * @author liukai
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {


    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                // 防xss攻击和过滤前后空格
                escapseValues[i] = new HTMLFilter().filter(values[i].trim());
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }


    @Override
    public String getParameter(String name) {
        String values = super.getParameter(name);
        if (values != null) {
            return new HTMLFilter().filter(values.trim());
        }
        return values;
    }


}