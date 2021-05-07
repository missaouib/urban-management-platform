package com.unicom.urban.management.common.util;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 顾志杰
 * @date 2021/3/23-17:36
 */
public class FastdfsToken {

    private static final String TOKENKEY = "1234567890";

    public static String md5(byte[] source) throws NoSuchAlgorithmException {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(source);
        byte[] tmp = md.digest();
        char[] str = new char[32];
        int k = 0;

        for (int i = 0; i < 16; ++i) {
            str[k++] = hexDigits[tmp[i] >>> 4 & 15];
            str[k++] = hexDigits[tmp[i] & 15];
        }

        return new String(str);
    }

    public static String getToken(String remote_filename, int ts) {
        try {
            byte[] bsFilename = remote_filename.getBytes(StandardCharsets.UTF_8);
            byte[] bsKey = TOKENKEY.getBytes(StandardCharsets.UTF_8);
            byte[] bsTimestamp = (new Integer(ts)).toString().getBytes(StandardCharsets.UTF_8);
            byte[] buff = new byte[bsFilename.length + bsKey.length + bsTimestamp.length];
            System.arraycopy(bsFilename, 0, buff, 0, bsFilename.length);
            System.arraycopy(bsKey, 0, buff, bsFilename.length, bsKey.length);
            System.arraycopy(bsTimestamp, 0, buff, bsFilename.length + bsKey.length, bsTimestamp.length);

            return md5(buff);
        } catch (NoSuchAlgorithmException e) {
            return "获取token失败";
        }
    }

    public static void main(String[] args) throws Exception {
        int ts = (int) (System.currentTimeMillis() / 1000);
        String remoteFilename = "M00/00/07/wKgYy2BalsaAR9UOAAS8ZjkOiZA476.jpg";
        System.out.println("lts=" + ts);
        String token = FastdfsToken.getToken(remoteFilename, ts);
        System.out.println("token=" + token);
        System.out.println("http://192.168.24.203:8090/group1/" + remoteFilename + "?token=" + token + "&ts=" + ts);

    }
}
