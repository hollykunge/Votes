package com.hollykunge.util;

import com.hollykunge.constants.VoteConstants;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 10:06 2019/10/28
 */
public class ClientIpUtil {
    public static String getClientIp(HttpServletRequest request){
        String clientIp = request.getHeader("clientIp");
        //如果请求头中没有ip，则为本地测试，使用默认值了
        if(StringUtils.isEmpty(clientIp)){
            clientIp = request.getRemoteHost();
        }
        if(StringUtils.isEmpty(clientIp)){
            clientIp = VoteConstants.DEFUALT_CLIENTIP;
        }
        return clientIp;
    }
}
