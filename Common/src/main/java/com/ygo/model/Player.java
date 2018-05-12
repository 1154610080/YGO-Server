package com.ygo.model;

import com.google.gson.annotations.SerializedName;

/**
 * 用户类
 * @author EganChen
 * @date 2018/4/16 14:10
 */
public class Player {

    @SerializedName("nm")
    private String name;
    private String ip;
    private int port;

    public Player(String name, String ip, int port) {
        this.name = name;
        this.ip = ip;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
