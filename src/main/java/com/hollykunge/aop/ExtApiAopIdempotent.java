package com.hollykunge.aop;

import com.alibaba.fastjson.JSONObject;
import com.hollykunge.annotation.ExtApiIdempotent;
import com.hollykunge.annotation.ExtApiToken;
import com.hollykunge.constants.VoteConstants;
import com.hollykunge.dictionary.VoteHttpResponseStatus;
import com.hollykunge.msg.ObjectRestResponse;
import com.hollykunge.service.ExtTokenService;
import com.hollykunge.util.ClientIpUtil;
import com.hollykunge.util.ExtApiTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Aspect
@Component
public class ExtApiAopIdempotent {
	@Autowired
	private ExtApiTokenUtil extApiTokenUtil;
	@Autowired
	private ExtTokenService extTokenService;

	@Pointcut("execution(public * com.hollykunge.controller.*.*(..))")
	public void rlAop() {
	}

	// 前置通知转发Token参数
	@Before("rlAop()")
	public void before(JoinPoint point) {
		MethodSignature signature = (MethodSignature) point.getSignature();
		ExtApiToken extApiToken = signature.getMethod().getDeclaredAnnotation(ExtApiToken.class);
		if (extApiToken != null) {
			HttpServletRequest request = extApiTokenUtil.getRequest();
			String clientIp = ClientIpUtil.getClientIp(request);
			String interfaceAdress = extApiToken.interfaceAdress();
			extApiTokenUtil.extApiToken(clientIp,interfaceAdress);
		}
	}

	// 环绕通知验证参数
	@Around("rlAop()")
	public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
		ExtApiIdempotent extApiIdempotent = signature.getMethod().getDeclaredAnnotation(ExtApiIdempotent.class);
		if (extApiIdempotent != null) {
			return extApiIdempotent(proceedingJoinPoint, signature);
		}
		// 放行
		Object proceed = proceedingJoinPoint.proceed();
		return proceed;
	}

	// 验证Token
	public Object extApiIdempotent(ProceedingJoinPoint proceedingJoinPoint, MethodSignature signature)
			throws Throwable {
		synchronized (this){
			ExtApiIdempotent extApiIdempotent = signature.getMethod().getDeclaredAnnotation(ExtApiIdempotent.class);
			if (extApiIdempotent == null) {
				// 直接执行程序
				Object proceed = proceedingJoinPoint.proceed();
				return proceed;
			}
			// 代码步骤：
			// 1.获取令牌 存放在请求头中
			HttpServletRequest request = extApiTokenUtil.getRequest();
			String valueType = extApiIdempotent.value();
			if (StringUtils.isEmpty(valueType)) {
                response502("参数错误!");
				log.warn("参数错误！");
				return null;
			}
			String token = null;
			if (valueType.equals(VoteConstants.EXTAPIHEAD)) {
//				token = (String) request.getSession().getAttribute("vote_token");
				token = request.getHeader("vote_token");
			} else {
				token = request.getParameter("vote_toke");
			}
			if (StringUtils.isEmpty(token)) {
                response502("参数错误!");
				log.warn("没有找到重复表单的token！");
				log.warn("参数错误！");
				return null;
			}
			String caCheToken = extTokenService.getCaCheToken(token);
            String failCacheToken = null;
			if(StringUtils.isEmpty(caCheToken)){
                failCacheToken = extTokenService.getFailCacheToken(token);
            }
            if(!StringUtils.isEmpty(failCacheToken)){
                //失效的缓存不为空，证明页面停留时间过长，导致token失效。
                response503("页面停留时间太长，正在刷新页面，请重新录入数据");
                //清除该失效的缓存
                extTokenService.removeFailCache(failCacheToken);
                return null;
            }else if(StringUtils.isEmpty(caCheToken)){
                //真正的幂等性问题
                response502("请勿重复提交!");
                if(extApiIdempotent.isViewTransfer()){
                    String view = extApiIdempotent.tranferView();
                    String regex = "\\{([^}]*)\\}";
                    Pattern pattern = Pattern.compile (regex);
                    Matcher matcher = pattern.matcher (view);
                    String group = "";
                    if(matcher.find()){
                        group = matcher.group();
                        group = group.substring(1,group.length()-1);
                    }
                    Object[] args = proceedingJoinPoint.getArgs();
                    String[] parameterNames = signature.getParameterNames();
                    Object id = null;
                    for(int i = 0;i<parameterNames.length;i++){
                        if(!StringUtils.isEmpty(group) && group.equals(parameterNames[i])){
                            id = args[i];
                        }
                    }
                    if(id != null){
                        view = view.substring(0,view.indexOf("{"))+id;
                    }
                    return view;
                }
                return null;
            }
			Object proceed = proceedingJoinPoint.proceed();
			extTokenService.removeCache(caCheToken);
			if(proceed instanceof ObjectRestResponse){
				ObjectRestResponse infor = (ObjectRestResponse)proceed;
				//涉及ajax请求，返回提示信息
				if(infor.getStatus() == VoteHttpResponseStatus.INFORMATIONAL.getValue()){
					return proceed;
				}
			}
			return proceed;
		}
	}


	public void response502(String msg) throws IOException {
        response(msg,502);
	}

    public void response503(String msg) throws IOException {
        response(msg,503);
    }

    public void response(String msg,int status){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        response.setHeader("Content-type", "application/json");
        response.setStatus(status);
        response.setCharacterEncoding("utf-8");
        JSONObject result = new JSONObject();
        result.put("message",msg);
        try {
            response.getOutputStream().write(result.toJSONString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
