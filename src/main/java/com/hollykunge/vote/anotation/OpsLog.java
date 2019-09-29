package com.hollykunge.vote.anotation;

import com.hollykunge.vote.constant.OpsLogType;

import java.lang.annotation.*;

/**
 * @program: Lark-Server
 * @description: 需要日志的地方加入此注解
 * @author: Mr.Do
 * @create: 2019-09-25 15:10
 */
@Documented // 定义注解的保留策略
@Inherited //说明子类可以继承父类中的该注解
@Retention(value = RetentionPolicy.RUNTIME) // 定义注解的保留策略
@Target(value = {ElementType.METHOD}) // 定义注解的作用目标
public @interface OpsLog {
    String value() default "";
    OpsLogType[] type() default {};
}
