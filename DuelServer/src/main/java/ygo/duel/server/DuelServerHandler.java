package ygo.duel.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ygo.comn.constant.StatusCode;
import ygo.comn.model.DataPacket;
import ygo.comn.controller.RedisClient;
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

    RedisClient client = RedisClient.getRedisForDuel();

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
        if(client.removeAndInform(address)){
            log.warn(StatusCode.LOST_CONNECTION, address.getHostString() + ":" + address.getPort());
        }

        client.close();
    }


    @Override
    public void channelRead0(ChannelHandlerContext ctx, DataPacket packet){
        new GameController(packet, ctx.channel());
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        ctx.writeAndFlush(new DataPacket(new ResponseStatus(StatusCode.INTERNAL_SERVER_ERROR)));
        log.fatal(cause.toString());
        client.removeAndInform((InetSocketAddress) ctx.channel().remoteAddress());
        ctx.close();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        client.close();
    }
}
