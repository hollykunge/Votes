package com.hollykunge.util;

/**
 * @author: zhhongyu
 * @description: integer比较类
 * @since: Create in 11:10 2019/10/25
 */
public class IntegerCompareUtil {
    public static int compareTo(Integer ob1,Integer ob2,Long id1,Long id2){
        if(ob2 != null && ob1 != null){
            return ob2.compareTo(ob1);
        }
        if(ob1 == null && ob2 != null){
            return 1;
        }
        if(ob1 != null && ob2 == null){
            return -1;
        }
        //如果都为空时，按id升序
        return id1.compareTo(id2);
    }
}
