package ygo.loby.server;

import ygo.comn.constant.MessageType;
import ygo.comn.constant.YGOP;
import ygo.comn.model.Room;
import ygo.comn.model.RoomRecord;
import ygo.loby.controller.ChiefController;
import ygo.comn.model.DataPacket;
import ygo.comn.model.ResponseStatus;
import ygo.comn.constant.StatusCode;
import ygo.comn.util.CommonLog;
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

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        CommonLog.log.info(address.getHostString() + " has the inbound.");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        CommonLog.log.info(address.getHostString() + " has the outbound.");

        //如果在房间记录里存在玩家的地址键，说明玩家掉线
        Room room = RoomRecord.getRecord().get(address);
        if(room != null){
            DataPacket packet = new DataPacket("", MessageType.EXIT);
            //判断是哪一位玩家掉线
            if(new InetSocketAddress(room.getHost().getIp(), room.getHost().getPort())
                    .equals(address)){
                room.getGuest().getChannel().writeAndFlush(packet);
                CommonLog.log.warn(new String
                        (("the player " + room.getHost().getName() + " has lost the connection")
                                .getBytes(), YGOP.CHARSET));
            }else {
                room.getHost().getChannel().writeAndFlush(packet);
                CommonLog.log.warn(new String
                        (("the player " + room.getGuest().getName() + " has lost the connection")
                                .getBytes(), YGOP.CHARSET));
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof DataPacket){
            DataPacket packet = (DataPacket)msg;
            CommonLog.log.info(
                    new String((packet.getType() +  " : " + packet.getBody()).getBytes(), YGOP.CHARSET));
            new ChiefController(packet, ctx.channel());
        }else {
            CommonLog.log.error("The message isn't a Data Packet");
            ctx.writeAndFlush(new DataPacket(new ResponseStatus(StatusCode.COMMUNICATION_ERROR)));
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(new ResponseStatus(StatusCode.INTERNAL_SERVER_ERROR, "msg"));
        CommonLog.log.error(cause + " in Lobby-Server-Handler");
        ctx.close();
    }


}
