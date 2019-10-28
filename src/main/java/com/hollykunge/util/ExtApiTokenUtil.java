package com.hollykunge.util;

import com.hollykunge.service.ExtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 10:00 2019/10/28
 */
@Slf4j
@Component
public class ExtApiTokenUtil {
    @Autowired
    private ExtTokenService extTokenService;

    public void extApiToken(String clentIp,String interfaceAdress) {
        String token = null;
        try {
            token = extTokenService.getToken(clentIp,interfaceAdress);
        } catch (Exception e) {
            log.error(ExceptionCommonUtil.getExceptionMessage(e));
        }
        getRequest().setAttribute("vote_token", token);
        getRequest().getSession().setAttribute("vote_token",token);
    }

    public HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }
}
