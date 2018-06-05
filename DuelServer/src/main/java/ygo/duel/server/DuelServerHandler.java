package ygo.duel.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ygo.comn.constant.StatusCode;
import ygo.comn.model.DataPacket;
import ygo.comn.model.Lobby;
import ygo.comn.model.ResponseStatus;
import ygo.comn.util.YgoLog;
import ygo.duel.controller.GameController;


import java.net.InetSocketAddress;

/**
 * 决斗服务器处理器
 *
 * @author Egan
 * @date 2018/6/5 22:10
 **/
public class DuelServerHandler extends SimpleChannelInboundHandler<DataPacket> {

    private YgoLog log = new YgoLog("IOBound");

    @Override
    public void handlerAdded(ChannelHandlerContext ctx){
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        log.info(StatusCode.INBOUND, address.getHostString() + ":" + address.getPort());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx){
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        log.info(StatusCode.OUTBOUND, address.getHostString() + ":" + address.getPort() );

        //检查玩家是否掉线
        if(Lobby.getLobby().removeAndInform(address)){
            log.warn(StatusCode.LOST_CONNECTION, address.getHostString() + ":" + address.getPort());
        }
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, DataPacket packet){
        new GameController(packet, ctx.channel());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.writeAndFlush(new DataPacket(new ResponseStatus(StatusCode.INTERNAL_SERVER_ERROR)));
        log.fatal(cause.toString());
        ctx.close();
    }

}
