package com.ygo.util;

import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http POST请求解析类
 *
 * @author Egan
 * @date 2018/5/7 22:53
 **/
public class RequestParser {

    /**
     * 解析POST请求
     *
     * @date 2018/5/7 23:22
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String, String> parse(HttpRequest req) throws IOException {
        HttpMethod method = req.method();

        Map<String, String> paramMap = new HashMap<>();

        HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
        decoder.offer((HttpContent) req);

        List<InterfaceHttpData> paramList = decoder.getBodyHttpDatas();
            
        for(InterfaceHttpData param : paramList){
            Attribute data = (Attribute)param;
            paramMap.put(data.getName(), data.getValue());
        }


        return paramMap;
    }

}
