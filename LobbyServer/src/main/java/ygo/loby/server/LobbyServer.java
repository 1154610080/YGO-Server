package ygo.loby.server;

import io.netty.channel.*;
import org.apache.commons.logging.LogFactory;
import ygo.comn.constant.Secret;
import ygo.comn.constant.YGOP;
import ygo.comn.controller.Console;
import ygo.comn.controller.IpFilterHandler;
import ygo.comn.controller.RedisClient;
import ygo.comn.model.GlobalMap;
import ygo.comn.util.YGOPDecoder;
import ygo.comn.util.YGOPEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * 游戏大厅服务器主类
 *
 * @author Egan
 * @date 2018/5/7 22:55
 **/
public class LobbyServer{

    private int port;

    private LobbyServer(int port){this.port = port;}

    private void start() throws InterruptedException {

        InetSocketAddress address = new InetSocketAddress(port);

        RedisClient redis = GlobalMap.getRedisforLobby(address);

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(address)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new YGOPEncoder());
                            socketChannel.pipeline().addLast(new YGOPDecoder());
                            socketChannel.pipeline().addLast(new LobbyServerHandler());
                            socketChannel.pipeline().addLast(new IpFilterHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, YGOP.HEAD_LEN + YGOP.MAX_LEN)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = b.bind(port).sync();
            LogFactory.getLog("LobbyServer")
                    .info(new String(("游戏大厅正在监听端口 " + port + "...").getBytes(), YGOP.CHARSET));

            new Console(redis).start();

            future.channel().closeFuture();

        }finally {

            //删除所有未在进行游戏的房间和玩家
            redis.flush();

            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        LobbyServer server = new LobbyServer(Secret.LOBBY_PORT);
        server.start();
    }

}
