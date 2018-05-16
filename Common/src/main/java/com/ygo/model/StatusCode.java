package com.ygo.model;

/**
 * 响应状态码枚举
 *
 * @author Egan
 * @date 2018/5/16 12:32
 **/
public enum StatusCode {
    OK(2000, "success"),
    INCORRECT(4316, "incorrect password"),
    INVALID_TOKEN(4317, "invalid token"),
    Playing(4381, "room is playing"),
    Unprepared(4382, "guest is unprepared"),
    Dimissed(4383, "room is dismissed"),
    FULL_LOBBY(4391, "lobby is full"),
    FULL_ROOM(4392, "room is full");


    private int code;
    private String name;

    StatusCode(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
