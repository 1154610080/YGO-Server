package com.ygo.model;

import org.apache.http.HttpStatus;

import java.util.HashMap;

/**
 * Http响应状态类
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

    public ResponseStatus ok(){
        return new ResponseStatus(StatusCode.OK);
    }

    public ResponseStatus error(){
        return new ResponseStatus(StatusCode.INTERNAL_SERVER_ERROR);
    }


}
