package com.ygo.constant;

import org.omg.CORBA.INTERNAL;

/**
 * 响应状态码枚举
 *
 * @author Egan
 * @date 2018/5/16 12:32
 **/
public enum StatusCode {
    OK(2000),

    COMMUNICATION_ERROR(4121),
    OUT_OF_SCOPE(4131),
    BLACKLISTED(4314),
    INCORRECT(4316),
    PlAYING(4381),
    UNPREPARED(4382),
    DISMISSED(4383),
    FULL_LOBBY(4391),
    FULL_ROOM(4392),
    UNSUPPORTED_METHOD(4050),
    INTERNAL_SERVER_ERROR(5000);

    private int code;

    StatusCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
