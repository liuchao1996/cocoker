package com.cocoker.utils;


import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

/**
 * MD5通用类
 */
public class MD5 {

    private static final String HASH_MD5 = "MD5";

    // 可表示16进制数字的字符
    private static final char hexChar[] = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    /**
     * MD5方法
     *
     * @param text 明文
     * @param key  密钥
     * @return 密文
     * @throws Exception
     */
    public static String md5(String text, String key) {
        //加密后的字符串
        String encodeStr = DigestUtils.md5Hex(text + key);
//        System.out.println("MD5加密后的字符串为:encodeStr=" + encodeStr);
        return encodeStr;
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key  密钥
     * @param md5  密文
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String text, String key, String md5) throws Exception {
        //根据传入的密钥进行验证
        String md5Text = md5(text, key);
        if (md5Text.equalsIgnoreCase(md5)) {
            System.out.println("MD5验证通过");
            return true;
        }

        return false;
    }

    public final static String encoding(byte[] bs) {

        String encodingStr = null;
        try {
            MessageDigest mdTemp = MessageDigest.getInstance(HASH_MD5);
            mdTemp.update(bs);

            return toHexString(mdTemp.digest());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return encodingStr;
    }

    /**
     * 转换为用16进制字符表示的MD5
     *
     * @param b
     * @return
     */
    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }
}