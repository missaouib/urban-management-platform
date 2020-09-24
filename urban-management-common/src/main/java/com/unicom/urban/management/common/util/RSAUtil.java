
package com.unicom.urban.management.common.util;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * 非对称RSA加密解密工具
 *
 * @author liukai
 */
public class RSAUtil {

    public static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcTsnTIIugZvcrU+I5BJR05umVtGf9tWprQ3WeaSRazPEPrUNrTMIr9+BlSEG3+vE7R/PoxPWPBvXavaGc/SdFixgAdr2MttMpBMCr5wpJIj1Nsc/luzO/CYIN61LqzrqcgHIVESxuQodG5wawgDUHr/Bf5WvAOrMssR1CqOAKtQIDAQAB";
    public static String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJxOydMgi6Bm9ytT4jkElHTm6ZW0Z/21amtDdZ5pJFrM8Q+tQ2tMwiv34GVIQbf68TtH8+jE9Y8G9dq9oZz9J0WLGAB2vYy20ykEwKvnCkkiPU2xz+W7M78Jgg3rUurOupyAchURLG5Ch0bnBrCANQev8F/la8A6syyxHUKo4Aq1AgMBAAECgYBG4hP0gpXPL+J265tzb/hC61SSZzWhgFu6OOVl6RhrdPT8vv4L8ss7VTWVuKbU4B/1WjPqIyeYrPdhMw3PO55K2NMtIKo/U17EZBsEbRzI2FkueuMaEEnFVPqmroNwIwcpPvuxqu4SA8l143+u/ZNuO8Nlp7RZGWDB9OEJH1/iAQJBAPzUr0RhTpowxD4fHo6WiJWTIQUpYJp+yC748BAiHAxOpjfHMCmpboU0n0uy6SbE0WsyujFudfjoeW3qqRgHQLECQQCeRF5TTfp2glpG4JmcP74pjhHDuL0Np53DlrqvWILnKB4y2Xi6TyLeGBEiRgtRbIbmY6OKSB6E0zdiCyG6ZAtFAkEA0hzuiyVS7VEZxYaXHmh7mdJr3p5EaiByvJak6sNuC8xQ1onyvKzR7GGPAJnuqQloAG5nziy+XdzVDUO2wwFEAQJAOhfuZ7Wf0hBXAt0+hVQAgVBcxPLysihsiHazX9mlcyQOsauR3RFfQxaic2oyjVjAy6CXTxLZ4PeGxL5BSyGd4QJAAbtPvQtcnRQvjO2NGqb/QKvIZGYI32FczL2EEH0WFg+Ou9Cd0K7JaVcKkU6wwRi1yyfVIYaruht2hE/dH7Nrrg==";

    private final static RSA RSA = new RSA(PRIVATE_KEY, PUBLIC_KEY);


    /**
     * 加密
     */
    public static String encrypt(String str) {
        return RSA.encryptBcd(str, KeyType.PublicKey);
    }

    /**
     * 解密
     */
    public static String decrypt(String str) {
        return RSA.decryptStr(str, KeyType.PrivateKey);
    }

}
