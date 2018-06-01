package ygo.loby.server;

import ygo.comn.model.*;
import ygo.comn.util.YgoLog;
import ygo.loby.controller.ChiefController;
import ygo.comn.constant.StatusCode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;

/**
 * 大厅服务器处理器
 *
 * @author Egan
 * @date 2018/5/7 22:54
 **/
public class LobbyServerHandler extends ChannelInboundHandlerAdapter {

    private YgoLog log = new YgoLog("IOBound");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        log.info(StatusCode.INBOUND, address.getHostString() + ":" + address.getPort());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        log.info(StatusCode.OUTBOUND, address.getHostString() + ":" + address.getPort() );

        //检查玩家是否掉线
        if(Lobby.getLobby().removeAndInform(address)){
            log.warn(StatusCode.LOST_CONNECTION, address.getHostString() + ":" + address.getPort());
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DataPacket packet = (DataPacket)msg;
        new ChiefController(packet, ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(new DataPacket(new ResponseStatus(StatusCode.INTERNAL_SERVER_ERROR)));
        log.fatal(cause.toString());
        ctx.close();
    }

}
