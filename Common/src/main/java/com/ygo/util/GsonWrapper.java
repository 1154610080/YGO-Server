package com.ygo.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ygo.model.Room;
import io.netty.util.CharsetUtil;

import java.lang.reflect.Type;

/**
 * Gson封装类
 * 统一设置字段忽略策略
 * 
 * @author Egan
 * @date 2018/5/12 10:26
 **/

public class GsonWrapper{
    
    private GsonBuilder builder;
    
    public GsonWrapper(){
        builder = new GsonBuilder();
        
        setExclusionStrategies();

    }
    
    /**
     * 如果请求来自决斗服务器，不字段设置忽略策略
     **/
    public GsonWrapper(boolean isDuelServer){
        builder = new GsonBuilder();
        if(!isDuelServer)
            setExclusionStrategies();
        
    }

    /**
     * 忽略用户隐私信息
     *
     * @date 2018/5/17 19:41  
     * @param    
     * @return void
     **/  
    private void setExclusionStrategies(){
        builder.serializeNulls().setExclusionStrategies(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                String name = fieldAttributes.getName();
                return name.contains("password") || name.contains("ip") || name.contains("port");
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        });
    }
    
    /**
     * 将对象转换成json
     *
     * @date 2018/5/17 19:40  
     * @param object 需要转换的对象  
     * @return byte[] json字节数组 
     **/   
    public byte[] toJson(Object object){
        return builder.create().toJson(object).getBytes(CharsetUtil.UTF_8);
    }

    /**
     * 将json字节数组转换成目标类的实例
     *
     * @date 2018/5/17 19:35
     * @param json json字节数组
     * @param type 目标类类型
     * @return com.ygo.model.Room
     **/
    public Room toObject(byte[] json, Type type){
        return builder.create().fromJson(new String(json), type);
    }
}
