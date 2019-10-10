package com.hollykunge.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExceptionCommonUtil {
    private static Logger log = LoggerFactory.getLogger(ExceptionCommonUtil.class);
    //异常信息获取
    public static String getExceptionMessage(Exception e) {
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }
        return sw.toString();
    }
    //格式化double
    public static String formatDouble2(double d) {
        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(2, RoundingMode.UP);
        return String.valueOf(bg.doubleValue());
    }
    //objec转字符串 null-> ""
    public static String nulToEmptyString(Object object){
        try{
            if(object==null){
                return "";
            }
            return  object.toString();
        }catch (Exception e){
            e.printStackTrace();
            log.error(getExceptionMessage(e));
        }
        return  null;
    }
    /**
     *@Description: 把返回给前端的VO 属性是字符串的且值是null的 转换成空字符串，其它类型在代码自行处理
     *@Param: vo
     *@return: void
     *@Author: zhuqz
     *@date: 2019/06/26
     */
    public static<T> void putVoNullStringToEmptyString (T vo) throws Exception{
        if(vo==null){
            return;
        }
        //遍历enity类 成员为String类型 属性为空的全部替换为“”
        Field[] fields = vo.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            // 获取属性的名字
            String name = fields[i].getName();
            // 将属性的首字符大写，方便构造get，set方法
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            // 获取属性的类型
            String type = fields[i].getGenericType().toString();
            // 如果type是类类型，则前面包含"class "，后面跟类名
            if (type.equals("class java.lang.String")) {
                Method m = vo.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                String value = (String) m.invoke(vo);
                //System.out.println("数据类型为：String");
                if (value == null) {
                    //set值
                    Class[] parameterTypes = new Class[1];
                    parameterTypes[0] = fields[i].getType();
                    m = vo.getClass().getMethod("set" + name, parameterTypes);
                    String string = new String("");
                    Object[] objects = new Object[1];
                    objects[0] = string;
                    m.invoke(vo, objects);
                }
            }
        }
    }
}
