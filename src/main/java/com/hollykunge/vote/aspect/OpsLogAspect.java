package com.hollykunge.vote.aspect;

import com.hollykunge.vote.anotation.OpsLog;
import com.hollykunge.vote.constant.OpsLogType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @program: Lark-Server
 * @description: 操作日志记录
 * @author: Mr.Do
 * @create: 2019-09-25 15:07
 */
@Slf4j
@Configuration
@Aspect
public class OpsLogAspect {

    @Pointcut(value = "@annotation(com.hollykunge.vote.anotation.OpsLog)")
    public void opsLogAnnotation() {}

    @AfterReturning(pointcut = "opsLogAnnotation()", returning = "object")
    public void doAfterReturning(JoinPoint joinPoint, Object object) {

        // 获取方法的签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        Method method = methodSignature.getMethod();
        OpsLog opsLog = method.getAnnotation(OpsLog.class);

        String value = opsLog.value();

        OpsLogType[] opsLogTypes = opsLog.type();
        String opsLogTypeName = null;
        if (opsLogTypes.length > 0) {
            opsLogTypeName = opsLogTypes[0].name();
        }
        log.warn("执行的操作：{}, 类型：{}", value, opsLogTypeName);

    }
}
