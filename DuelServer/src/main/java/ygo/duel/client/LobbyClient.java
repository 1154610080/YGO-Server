package ygo.duel.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import ygo.comn.constant.Secret;
import ygo.comn.controller.Console;
import ygo.comn.util.YGOPDecoder;
import ygo.comn.util.YGOPEncoder;

import java.net.InetSocketAddress;

/**
 * 大厅客户端
 * 
 * @author Egan
 * @date 2018/6/18 1:33
 **/
public class LobbyClient implements Runnable{
    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap s = new Bootstrap();
            s.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(Secret.LOBBY_HOST, Secret.LOBBY_PORT))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new YGOPEncoder());
                            socketChannel.pipeline().addLast(new YGOPDecoder());
                            socketChannel.pipeline().addLast(new LobbyServerHandler());
                        }
                    });
            ChannelFuture f = s.connect();
            new Console().start();
            f.channel().closeFuture();

        }finally {
            group.shutdownGracefully();
        }
    }
}
