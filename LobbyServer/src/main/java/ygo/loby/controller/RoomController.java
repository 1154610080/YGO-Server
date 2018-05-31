package ygo.loby.controller;

import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.constant.YGOP;
import ygo.comn.controller.AbstractController;
import ygo.comn.model.*;
import io.netty.channel.Channel;
import ygo.comn.util.CommonLog;

import java.util.Timer;
import java.util.TimerTask;

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
            case READY:
                ready(); break;
            case STARTED:
                start(); break;
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
        if(!lobby.removeAndInform(address))
            CommonLog.log.error("Unexpected Error: The player("
                    + address.getHostString() + ":" + address.getPort()
            +") can't leave the room cause he not in here.");
    }

    /**
     * 房主进入/取消开始状态
     *
     * @date 2018/5/31 16:10
     * @param
     * @return void
     **/
    private void start(){
        Room room = lobby.getRoomByAddress(address);
        if(room != null && room.getHost() != null
                && channel.equals(room.getHost().getChannel())){
            synchronized (room){
                //房主改变开始状态
                //如果房客未准备，警告房主
                if (room.getGuest() == null || !room.getGuest().isPrepared()){
                    channel.writeAndFlush(new DataPacket(new ResponseStatus(StatusCode.UNPREPARED)));
                    return;
                }
                Player host = room.getHost();
                host.setStarting(!host.isStarting());
                packet.setType(MessageType.STARTED);
                channel.writeAndFlush(packet);
                room.getGuest().getChannel().writeAndFlush(packet);

                if(host.isStarting()){
                    //如果进入开始状态，开始倒计时
                    countDown();
                }else if(room.timer != null){
                    //否则取消倒计时，取消房客的准备状态
                    room.timer.cancel();
                    room.getGuest().setPrepared(false);
                }
            }
        }else {
            CommonLog.log.error("The Room maybe is null or host is null or channel is not match.");
        }

    }

    /**
     * 房客进入/取消准备状态
     *
     * @date 2018/5/31 16:10
     * @param
     * @return void
     **/
    private void ready(){
        Room room = lobby.getRoomByAddress(address);
        if(room != null && room.getGuest() != null && channel.equals(room.getGuest().getChannel())){
            synchronized (room){
                //房客改变准备状态
                Player guest = room.getGuest();
                guest.setPrepared(!guest.isPrepared());
                //通知房主和房客
                channel.writeAndFlush(packet);
                room.getHost().getChannel().writeAndFlush(packet);

                if(!guest.isPrepared()){
                    //如果取消准备状态，取消倒计时和房主的开始状态
                    countDown();
                    room.getHost().setStarting(false);
                }
            }
        }else {
            CommonLog.log.error("Unexpected Error: The Room maybe is null or guest is null or channel is not match.");
        }
    }

    private int remaining = YGOP.COUNTDOWN_SECONDS;

    /**
     * 开始倒计时
     *
     * @date 2018/5/31 16:27
     * @param
     * @return void
     **/
    private void countDown(){

        Room room = lobby.getRoomByAddress(address);

        if(room == null || room.getHost()==null || room.getGuest() == null){
            CommonLog.log.error("Unexpected Error: The room is not full when countdown.");
            return;
        }

        Channel hChannel = room.getHost().getChannel();
        Channel gChannel = room.getGuest().getChannel();

        DataPacket packet = new DataPacket("", MessageType.COUNT_DOWN);

        room.timer = new Timer();
        room.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                CommonLog.log.info("Room will start game(" + remaining +"s)");
                packet.setBody(String.valueOf(remaining--));
                hChannel.writeAndFlush(packet);
                gChannel.writeAndFlush(packet);
                //倒计时结束
                if(remaining < 0){
                    room.setPlaying(true);
                    room.timer.cancel();
                }
            }
        }, 0, 1000);
    }
}
