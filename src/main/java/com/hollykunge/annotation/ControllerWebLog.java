package com.hollykunge.annotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ControllerWebLog {
    String name();//所调用接口的名称
    boolean intoDb() default false;//标识该条操作日志是否需要持久化存储
}
