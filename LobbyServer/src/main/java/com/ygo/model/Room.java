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

    @SerializedName("id")
    private String id;
    @SerializedName("un")
    private String name;
    private String password;

    @SerializedName("mp")
    private int MaxPlayer;

    @SerializedName("pa")
    private List<User>players = new LinkedList<>();

    public Room(String id, String name, String password, List<User> players) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.players = players;
    }

    public int getMaxPlayer() {
        return MaxPlayer;
    }

    public void setMaxPlayer(int maxPlayer) {
        MaxPlayer = maxPlayer;
    }


    public List<User> getPlayers() {
        return players;
    }

    public void setPlayers(List<User> players) {
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
