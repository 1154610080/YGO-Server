package com.ygo.lobby;

import java.util.LinkedList;
import java.util.List;

/**
 * 游戏大厅类
 * @author EganChen
 * @date 2018/4/16 14:15
 */
public class Lobby {

    private static List<Room> rooms = new LinkedList<>();

    public List<Room> getRooms() {
        return rooms;
    }
}
