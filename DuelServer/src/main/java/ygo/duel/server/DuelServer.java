package ygo.duel.server;

import org.apache.commons.logging.Log;
import ygo.comn.constant.Secret;
import ygo.comn.constant.YGOP;
import ygo.comn.controller.IpFilterHandler;
import ygo.comn.util.YGOPDecoder;
import ygo.comn.util.YGOPEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.logging.LogFactory;
import ygo.duel.client.LobbyClient;

import java.net.InetSocketAddress;

/**
 * 服务端主类
 * @author EganChen
 * @date 2018/4/16 13:48
 */
public class DuelServer implements Runnable{

    private Log log = LogFactory.getLog("Duel-Server");

    private int port;

    public DuelServer(int port){
        this.port = port;
    }

    @Override
    public void run(){
        EventLoopGroup group = new NioEventLoopGroup();

        try{

            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel){
                            socketChannel.pipeline().addLast(new IpFilterHandler());
                            socketChannel.pipeline().addLast(new YGOPEncoder());
                            socketChannel.pipeline().addLast(new YGOPDecoder());
                            socketChannel.pipeline().addLast(new DuelServerHandler());
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind().sync();
            log.info(new String(("决斗服务器 正在监听端口 " + port + "...").getBytes(), YGOP.CHARSET));
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.fatal(e.getStackTrace());
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                log.fatal(e.getStackTrace());
            }
        }
    }

    public static void main(String[] args){

        DuelServer duelServer = new DuelServer(Secret.DUEL_PORT);
        LobbyClient lobbyClient = new LobbyClient(Secret.LOBBY_HOST, Secret.LOBBY_PORT );

        Thread duel = new Thread(duelServer);
        Thread lobby = new Thread(lobbyClient);

        duel.start();
        lobby.start();
    }
}
