package com.unicom.urban.management.api.framework.security;

import com.unicom.urban.management.pojo.SecurityRoleBean;
import com.unicom.urban.management.pojo.SecurityUserBean;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * JWT token manager.
 *
 * @author wfnuser
 * @author nkorange
 */
@Component
public class JwtTokenManager {

    private static final String AUTHORITIES_KEY = "auth";

    @Autowired
    private AuthConfigs authConfigs;

    /**
     * Create token.
     *
     * @param authentication auth info
     * @return token
     */
    public String createToken(Authentication authentication) {
        SecurityUserBean securityUserBean = (SecurityUserBean) authentication.getPrincipal();
        return createToken(securityUserBean);
    }

    private String createToken(SecurityUserBean securityUserBean) {
        long now = System.currentTimeMillis();
        Date validity = new Date(now + authConfigs.getTokenValidityInSeconds() * 1000L);
        Claims claims = Jwts.claims();
        claims.put("id", securityUserBean.getId());
        claims.put("username", securityUserBean.getUsername());
        claims.put("name", securityUserBean.getName());
        claims.put("roleList", securityUserBean.getRoleList());
        return Jwts.builder().setClaims(claims).setExpiration(validity).signWith(Keys.hmacShaKeyFor(authConfigs.getSecretKeyBytes()), SignatureAlgorithm.HS256).compact();
    }


    /**
     * Create token.
     *
     * @param userName auth info
     * @return token
     */
    public String createToken(String userName) {

        long now = System.currentTimeMillis();

        Date validity = new Date(now + authConfigs.getTokenValidityInSeconds() * 1000L);

        Claims claims = Jwts.claims().setSubject(userName).setId("11111");
        return Jwts.builder().setClaims(claims).setExpiration(validity).signWith(Keys.hmacShaKeyFor(authConfigs.getSecretKeyBytes()), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Get auth Info.
     *
     * @param token token
     * @return auth info
     */
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(authConfigs.getSecretKeyBytes()).build().parseClaimsJws(token).getBody();

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get(AUTHORITIES_KEY));

        String userId = (String) claims.get("id");
        String name = (String) claims.get("name");
        String username = (String) claims.get("username");
        List<Map<String, String>> roleList = (ArrayList<Map<String, String>>) claims.get("roleList");
        Set<SecurityRoleBean> roles = new HashSet<>();
        for (Map<String, String> stringStringMap : roleList) {
            SecurityRoleBean roleBean = new SecurityRoleBean();
            roleBean.setId(stringStringMap.get("id"));
            roleBean.setRoleName(stringStringMap.get("roleName"));
            roles.add(roleBean);
        }


        SecurityUserBean securityUserBean = new SecurityUserBean(userId, username, name, roles);
        return new UsernamePasswordAuthenticationToken(securityUserBean, "", authorities);
    }

    /**
     * validate token.
     *
     * @param token token
     */
    public void validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(authConfigs.getSecretKeyBytes()).build().parseClaimsJws(token);
    }

}
