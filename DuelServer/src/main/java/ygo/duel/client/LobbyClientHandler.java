package ygo.duel.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ygo.comn.model.DataPacket;

/**
 * 大厅客户端处理器
 *
 * @author Egan
 * @date 2018/6/2 10:13
 **/
public class LobbyClientHandler extends SimpleChannelInboundHandler<DataPacket>{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataPacket dataPacket) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
