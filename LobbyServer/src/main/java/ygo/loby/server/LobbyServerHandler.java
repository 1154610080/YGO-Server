package ygo.loby.server;

import io.netty.channel.SimpleChannelInboundHandler;
import ygo.comn.controller.RedisClient;
import ygo.comn.model.*;
import ygo.comn.util.YgoLog;
import ygo.loby.controller.ChiefController;
import ygo.comn.constant.StatusCode;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;

/**
 * 大厅服务器处理器
 *
 * @author Egan
 * @date 2018/5/7 22:54
 **/
public class LobbyServerHandler extends SimpleChannelInboundHandler<DataPacket> {

    private YgoLog log = new YgoLog("IOBound");

    private RedisClient redisClient = RedisClient.getRedisForLobby();

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
        if(redisClient.removeAndInform(address)){
            log.warn(StatusCode.LOST_CONNECTION, address.getHostString() + ":" + address.getPort());
        }
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, DataPacket packet){
        new ChiefController(packet, ctx.channel());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.writeAndFlush(new DataPacket(new ResponseStatus(StatusCode.INTERNAL_SERVER_ERROR)));
        log.fatal(cause.toString());
        ctx.close();
    }

}
