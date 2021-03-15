package com.unicom.urban.management.api.framework.security;

import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Auth related configurations.
 *
 * @author nkorange
 * @author mai.jh
 * @since 1.2.0
 */
@Configuration
public class AuthConfigs {
    
//    @JustForTest
    private static Boolean cachingEnabled = null;
    
    /**
     * secret key.
     */
    @Value("${auth.token.secret.key:}")
    private String secretKey;
    
    /**
     * secret key byte array.
     */
    private byte[] secretKeyBytes;
    
    /**
     * Token validity time(seconds).
     */
    @Value("${auth.token.expire.seconds:18000}")
    private long tokenValidityInSeconds;
    
    /**
     * Which auth system is in use.
     */
    @Value("${nacos.core.auth.system.type:}")
    private String nacosAuthSystemType;
    
    @Value("${nacos.core.auth.server.identity.key:}")
    private String serverIdentityKey;
    
    @Value(("${nacos.core.auth.server.identity.value:}"))
    private String serverIdentityValue;
    
    @Value(("${nacos.core.auth.enable.userAgentAuthWhite:true}"))
    private boolean enableUserAgentAuthWhite;

    public byte[] getSecretKeyBytes() {
        if (secretKeyBytes == null) {
            secretKeyBytes = Decoders.BASE64.decode(secretKey);
        }
        return secretKeyBytes;
    }
    
    public long getTokenValidityInSeconds() {
        return tokenValidityInSeconds;
    }
    
    public String getNacosAuthSystemType() {
        return nacosAuthSystemType;
    }
    
    public String getServerIdentityKey() {
        return serverIdentityKey;
    }
    
    public String getServerIdentityValue() {
        return serverIdentityValue;
    }
    
    public boolean isEnableUserAgentAuthWhite() {
        return enableUserAgentAuthWhite;
    }
    
    /**
     * auth function is open.
     *
     * @return auth function is open
     */
//    public boolean isAuthEnabled() {
//        // Runtime -D parameter has higher priority:
//        String enabled = System.getProperty("nacos.core.auth.enabled");
//        if (StringUtils.isNotBlank(enabled)) {
//            return BooleanUtils.toBoolean(enabled);
//        }
//        return BooleanUtils
//                .toBoolean(EnvUtil.getProperty("nacos.core.auth.enabled", "false"));
//    }
    
    /**
     * Whether permission information can be cached.
     *
     * @return bool
     */
//    public boolean isCachingEnabled() {
//        if (Objects.nonNull(AuthConfigs.cachingEnabled)) {
//            return cachingEnabled;
//        }
//        return BooleanUtils
//                .toBoolean(EnvUtil.getProperty("nacos.core.auth.caching.enabled", "true"));
//    }
    
//    @JustForTest
//    public static void setCachingEnabled(boolean cachingEnabled) {
//        AuthConfigs.cachingEnabled = cachingEnabled;
//    }
}
