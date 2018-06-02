package ygo.duel.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ygo.comn.util.YGOPDecoder;
import ygo.comn.util.YGOPEncoder;

import java.net.InetSocketAddress;

/**
 * 大厅客户端
 * 用于向大厅服务器发送请求
 *
 * @author Egan
 * @date 2018/6/2 10:13
 **/
public class LobbyClient implements Runnable{

    private String ip;
    private int port;

    /**
     * 连接大厅服务器的通道
     **/
    public static Channel channel;

    private Log log = LogFactory.getLog("Lobby-Client");

    @Override
    public void run() {

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(ip, port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new YGOPEncoder());
                            socketChannel.pipeline().addLast(new YGOPDecoder());
                            socketChannel.pipeline().addLast(new LobbyClientHandler());
                        }
                    });
            ChannelFuture future = b.connect().sync();
            channel = future.channel();
            future.channel().close().sync();
        } catch (InterruptedException e) {
            log.fatal(e.getStackTrace());
        }finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                log.fatal(e.getStackTrace());
            }
        }

    }

    public LobbyClient(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
}
