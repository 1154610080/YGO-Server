package com.ygo.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.DatagramPacket;

/**
 * UDPServer
 * 
 * @author Egan
 * @date 2018/5/12 10:28
 **/
public class UDPServerHandler extends SimpleChannelInboundHandler<DatagramPacket>{


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                DatagramPacket datagramPacket) throws Exception {

    }
}
