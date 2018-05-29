package ygo.loby.controller;

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
    }

    @Override
    protected void assign() {

        switch (packet.getType()){
            case CHAT:
                chat(); break;
            case LEAVE:
                leave(); break;
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


    /**
     * 离开房间
     *
     * @date 2018/5/29 16:58
     * @param
     * @return void
     **/
    private void leave()
    {
        Map<InetSocketAddress, Room> record = RoomRecord.getRecord();

        if(RoomRecord.getRecord().remove(address) == null)
            CommonLog.log.error("Unexpected Error: The player("
                    + address.getHostString() + ":" + address.getPort()
            +") can't leave the room cause he not in here.");
    }

    /**
     * 改变玩家状态(开始、准备)
     *
     * @date 2018/5/29 16:59
     * @param
     * @return void
     **/
    private void changeStatus(){
        Room room = RoomRecord.getRecord().get(address);
        if(room != null){
            DataPacket packet = new DataPacket("", MessageType.READY);
            synchronized (room){
                if(RoomRecord.isHost(address, room)){
                    //房主改变开始状态
                    //如果房客未准备，警告房主
                    if (!room.getGuest().isPrepared()){
                        channel.writeAndFlush(new DataPacket(new ResponseStatus(StatusCode.UNPREPARED)));
                        return;
                    }
                    Player host = room.getHost();
                    host.setStarting(!host.isStarting());
                    packet.setType(MessageType.STARTED);
                    channel.writeAndFlush(packet);
                    room.getGuest().getChannel().writeAndFlush(packet);
                }else {
                    //房客改变准备状态
                    Player guest = room.getGuest();
                    guest.setPrepared(!guest.isPrepared());
                    //通知房主和房客
                    channel.writeAndFlush(packet);
                    room.getHost().getChannel().writeAndFlush(packet);
                }
            }

        }
    }

}
