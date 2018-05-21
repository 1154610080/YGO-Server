package com.ygo.controller;

import com.ygo.constant.MessageType;
import com.ygo.constant.StatusCode;
import com.ygo.model.DataPacket;
import com.ygo.model.GameLobby;
import com.ygo.model.ResponseStatus;
import com.ygo.model.Room;
import com.ygo.util.CommonLog;
import com.ygo.util.GsonWrapper;
import io.netty.channel.Channel;

import java.util.List;


/**
 * 决斗控制器
 * 用于处理来自决斗服务器的请求
 *
 * @author Egan
 * @date 2018/5/21 9:48
 **/
public class DuelController {

    private Channel channel;

    private DataPacket packet;

    public DuelController(Channel channel, DataPacket packet){
        this.channel = channel;
        this.packet = packet;

        assign();
    }

    /**
     * 根据消息类型分配任务
     *
     * @date 2018/5/21 9:54
     * @param
     * @return void
     **/
    private void assign() {

        switch (packet.getType())
        {
            case CREATE:
                addRoom();break;
            case JOIN:
                joinRoom();break;
            case REQUIRED_ROOMS:
                getRooms();break;
            case LEAVE:
                removeRoom();break;
            default:
                channel.writeAndFlush(
                        new DataPacket(
                                new ResponseStatus(StatusCode.COMMUNICATION_ERROR,
                                "Nonexistent Type"),
                                MessageType.WARING)
                );
        }

    }

    /**
     * 添加新房间
     *
     * @date 2018/5/21 9:59
     * @param
     * @return void
     **/
    private synchronized void addRoom(){

        try {
            Room room = new GsonWrapper().toObject(packet.getBody(), Room.class);
            if(room == null || room.getHost() == null)
            {
                CommonLog.log.error("Incomplete room which come from Duel-Server!");
                return;
            }

            //分配id

            List<Room> rooms = GameLobby.getLobby().getRooms();

            int id = 0;
            room.setId(0);
            for (; id < rooms.size() && id == rooms.get(id).getId()-1; id++ );

            room.setId(id + 1);

            rooms.add(id, room);

        }catch (Exception e){
            CommonLog.log.error(e.toString());
        }

    }

    /**
     * 获取房间列表
     *
     * @date 2018/5/21 10:00
     * @param
     * @return void
     **/
    private void getRooms(){

    }

    /**
     * 加入房间
     *
     * @date 2018/5/21 10:01
     * @param
     * @return void
     **/
    private void joinRoom(){

    }

    /**
     * 删除房间
     *
     * @date 2018/5/21 10:00
     * @param
     * @return void
     **/
    private void removeRoom(){

    }

}
