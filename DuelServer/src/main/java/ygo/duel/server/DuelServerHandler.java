package ygo.duel.server;

import ygo.comn.constant.MessageType;
import ygo.comn.constant.YGOP;
import ygo.duel.controller.ChiefController;
import ygo.comn.model.DataPacket;
import ygo.duel.model.Lobby;
import ygo.comn.util.CommonLog;
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

        InetSocketAddress channelAddress = (InetSocketAddress) ctx.channel().remoteAddress();

        CommonLog.log.info(channelAddress.getAddress() + ":" +channelAddress.getPort() + " has come.");

        //如果连接来自大厅服务器，保存通道
        if(Lobby.getChannel() == null
                && YGOP.LOBBY_SERVER_ADDR.getAddress().equals(channelAddress.getAddress())){
            Lobby.setChannel(ctx.channel());
            CommonLog.log.info("Lobby-Server has connected successfully");
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
        InetSocketAddress channelAddress = (InetSocketAddress) ctx.channel().remoteAddress();

        CommonLog.log.info(channelAddress.getAddress() + ":" +channelAddress.getPort() + " has out.");
    }

    /**
     * 处理接收到的消息
     * @date 2018/4/16
     * @return
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DataPacket packet = (DataPacket)msg;

        CommonLog.log.info(new String(packet.getBody().getBytes(), YGOP.CHARSET));
        ctx.channel().write(new DataPacket("Hello, I'm Duel-Server.", MessageType.CHAT));
        ctx.channel().write(new DataPacket("Hello again, can you parse it?", MessageType.CHAT));
        ChiefController chief = new ChiefController(packet, ctx.channel());
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
