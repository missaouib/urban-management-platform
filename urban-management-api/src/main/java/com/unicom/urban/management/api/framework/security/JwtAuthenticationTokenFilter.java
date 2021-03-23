package com.unicom.urban.management.api.framework.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unicom.urban.management.common.enums.ErrorCodeEnum;
import com.unicom.urban.management.common.util.ResponseUtil;
import com.unicom.urban.management.pojo.Result;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt auth token filter.
 *
 * @author liukai
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtTokenManager tokenManager;

    private ObjectMapper objectMapper;

    public JwtAuthenticationTokenFilter(JwtTokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String jwt = resolveToken(request);

        if (StringUtils.isNotBlank(jwt)) {
            try {
                tokenManager.validateToken(jwt);
            } catch (ExpiredJwtException e) {
                log.debug("token已经过期", e);
                ResponseUtil.write(response, objectMapper.writeValueAsString(Result.fail(ErrorCodeEnum.TOKEN_EXPIRED)), MediaType.APPLICATION_JSON_VALUE);
                return;
            }
            Authentication authentication = tokenManager.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }

    /**
     * Get token from header.
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(SecurityConfigurer.AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        String jwt = request.getParameter(SecurityConfigurer.ACCESS_TOKEN);
        if (StringUtils.isNotBlank(jwt)) {
            return jwt;
        }
        return null;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
