package ygo.duel.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ygo.comn.constant.Secret;
import ygo.comn.constant.YGOP;
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

    private Log log = LogFactory.getLog("Lobby-Client");

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
            ChannelFuture f = s.connect().sync();
            log.info(new String(("决斗服务器已成功连接游戏大厅").getBytes(), YGOP.CHARSET));
            new Console().start();
            f.channel().closeFuture();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
