package com.hollykunge.util;

import com.hollykunge.msg.ObjectRestResponse;

/**
 * @author: zhhongyu
 * @description: ajax响应赋值
 * @since: Create in 14:41 2019/12/12
 */
public class HttpResponseUtil {
    private static ObjectRestResponse setErrorBaseEntity(){
        ObjectRestResponse response = new ObjectRestResponse();
        response.setStatus(500);
        response.setRel(false);
        return response;
    }

    private static ObjectRestResponse setSuccessBaseEntity(){
        ObjectRestResponse response = new ObjectRestResponse();
        response.setStatus(200);
        response.setRel(true);
        return response;
    }

    public static ObjectRestResponse setErrorResponse(String message){
        ObjectRestResponse response = setErrorBaseEntity();
        response.setMessage(message);
        return response;
    }
    public static ObjectRestResponse setErrorResponse(){
        ObjectRestResponse response = setErrorBaseEntity();
        response.setMessage("操作失败！");
        return response;
    }

    public static ObjectRestResponse setSuccessResponse(String message){
        ObjectRestResponse response = setSuccessBaseEntity();
        response.setMessage(message);
        return response;
    }
    public static ObjectRestResponse setSuccessResponse(){
        ObjectRestResponse response = setSuccessBaseEntity();
        response.setMessage("操作成功！");
        return response;
    }
}
