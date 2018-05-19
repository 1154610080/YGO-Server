package com.ygo.constant;

import org.omg.CORBA.INTERNAL;

/**
 * 响应状态码枚举
 *
 * @author Egan
 * @date 2018/5/16 12:32
 **/
public enum StatusCode {
    OK(2000, "success"),
    COMMUNICATION_ERROR(4121, "the communication protocol is different"),
    OUT_OF_SCOPE(4131, "the request too long"),
    INCORRECT(4316, "incorrect password"),
    PlAYING(4381, "room is playing"),
    UNPREPARED(4382, "guest is unprepared"),
    DISMISSED(4383, "room is dismissed"),
    FULL_LOBBY(4391, "lobby is full"),
    FULL_ROOM(4392, "room is full"),
    UNSUPPORTED_METHOD(4050, "unsupported request method"),
    INTERNAL_SERVER_ERROR(5000, "internal server error");

    private int code;
    private String msg;

    StatusCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
