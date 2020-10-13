package com.unicom.urban.management.web.framework.security.xss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * xss过滤器
 *
 * @author liukai
 */
@Slf4j
public class XssFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(request);
        filterChain.doFilter(request, response);
    }


}
