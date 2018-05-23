package com.ygo.controller;

import com.ygo.model.DataPacket;
import com.ygo.util.GsonWrapper;
import io.netty.channel.Channel;

/**
 * 抽象控制器
 *
 * @author Egan
 * @date 2018/5/20 22:11
 **/
public abstract class AbstractController {

    protected DataPacket packet;

    protected Channel channel;

    GsonWrapper wrapper;

    AbstractController(DataPacket packet, Channel channel) {
        wrapper = new GsonWrapper();
        this.packet = packet;
        this.channel = channel;
        assign();
    }

    /**
     * 根据数据包的消息类型分配不同的任务
     *
     * @date 2018/5/20 22:13
     * @param
     * @return void
     **/
    protected abstract void assign();

}
