package com.ygo.model;

import com.google.gson.annotations.SerializedName;

/**
 * 用户类
 * @author EganChen
 * @date 2018/4/16 14:10
 */
public class Player {

    /**
     * 玩家名
     **/
    @SerializedName("nm")
    private String name;

    /**
     * 玩家头像图片
     **/
    @SerializedName("hd")
    private byte[] head;

    /**
     * 玩家主机IP地址
     **/
    @SerializedName("ad")
    private String ip;


    /**
     * 玩家主机端口号
     **/
    @SerializedName("pr")
    private int port;

    /**
     * 玩家是否为房主
     **/
    @SerializedName("ih")
    private boolean isHost = false;

    /**
     * 玩家猜拳的结果
     **/
    @SerializedName("fg")
    private int finger = 0x0;

    /**
     * 玩家（房主）是否进入开始状态
     **/
    @SerializedName("iss")
    private boolean isStarting = false;

    /**
     * 玩家（房客）是否进入准备状态
     **/
    @SerializedName("isp")
    private boolean isPrepared = false;

    @Override
    public boolean equals(Object obj) {

        return obj instanceof Player &&
                ((Player)obj).ip.equals(ip) &&
                ((Player)obj).port == port;
    }

    public Player(){}

    public Player(String name, byte[] head,
                  String ip, int port,
                  boolean isStarting, boolean isPrepared) {
        this.name = name;
        this.head = head;
        this.ip = ip;
        this.port = port;
        this.isStarting = isStarting;
        this.isPrepared = isPrepared;
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
