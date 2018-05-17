package com.ygo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.protocol.HTTP;

import java.net.InetSocketAddress;

/**
 * 游戏大厅-决斗客户端
 * 用于与游戏大厅-决斗服务器通讯
 * 
 * @author Egan
 * @date 2018/5/12 11:22
 **/
public class LDClient implements Runnable{

    private String host;
    private int port;

    private static Log log = LogFactory.getLog(HTTP.class);

    public LDClient(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        LDClientHandler handler = new LDClientHandler();
        try{
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture future = b.connect().sync();
            log.info("Lobby-Duel client has connected to server.");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
