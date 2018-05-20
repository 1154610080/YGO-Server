package com.ygo.client;

import com.ygo.model.DataPacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 决斗客户端的处理器
 *
 * @author Egan
 * @date 2018/5/20 20:24
 **/
public class DuelClientHandler extends SimpleChannelInboundHandler<DataPacket> {
    @Override
    protected void channelRead0
            (ChannelHandlerContext channelHandlerContext,
             DataPacket dataPacket) throws Exception {

    }
}
