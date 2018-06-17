package ygo.duel.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import ygo.comn.model.DataPacket;
import ygo.duel.controller.LobbyController;

/**
 * 大厅客户端处理器
 * 
 * @author Egan
 * @date 2018/6/18 1:41
 **/
public class LobbyServerHandler extends SimpleChannelInboundHandler<DataPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataPacket packet) throws Exception {
        new LobbyController(packet, channelHandlerContext.channel());
    }
}
