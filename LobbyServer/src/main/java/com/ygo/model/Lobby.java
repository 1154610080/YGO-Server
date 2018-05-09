package com.ygo.model;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * 游戏大厅类
 * @author EganChen
 * @date 2018/4/16 14:15
 */
public class Lobby {

    private static Map<String, Room> roomMap = new HashMap<>();

    @SerializedName("rs")
    public Map<String, Room> getRooms() {
        return roomMap;
    }

}
