package com.ygo.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * UDP服务器类
 * 用于协助双方客户端实现NAT穿透
 *
 * @author Egan
 * @date 2018/5/12 10:22
 **/
public class UDPServer {

    private int port;

    public UDPServer(int port){
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        UDPServerHandler handler = new UDPServerHandler();
        try{
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(handler);
            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
