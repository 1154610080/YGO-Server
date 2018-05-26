package ygo.loby.controller;

import com.google.gson.GsonBuilder;
import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.controller.AbstractController;
import ygo.comn.model.*;
import io.netty.channel.Channel;
import ygo.comn.util.CommonLog;
import java.net.InetSocketAddress;
import java.util.Map;

/**
 * 房间控制器
 * 处理玩家在房间内的请求
 *
 * @author Egan
 * @date 2018/5/22 13:16
 **/
public class RoomController extends AbstractController{

    RoomController(DataPacket packet, Channel channel) {
        super(packet, channel);
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        assign();
    }

    @Override
    protected void assign() {

        switch (packet.getType()){
            case CHAT:
                chat(); break;
            default:
                channel.writeAndFlush(
                        new DataPacket(
                                new ResponseStatus(
                                        StatusCode.COMMUNICATION_ERROR,
                                        "Unsupported Type")));
                CommonLog.log.
                        error("The type \""+packet.getType() + "\" is unsupported in Room-Controller");
        }
    }


    private void leaveRoom()
    {

        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        Map<InetSocketAddress, Room> record = RoomRecord.getRecord();
        Room room = record.get(address);

        if(room != null) {

            DataPacket packet = new DataPacket("", MessageType.LEAVE);

            Player host = room.getHost();
            Player guest = room.getGuest();

            InetSocketAddress hostAddress =
                    host != null ?
                            new InetSocketAddress(host.getIp(), host.getPort()) : null;
            InetSocketAddress guestAddress =
                    guest != null ?
                            new InetSocketAddress(guest.getIp(), guest.getPort()) : null;

            //判断消息来源
            if (RoomRecord.isHost(address, room)) {
                //如果房主离开房间，解散房间，并通知房客
                if(guest != null)
                    guest.getChannel().writeAndFlush(packet);
                record.remove(hostAddress);
                record.remove(guestAddress);
            }else {
                if(host != null)
                    host.getChannel().writeAndFlush(packet);
                record.remove(guestAddress);
            }
        }
    }

}
