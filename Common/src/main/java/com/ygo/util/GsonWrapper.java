package com.ygo.util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    
    public Gson create(){
        return builder.create();
    }
}
