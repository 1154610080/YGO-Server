package com.ygo.server;

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
import org.apache.http.protocol.HTTP;

import java.net.InetSocketAddress;

/**
 * 游戏大厅-决斗服务器
 * 游戏大厅服务器和决斗服务器之间的socket服务器
 *
 * @author Egan
 * @date 2018/5/12 9:34
 **/
public class LDServer implements Runnable{

    private int port;

    private static Log log = LogFactory.getLog(HTTP.class);

    public LDServer(int port){
        this.port = port;
    }

    @Override
    public void run(){
        LDServerHandler handler = new LDServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(handler);
                        }
                    }).option(ChannelOption.SO_BACKLOG, 1);
            ChannelFuture f = bootstrap.bind().sync();
            log.info("Lobby-Duel server listening on " + port);
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
}
