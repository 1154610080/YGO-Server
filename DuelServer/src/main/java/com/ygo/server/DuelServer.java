package com.ygo.server;

import com.sun.net.httpserver.HttpServer;
import com.ygo.client.LDClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.InetSocketAddress;

/**
 * 服务端主类
 * @author EganChen
 * @date 2018/4/16 13:48
 */
public class DuelServer implements Runnable{

    private static Log log = LogFactory.getLog(HttpServer.class);
    private int port;

    public DuelServer(int port){
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();

        try{

            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new DuelServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)  //最大客户端连接数
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture f = b.bind().sync();
            log.info("Duel Server listening on " + port);
            f.channel().closeFuture().sync();
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

        DuelServer duel = new DuelServer(2333);
        LDClient ld = new LDClient("http://localhost/", 19208);

        Thread duelThread = new Thread(duel);
        Thread ldThread = new Thread(ld);

        duelThread.start();
        ldThread.start();
    }


}
