package ygo.duel.server;

import ygo.comn.util.CommonLog;
import ygo.comn.util.YGOPDecoder;
import ygo.comn.util.YGOPEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;

/**
 * 服务端主类
 * @author EganChen
 * @date 2018/4/16 13:48
 */
public class DuelServer{

    private int port;

    public DuelServer(int port){
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try{

            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new YGOPEncoder());
                            socketChannel.pipeline().addLast(new YGOPDecoder());
                            socketChannel.pipeline().addLast(new DuelServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)  //最大客户端连接数
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind().sync();
            CommonLog.log.info("Duel Server listening on " + port);
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        DuelServer server = new DuelServer(16384);
        server.start();
    }

    static {
        CommonLog.log = LogFactory.getLog("Duel-Server(TCP)");
    }
}
