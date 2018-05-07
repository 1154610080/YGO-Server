package com.ygo.util;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Http请求解析类
 *
 * @author Egan
 * @date 2018/5/7 22:53
 **/
public class RequestParser {

    private FullHttpRequest req;

    public RequestParser(FullHttpRequest req){ this.req = req;}

    /**
     * 解析请求
     *
     * @date 2018/5/7 23:22
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public Map<String, String> parse() throws IOException {
        HttpMethod method = req.method();

        Map<String, String> paramMap = new HashMap<>();

        if(HttpMethod.GET == method){
            //Get请求
            QueryStringDecoder decoder = new QueryStringDecoder(req.uri());

            decoder.parameters().entrySet().forEach( entry -> {
                //entry是一个list，只取第一个元素
                paramMap.put(entry.getKey(), entry.getValue().get(0));
            });
        }else if(HttpMethod.POST == method){
            //Post请求
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(req);
            decoder.offer(req);

            List<InterfaceHttpData> paramList = decoder.getBodyHttpDatas();
            
            for(InterfaceHttpData param : paramList){
                Attribute data = (Attribute)param;
                paramMap.put(data.getName(), data.getValue());
            }
        }else{
            System.err.println("错误：不支持其他方法！");
        }

        return paramMap;
    }

}
