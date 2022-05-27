package com.hollykunge.util;

import com.hollykunge.exception.BaseException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author: zhhongyu
 * @description: 解析院网关请求头中的dnname
 * @since: Create in 14:18 2020/4/29
 */
public class ParsingDnnameHeaderUtil {
    public static String parsing(String dnname) {
        Assert.notNull(dnname);
        try {
            dnname = new String(dnname.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw new BaseException("dnname中的，编码不被允许...");
        }
        String[] userObjects = dnname.trim().split(",", 0);
        String pid = null;
        for (String val :
                userObjects) {
            val = val.trim();
            if (val.contains("t=") || val.contains("T=")) {
                pid = val.substring(2, val.length());
            }
        }
        if(pid == null){
            pid = dnname;
        }
        return pid.toLowerCase();
    }
    public static String getDnname(HttpServletRequest request,String headerName){
        String dnname = request.getHeader(headerName);
        return parsing(dnname);
    }
}
