package com.ygo.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 游戏大厅类
 * @author EganChen
 * @date 2018/4/16 14:15
 */
public class GameLobby {

    private static final int MAXIMUM = 500;

    private static Map<String, Room> roomMap = new HashMap<>();

    public static Map<String, Room> getRoomMap() {
        return roomMap;
    }

    public static int getMAXIMUM() {
        return MAXIMUM;
    }
}
