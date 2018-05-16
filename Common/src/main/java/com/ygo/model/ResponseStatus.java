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
        return new ResponseStatus(HttpStatus.SC_OK, "success");
    }

    public ResponseStatus ok(String msg){
        return new ResponseStatus(HttpStatus.SC_OK, msg);
    }

    public ResponseStatus ok(int code, String msg){
        return new ResponseStatus(code, msg);
    }

    public ResponseStatus error(){
        return new ResponseStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR, "failure");
    }

    public ResponseStatus error(String msg){
        return new ResponseStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
    }

    public ResponseStatus error(int code, String msg){
        return new ResponseStatus(code, msg);
    }

}
