package com.ygo.server;

import com.sun.net.httpserver.HttpServer;
import com.ygo.client.DuelClient;
import com.ygo.constant.YGOP;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 游戏大厅服务器主类
 *
 * @author Egan
 * @date 2018/5/7 22:55
 **/
public class LobbyServer implements Runnable{

    private static Log log = LogFactory.getLog(HttpServer.class);

    private int port;

    public LobbyServer(int port){this.port = port;}

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new HttpServerCodec());
                            socketChannel.pipeline().addLast(new HttpObjectAggregator(65536));
                            socketChannel.pipeline().addLast(new LobbyServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, YGOP.HEAD_LEN + YGOP.MAX_LEN)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = b.bind(port).sync();
            log.info("GameLobby server listening on 8844...");
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

    public static void main(String[] args) throws InterruptedException {

        LobbyServer server = new LobbyServer(8844);
        DuelClient client = new DuelClient("127.0.0.1", 19208);

        Thread lobbyThread = new Thread(server);
        Thread duelThread = new Thread(client);

        lobbyThread.start();
        duelThread.start();
    }


}
