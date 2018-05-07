package com.ygo.server;

import com.sun.net.httpserver.HttpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * 游戏大厅服务器主类
 *
 * @Author Egan
 * @Date 2018/5/7
 **/
public class LobbyServer {

    private static Log log = LogFactory.getLog(HttpServer.class);

    private int port;

    LobbyServer(int port){this.port = port;}

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //编码
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());
                            //解码
                            socketChannel.pipeline().addLast(new HttpRequestDecoder());
                            socketChannel.pipeline().addLast(new LobbyServerInboundServer());
                        }
                    }).option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LobbyServer server = new LobbyServer(8844);
        log.info("Lobby server listening on 8844...");
        server.start();
    }
}
