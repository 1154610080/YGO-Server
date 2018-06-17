package ygo.duel.controller;

import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.controller.AbstractController;
import ygo.comn.controller.redis.RedisClient;
import ygo.comn.controller.redis.RedisFactory;
import ygo.comn.model.DataPacket;
import io.netty.channel.Channel;
import ygo.comn.model.GlobalMap;
import ygo.comn.model.Player;
import ygo.comn.model.Room;
import ygo.comn.util.YgoLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 游戏控制器
 * 处理游戏开始后的消息
 *
 * @author Egan
 * @date 2018/5/20 22:36
 **/
public class GameController extends AbstractController {

    public GameController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign(){

        redis = RedisFactory.getRedisforDuel(address);
        room = redis.getRoomByAddress(address);

        log = new YgoLog("Game-Controller");

        //检查房间完整性
        if(packet.getType() != MessageType.JOIN && packet.getType() != MessageType.CREATE && room == null){
            log.error(StatusCode.ILLEGAL_DATA, "玩家已加入房间，但无法找到对应记录");
            packet.setStatusCode(StatusCode.ILLEGAL_DATA);
            channel.writeAndFlush(packet);
            return;
        }

        switch (packet.getType()) {
            case TEST:
                break;
            case LEAVE:
                redis.removeAndInform(address);
                break;
            case CREATE:
                joinGame(true);
                break;
            case JOIN:
                joinGame(false);
                break;
            case DECK:
                sendDeck();
                break;
            case FINGER_GUESS:
                fingerGuess();
                break;
            case CHAT:
                chat();
                break;
            case OPERATE:
                operate();
                break;
            default:
                log.error(StatusCode.ERROR_CONTROLLER, "游戏控制器不能处理该消息类型 " + packet.getType());
                packet.setStatusCode(StatusCode.ERROR_CONTROLLER);
                channel.writeAndFlush(packet);
                break;
        }
    }

    /**
     * 发送操作消息
     *
     * @date 2018/5/20 23:16
     * @param
     * @return void
     **/
    private void operate(){

    }

    /**
     * 玩家主机成功连接本服务器
     *
     * @date 2018/6/2 11:02
     * @param isHost 是否为房主
     * @return void
     **/
    private synchronized void joinGame(boolean isHost){

        //解析消息体
        int id = Integer.parseInt(packet.getBody());

        Room room = redis.getRoomById(id);

        if(room == null){
            packet.setStatusCode(StatusCode.DISMISSED);
            channel.writeAndFlush(packet);
            return;
        }

        //log.info(StatusCode.OUTBOUND, "The room before diy: hs" + room.getHost().getAddress() + " gs" + room.getGuest().getAddress());

        //记录通道
        GlobalMap.addChannel(address, channel);

        Player player = isHost ? room.getHost() : room.getGuest();

        player.setIp(address.getHostString());
        player.setPort(address.getPort());

        synchronized (RedisClient.class){
            room = redis.getRoomById(id);
            if(isHost){
                room.setHost(player);
            }else {
                room.setGuest(player);
            }

            redis.addPlayer(room, player);
        }


        //log.info(StatusCode.OUTBOUND, "The room after diy: hs" + room.getHost().getAddress() + " gs" + room.getGuest().getAddress());

        packet = new DataPacket("", MessageType.JOIN);
        channel.writeAndFlush(packet);

        //如果双方都已连接，通知双方
        if(isAllConnect()){
            packet.setType(MessageType.CREATE);
            broadcast(packet);
        }
    }

    /**
     * 玩家发送卡牌
     *
     * @date 2018/6/2 11:02
     * @param
     * @return void
     **/
    private void sendDeck(){
        List deck = new ArrayList<>();
        deck = gson.fromJson(packet.getBody(), deck.getClass());

        //对方未连接
        if(!isAllConnect()){
            packet.setStatusCode(StatusCode.INCOMPLETE_ROOM);
            channel.writeAndFlush(packet);
            return;
        }

        packet = new DataPacket(gson.toJson(deck), MessageType.DECK);
        if (room.isHost(address)) {
            GlobalMap.getChannel(room.getGuest().getAddress()).writeAndFlush(packet);
        } else {
            GlobalMap.getChannel(room.getHost().getAddress()).writeAndFlush(packet);
        }

    }

    /**
     * 玩家出拳
     *
     * @date 2018/6/2 11:03
     * @param
     * @return void
     **/
    private void fingerGuess(){

        synchronized (RedisClient.class){
            int finger = Integer.parseInt(packet.getBody());
            if (room.isHost(address)) {
                room.getHost().setFinger(finger);
            }else {
                room.getGuest().setFinger(finger);
            }

            redis.updateRoom(room);

            if (isAllConnect()){
                Player host = room.getHost();
                Player guest = room.getGuest();

                if(host.getFinger() == 0 || guest.getFinger() == 0)
                    return;

                DataPacket fail = new DataPacket("-1", MessageType.FINGER_GUESS);
                DataPacket win = new DataPacket("1", MessageType.FINGER_GUESS);

                if(host.getFinger() == guest.getFinger()){
                    fail = win = new DataPacket("0", MessageType.FINGER_GUESS);
                    host.setFinger(0);
                    guest.setFinger(0);
                    redis.updateRoom(room);
                }

                //分出胜负，房客是否胜利
                boolean result = guest.getFinger() % 3 + 1 == host.getFinger();

                GlobalMap.getChannel(host.getAddress()).writeAndFlush(result ? fail : win);
                GlobalMap.getChannel(guest.getAddress()).writeAndFlush(result ? win : fail);
            }
        }




    }



}
