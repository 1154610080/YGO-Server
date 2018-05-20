package com.ygo.client;

import com.ygo.util.YGOPDecoder;
import com.ygo.util.YGOPEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 决斗客户端
 * 拥有特殊权限，可以获取用户的重要信息
 *
 * @author Egan
 * @date 2018/5/20 20:23
 **/
public class DuelClient implements Runnable{

    private String host;
    private int port;

    public DuelClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run(){

        EventLoopGroup group = new NioEventLoopGroup();
        DuelClientHandler handler = new DuelClientHandler();

        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            socketChannel.pipeline().addLast(new YGOPEncoder());
                            socketChannel.pipeline().addLast(new YGOPDecoder());
                            socketChannel.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture future = b.connect().sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
