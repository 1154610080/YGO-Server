package com.ygo.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 游戏大厅类
 * 采用线程安全的单列模式
 *
 * @author EganChen
 * @date 2018/4/16 14:15
 */
public class GameLobby {

    private volatile GameLobby lobby;

    public GameLobby getLobby() {
        if(lobby == null){
            synchronized (GameLobby.class){
                if (lobby == null)
                    lobby = new GameLobby();
            }
        }
        return lobby;
    }

    @SerializedName("mx")
    private final int MAXIMUM = 500;

    @SerializedName("rm")
    private List<Room> rooms = new LinkedList<>();

    public int getMAXIMUM() {
        return MAXIMUM;
    }

    public List<Room> getRooms() { return rooms; }

    public void setRooms(List<Room> rooms) { this.rooms = rooms; }
}
