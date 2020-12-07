package com.unicom.urban.management.common.configurer;

import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 顾志杰
 * @date 2020/12/7-10:40
 */
@Configuration
public class GisServer  {
    @Bean
    public Servlet baiduProxyServlet(){
        return new ProxyServlet();
    }


    @Bean
    public ServletRegistrationBean<Servlet> servletServletRegistrationBean() {
        ServletRegistrationBean<Servlet> registrationBean = new ServletRegistrationBean<>(baiduProxyServlet(), "/geoserver");
        Map<String, String> params = new HashMap<>();
        params.put("targetUri", "http://192.168.24.203:7880/geoserver/guyuan20201010/wms");
        params.put("log", "true");
        registrationBean.setInitParameters(params);
        return registrationBean;
    }
}
