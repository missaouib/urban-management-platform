
package com.unicom.urban.management.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * 非对称RSA加密解密工具
 *
 * @author liukai
 */
public class RSAUtil {

    private static final Map<Integer, String> keyMap = new HashMap<Integer, String>();

    public static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCH6/CXLPkHodg3rxUaQDsrPSA1NPlWlilZ4zNe8BV+9hPRnMGB3xYqKmr915dK66gtpK8nNkGLy1pSrhbc5lQWVO8SNPRqe7rFAHnaTQcvVo825W9P4eM01NVfRwmDz4BbaYoVdIRR/Ow8XMD+cgl7rk3kgRPKLd56YEM0hiXuFQIDAQAB";
    public static String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIfr8Jcs+Qeh2DevFRpAOys9IDU0+VaWKVnjM17wFX72E9GcwYHfFioqav3Xl0rrqC2kryc2QYvLWlKuFtzmVBZU7xI09Gp7usUAedpNBy9Wjzblb0/h4zTU1V9HCYPPgFtpihV0hFH87DxcwP5yCXuuTeSBE8ot3npgQzSGJe4VAgMBAAECgYBcEuUg03NCy0z3s5+876PlT3wA7wi0sfYDERiciFBXeATG6sRe4KtYFvHxUNkJBIghmzyXeHaAZ1mhRpLwQwrUNIjU1clrk7/UTtngAtc3LMWG+pTWw0jwpcLMqdn4s2deqKPZiGSTn5AWhQK0VDBFAxbELZ4C3Y5NWMdGAkU35QJBAMhRGZRu51OiRJ/ZQ0LSydmGONa7HUIKqNpiXxwwXABL4qEje08GoO7vRKxdZxtUqpSuIfWlkorPpE8LByg2YMsCQQCttF2mrD5FIg5cJslsNB6bk2Y7nSFOoK0ne3sIzjI1uEqsrEVeAcnfW1iR6wH4y/q1XtWWwqzb79kZlTsoNXCfAkByYczHcyRoHyosVHURvSdBLGFcS/AC++869Bz3Da4bBTndZVNN5q0SglGmUpdDhe75gw0lJWwuEy7xa4ykq5hNAkEAiahzw1+jJaR8ntj6LW8MhpyEm1/GAorhTnXUJkFKlzoKq9cGTh66vfClO1ZzxJjagZu7RvPWi6IDefvC2vWm3wJBAMC31E6uvH/JeqnNIfxzAraZWq7/NSAXWfh/7FoPWGWy5Ag8mFKTHkgSAFrsVDAvVYt2IzXzyxhhH8ydun2d/P4=";


    /**
     * 随机生成密钥对
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);  //0表示公钥
        keyMap.put(1, privateKeyString);  //1表示私钥
    }

    /**
     * RSA公钥加密
     */
    public static String encrypt(String str, String publicKey) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.decodeBase64(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//"算法/模式/补码方式"
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
            return outStr;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 加密
     */
    public static String encrypt(String str) {
        return encrypt(str, PUBLIC_KEY);
    }


    /**
     * RSA私钥解密
     */
    public static String decrypt(String str, String privateKey) {
        try {
            //64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            String outStr = new String(cipher.doFinal(inputByte), "UTF-8");
            return outStr;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     */
    public static String decrypt(String str) {
        return decrypt(str, PRIVATE_KEY);
    }

}
