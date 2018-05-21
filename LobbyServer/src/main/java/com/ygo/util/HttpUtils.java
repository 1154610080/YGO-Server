package com.ygo.util;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Http工具类
 *
 * @author Egan
 * @date 2018/5/21 9:00
 **/
public class HttpUtils {

    /**
     * 获取get方法的请求参数
     *
     * @date 2018/5/21 9:02
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.String>
     **/
    public static Map<String, String> getRequestParams(HttpRequest request){

        Map<String, String> requestParamMap = new HashMap<>();

        if(request.method() == HttpMethod.GET){
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
            Map<String, List<String>> paramMap = decoder.parameters();

            for (Map.Entry<String, List<String>> next : paramMap.entrySet()) {
                requestParamMap.put(next.getKey(), next.getValue().get(0));
            }
        }else {
            CommonLog.log.error("Request parameters parse failed: Unsupported Method");
        }

        return requestParamMap;
    }

}
