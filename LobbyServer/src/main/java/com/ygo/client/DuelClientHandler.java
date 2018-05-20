package com.ygo.client;

import com.ygo.constant.MessageType;
import com.ygo.constant.YGOP;
import com.ygo.model.DataPacket;
import com.ygo.util.CommonLog;
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
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new DataPacket("hello, I'm duel client", MessageType.CHAT));
    }

    @Override
    protected void channelRead0
            (ChannelHandlerContext channelHandlerContext,
             DataPacket dataPacket) throws Exception {
        CommonLog.log.info("DuelServer: " + dataPacket.getBody());
    }
}
