package com.ygo.controller;

import com.ygo.model.DataPacket;
import io.netty.channel.Channel;

/**
 * 主控制器
 *
 * @author Egan
 * @date 2018/5/20 23:04
 **/
public class ChiefController extends AbstractController{

    ChiefController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

    }
}
