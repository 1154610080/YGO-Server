package com.ygo.server;

import com.ygo.constant.MessageType;
import com.ygo.constant.YGOP;
import com.ygo.model.DataPacket;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

/**
 * 出入站事件处理类
 * @author EganChen
 * @date 2018/4/16 13:49
 */
@Sharable
public class DuelServerHandler extends ChannelInboundHandlerAdapter{

    /**
    * 处理入站事件
    * @date 2018/4/16
    * @return
    */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        System.out.println(ctx.channel().id() + " has come.");

        //如果连接来自大厅服务器，保存通道
        if(DuelServer.lobbyChannel == null
                && ((InetSocketAddress)ctx.channel().remoteAddress()).getAddress()
                == YGOP.LOBBY_SERVER_ADDR.getAddress()){
            DuelServer.lobbyChannel = ctx.channel();
        }
    }

    /**
    * 处理出站事件
    * @date 2018/4/16
    * @return
    */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        System.out.println(ctx.channel().id() + " has out.");
    }

    /**
     * 处理接收到的消息
     * @date 2018/4/16
     * @return
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DataPacket packet = (DataPacket)msg;
        System.out.println(packet.getVersion() + "  |  " + packet.getType().name() + "  |  " +
                packet.getMagic() + "  |  " + packet.getLen() + "  |  " + packet.getBody());
        ctx.channel().write(new DataPacket("Hello, I'm Duel-Server.", MessageType.CHAT));
        ctx.channel().write(new DataPacket("Hello again, can you parse it?", MessageType.CHAT));
    }

    /**
    * 将未决消息冲刷到远程节点，并关闭连接
    * @date 2018/4/16
    * @return
    */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        //ctx.close();
    }

    /**
    * 捕获并打印异常，关闭连接
    * @date 2018/4/16
    * @return
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
