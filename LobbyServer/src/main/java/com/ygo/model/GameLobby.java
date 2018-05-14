package com.ygo.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 游戏大厅类
 * @author EganChen
 * @date 2018/4/16 14:15
 */
public class GameLobby {

    @SerializedName("mx")
    private static final int MAXIMUM = 500;

    @SerializedName("rm")
    private static List<Room> rooms = new LinkedList<>();

    public static int getMAXIMUM() {
        return MAXIMUM;
    }

    public static List<Room> getRooms() { return rooms; }

    public static void setRooms(List<Room> rooms) { GameLobby.rooms = rooms; }
}
