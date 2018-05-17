package com.ygo.model;

import org.apache.http.HttpStatus;

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

    public ResponseStatus(int code, String msg){
        super.put("code", code);
        super.put("msg", msg);
    }

    public ResponseStatus(StatusCode status){
        super.put("code", status.getCode());
        super.put("msg", status.getMsg());
    }

    public ResponseStatus(StatusCode status, String msg){
        super.put("code", status.getCode());
        super.put("msg", msg);
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
