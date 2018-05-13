package com.ygo.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.impl.bootstrap.HttpServer;

import java.net.InetSocketAddress;

/**
 * 验证服务器
 * 用于验证请求是否来自客户端，如果是，颁发token
 *
 * @author Egan
 * @date 2018/5/13 23:11
 **/
public class AuthServer {

    private int port;

    private static Log log = LogFactory.getLog(HttpServer.class);

    public AuthServer(int port){
        this.port = port;
    }

    public void start() throws InterruptedException {

        EventLoopGroup group = new NioEventLoopGroup();
        AuthServerHandler handler = new AuthServerHandler();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler);
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }

    }

}
