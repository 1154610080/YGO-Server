package ygo.duel.server;

import jdk.nashorn.internal.objects.Global;
import org.apache.commons.logging.Log;
import ygo.comn.constant.Secret;
import ygo.comn.constant.YGOP;
import ygo.comn.controller.IpFilterHandler;
import ygo.comn.controller.RedisClient;
import ygo.comn.model.GlobalMap;
import ygo.comn.model.Room;
import ygo.comn.util.YGOPDecoder;
import ygo.comn.util.YGOPEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;
import java.util.Scanner;

/**
 * 服务端主类
 * @author EganChen
 * @date 2018/4/16 13:48
 */
public class DuelServer{

    private Log log = LogFactory.getLog("Duel-Server");

    private int port;

    public DuelServer(int port){
        this.port = port;
    }

    public void start() throws InterruptedException {
        InetSocketAddress localAddr = new InetSocketAddress(port);
        EventLoopGroup group = new NioEventLoopGroup();
        RedisClient redisClient = GlobalMap.getRedisforDuel(localAddr);
        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(localAddr)
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
            Scanner scanner = new Scanner(System.in);
            while (true){
                String str = scanner.nextLine();
                if("c".equals(str)){
                    redisClient.flush();
                    f.channel().closeFuture();
                    break;
                }if("cz".equals(str)){
                    System.out.println(GlobalMap.ChannelSize());
                }if("ck".equals(str)){
                    System.out.println(GlobalMap.getChannelKeys());
                }
            }
        } catch (Exception e) {
            log.fatal(e.getStackTrace());
        } finally {
            //删除所有未在进行游戏的房间和玩家
            redisClient.flush();
            redisClient.close();
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        DuelServer duelServer = new DuelServer(Secret.DUEL_PORT);

        duelServer.start();
    }
}
