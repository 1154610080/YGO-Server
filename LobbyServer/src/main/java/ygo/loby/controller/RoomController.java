package ygo.loby.controller;

import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.constant.YGOP;
import ygo.comn.controller.AbstractController;
import ygo.comn.controller.redis.RedisClient;
import ygo.comn.controller.redis.RedisFactory;
import ygo.comn.model.*;
import io.netty.channel.Channel;
import ygo.comn.util.YgoLog;

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

        redis = RedisFactory.getRedisforLobby(address);

        log = new YgoLog("RoomController");

        room = redis.getRoomByAddress(address);

        if(isNormalRoom()) return;

        switch (packet.getType()){
            case CHAT:
                chat(); break;
            case LEAVE:
                leave(); break;
            case READY:
                if(isMatchedChannel(room, false))
                    return;
                ready();
                break;
            case STARTED:
                if(isMatchedChannel(room, true))
                    return;
                if (room.getGuest() == null || !room.getGuest().isSP()){
                    packet.setStatusCode(StatusCode.UNPREPARED);
                    channel.writeAndFlush(packet);
                    return;
                }
                start();
                break;
            case KICK_OUT:
                if(isMatchedChannel(room, true))
                    return;
                kickOut();
                break;
            case FINGER_GUESS:
                boolean isHost = room.isHost(address);
                if(isMatchedChannel(room, isHost))
                    return;
                fingerGuess(isHost);
                break;
            default:
                log.error(StatusCode.ERROR_CONTROLLER, "房间控制器不能处理该消息类型 " + packet.getType());
                packet.setStatusCode(StatusCode.ERROR_CONTROLLER);
                channel.writeAndFlush(packet);
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
        if(!redis.removeAndInform(address))
            log.error(StatusCode.NOT_IN_HERE, "不能删除玩家记录，因为TA不在房间");
    }

    /**
     * 房主进入/取消开始状态
     *
     * @date 2018/5/31 16:10
     * @param
     * @return void
     **/
    private void start(){

        Player host = room.getHost();
        host.setSP(!host.isSP());
        redis.updateRoom(room);
        packet.setType(MessageType.STARTED);
        channel.writeAndFlush(packet);
        GlobalMap.getChannel(room.getGuest().getAddress()).writeAndFlush(packet);

        Timer timer = GlobalMap.getTimer(room.getId());

        if(host.isSP()){
            //如果进入开始状态，开始倒计时
            countDown();
        }else if(timer != null){
            //否则取消倒计时，取消房客的准备状态
            timer.cancel();
            room.getGuest().setSP(false);
            redis.updateRoom(room);
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
        //房客改变准备状态
        Player guest = room.getGuest();
        guest.setSP(!guest.isSP());
        redis.updateRoom(room);
        //通知房主和房客
        channel.writeAndFlush(packet);
        GlobalMap.getChannel(room.getHost().getAddress()).writeAndFlush(packet);

        if(!guest.isSP()){
            Timer timer = GlobalMap.getTimer(room.getId());
            //如果取消准备状态，取消倒计时和房主的开始状态
            if(timer!=null)
                timer.cancel();
            room.getHost().setSP(false);
            redis.updateRoom(room);
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

        Room room = this.room;

        Channel hChannel = GlobalMap.getChannel(room.getHost().getAddress());
        Channel gChannel = GlobalMap.getChannel(room.getGuest().getAddress());

        DataPacket packet = new DataPacket("", MessageType.COUNT_DOWN);

        Timer timer = new Timer();
        GlobalMap.addTimer(room.getId(), timer);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                //倒计时结束
                if(remaining == 0){
                    synchronized (RedisClient.class) {

                        room.setPlaying(true);
                        redis.updateRoom(room);
                        redis.removeRecord(room.getHost().getAddress());
                        redis.removeRecord(room.getGuest().getAddress());
                    }
                }

                packet.setBody(String.valueOf(remaining--));
                hChannel.writeAndFlush(packet);
                gChannel.writeAndFlush(packet);

                if(remaining < 0){
                    timer.cancel();
                    GlobalMap.removeTimer(room.getId());
                }

            }
        }, 0, 1000);
    }

    /**
     * 踢出房间
     *
     * @date 2018/6/1 11:25
     * @param
     * @return void
     **/
    private void kickOut()
    {
        Player guest = room.getGuest();
        if(guest != null){
            GlobalMap.getChannel(guest.getAddress()).writeAndFlush(packet);
            redis.removeGuest(guest);
        }
        channel.writeAndFlush(packet);

    }

    /**
     * 猜拳
     *
     * @date 2018/6/1 20:26
     * @param
     * @return void
     **/
    private void fingerGuess(boolean isHost){

        int finger = Integer.parseInt(packet.getBody(), 16);

        if (isHost) {
            room.getHost().setFinger(finger);
        } else {
            room.getGuest().setFinger(finger);
        }
    }
}
