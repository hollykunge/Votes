package com.hollykunge.util;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * base64简单的转码工具类
 * @author zhhongyu
 */
public class Base64Utils {

    // 加密
    public static String encrypt(String str) {
        String s = null;
        try {
            byte[] b = str.getBytes("UTF-8");
            s = new BASE64Encoder().encode(b);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    // 解密
    public static String decryption(String s) {
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                byte[] b = decoder.decodeBuffer(s);
                result = new String(b, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
