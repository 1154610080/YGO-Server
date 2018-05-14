package com.ygo.model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

/**
 * 房间类
 * @author EganChen
 * @date 2018/4/16 14:11
 */
public class Room {

    /**
     * 房间ID
     **/
    @SerializedName("id")
    private int id;

    /**
     * 房间名称
     **/
    @SerializedName("un")
    private String name;

    /**
     * 房间描述
     **/
    @SerializedName("ds")
    private String desc;

    /**
     * 房间密码
     **/
    private String password;

    /**
     * 房间是否存在密码
     **/
    private boolean hasPwd = false;

    /**
     * 房间是否已经开始游戏
     **/
    @SerializedName("ip")
    private boolean isPlaying = false;

    /**
     * 房主
     **/
    @SerializedName("hs")
    private Player host;

    /**
     * 房客
     **/
    @SerializedName("gs")
    private List<Player>guests = new LinkedList<>();

    public Room(){}

    public Room(int id, String name, String desc, String password,
                boolean hasPwd, boolean isPlaying,
                Player host, List<Player> guests) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.password = password;
        this.hasPwd = hasPwd;
        this.isPlaying = isPlaying;
        this.host = host;
        this.guests = guests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasPwd() {
        return hasPwd;
    }

    public void setHasPwd(boolean hasPwd) {
        this.hasPwd = hasPwd;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public List<Player> getGuests() {
        return guests;
    }

    public void setGuests(List<Player> guests) {
        this.guests = guests;
    }
}
