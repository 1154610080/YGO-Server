package com.ygo.model;

import com.ygo.constant.StatusCode;

import java.util.HashMap;

/**
 * 响应状态类
 *
 * @author Egan
 * @date 2018/5/12 9:15
 **/
public class ResponseStatus extends HashMap<String, Object>{

    public ResponseStatus(){

    }

    public ResponseStatus(int code){
        super.put("code", code);
    }

    public ResponseStatus(StatusCode status){
        super.put("code", status.getCode());
    }

    public ResponseStatus(StatusCode status, String msg){
        super.put("code", status.getCode());
    }

    /**
     * 默认成功响应
     *
     * @date 2018/5/17 19:42
     * @param
     * @return com.ygo.model.ResponseStatus
     **/
    public ResponseStatus ok(){
        return new ResponseStatus(StatusCode.OK);
    }

}
