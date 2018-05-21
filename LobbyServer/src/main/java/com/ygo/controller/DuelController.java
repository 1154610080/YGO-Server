package com.ygo.controller;

import com.ygo.constant.MessageType;
import com.ygo.constant.StatusCode;
import com.ygo.model.DataPacket;
import com.ygo.model.ResponseStatus;
import io.netty.channel.Channel;


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
    private void addRoom(){

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
