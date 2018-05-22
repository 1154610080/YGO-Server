package com.ygo.server;

import com.ygo.constant.YGOP;
import com.ygo.util.CommonLog;
import com.ygo.util.YGOPDecoder;
import com.ygo.util.YGOPEncoder;
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
import org.apache.commons.logging.LogFactory;

/**
 * 游戏大厅服务器主类
 *
 * @author Egan
 * @date 2018/5/7 22:55
 **/
public class LobbyServer{

    private int port;

    public LobbyServer(int port){this.port = port;}

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new YGOPEncoder());
                            socketChannel.pipeline().addLast(new YGOPDecoder());
                            socketChannel.pipeline().addLast(new LobbyServerHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG, YGOP.HEAD_LEN + YGOP.MAX_LEN)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = b.bind(port).sync();
            CommonLog.log.info(new String(("游戏大厅正在监听端口 " + port + "...").getBytes(), YGOP.CHARSET));
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();

        }
    }

    public static void main(String[] args) throws InterruptedException {

//        Player player1 = new Player();
//        player1.setName("测试房主1");
//        Player player2 = new Player();
//        player2.setName("测试房客1");
//        Player player3 = new Player();
//        player3.setName("测试房主2");
//        Player player4 = new Player();
//        player4.setName("测试房客2");
//        Room room1 = new Room();
//        room1.setId(1);
//        room1.setHost(player1);
//        room1.setGuest(player2);
//        room1.setName("测试房间1");
//        room1.setDesc("用于测试的房间");
//        Room room2 = new Room();
//        room2.setId(2);
//        room2.setHost(player3);
//        room2.setGuest(player4);
//        room2.setName("测试房间2");
//        room2.setDesc("用于测试的房间");
//        GameLobby.getLobby().getRooms().add(room1);
//        GameLobby.getLobby().getRooms().add(room2);


        LobbyServer server = new LobbyServer(8192);
        server.start();
    }

    static{
        CommonLog.log = LogFactory.getLog("Lobby-Server");
    }
}
