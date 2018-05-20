package com.ygo.controller;

import com.ygo.model.DataPacket;
import io.netty.channel.Channel;

/**
 * 房间出入控制器
 * 用于处理玩家出入房间的消息
 *
 * @author Egan
 * @date 2018/5/20 22:57
 **/
public class IOboundController extends AbstractController{

    IOboundController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

    }

    /**
     * 创建房间
     *
     * @date 2018/5/20 22:41
     * @param
     * @return void
     **/
    private void createRoom(){

    }

    /**
     * 加入房间
     *
     * @date 2018/5/20 22:41
     * @param
     * @return void
     **/
    private void joinRoom(){

    }

    /**
     * 离开房间
     *
     * @date 2018/5/20 22:42
     * @param
     * @return void
     **/
    private void leaveRoom(){

    }

    /**
     * 踢出房间
     *
     * @date 2018/5/20 22:53
     * @param
     * @return void
     **/
    private void kickOut(){

    }

}
