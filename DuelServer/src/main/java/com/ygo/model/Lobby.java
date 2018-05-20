package com.ygo.model;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ygo.constant.YGOP;
import com.ygo.util.GsonWrapper;
import io.netty.channel.Channel;

import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

/**
 * 游戏大厅类
 * 保存来自游戏大厅服务器的数据
 *
 * @author Egan
 * @date 2018/5/21 0:34
 **/
public class Lobby {

    /**
     * 最大房间数
     **/
    private static int maxRoom;

    /**
     * 房间列表
     **/
    private static List<Room> rooms;

    /**
     * 连接游戏大厅服务器的通道
     **/
    private static Channel channel;

    /**
     * 接收来自游戏大厅的数据，刷新当前数据
     *
     * @date 2018/5/21 0:38
     * @param jsonStr 数据包的json消息体
     * @return void
     **/
    public static void refresh(String jsonStr){
        JsonObject json = new JsonObject();
        maxRoom = json.get("mx").getAsInt();

        String roomsJsonStr = json.get("rm").getAsString();

        rooms = new GsonWrapper().toObject(roomsJsonStr.getBytes(YGOP.CHARSET),
                new TypeToken<LinkedList<Room>>(){}.getType());

    }

    public static int getMaxRoom() {
        return maxRoom;
    }

    public static void setMaxRoom(int maxRoom) {
        Lobby.maxRoom = maxRoom;
    }

    public static List<Room> getRooms() {
        return rooms;
    }

    public static void setRooms(List<Room> rooms) {
        Lobby.rooms = rooms;
    }

    public static Channel getChannel() {
        return channel;
    }

    public static void setChannel(Channel channel) {
        Lobby.channel = channel;
    }
}
