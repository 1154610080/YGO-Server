package com.ygo.controller;

import com.ygo.model.DataPacket;
import io.netty.channel.Channel;

/**
 * 房间控制器
 * 处理玩家在房间内的请求
 *
 * @author Egan
 * @date 2018/5/22 13:16
 **/
public class RoomController extends AbstractController {

    RoomController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

    }
}
