package ygo.duel.controller;

import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.controller.AbstractController;
import ygo.comn.controller.RedisClient;
import ygo.comn.model.DataPacket;
import io.netty.channel.Channel;
import ygo.comn.model.Player;
import ygo.comn.model.Room;

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

        redisClient = RedisClient.getRedisForDuel();
        room = redisClient.getRoomByAddress(address);

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
                redisClient.removeAndInform(address);
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
    private void joinGame(boolean isHost){

        //解析消息体
        int id = Integer.parseInt(packet.getBody());

        Room room = redisClient.getRoomById(id);

        if(room == null){
            packet.setStatusCode(StatusCode.DISMISSED);
            channel.writeAndFlush(packet);
            return;
        }
        //记录通道
        redisClient.addChannel(address, channel);

        Player player = isHost ? room.getHost() : room.getGuest();

        player.setIp(address.getHostString());
        player.setPort(address.getPort());

        redisClient.updateRoom(room);

        packet = new DataPacket("", MessageType.JOIN);
        channel.writeAndFlush(packet);

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
        if (room.isHost(address)) {
            room.getHost().setDeck(deck);
        } else {
            room.getGuest().setDeck(deck);
        }

        redisClient.updateRoom(room);

        //交换卡组
        if(isAllConnect()){
            Player host = room.getHost();
            Player guest = room.getGuest();

            DataPacket hostDeckPacket = new DataPacket(gson.toJson(host.getDeck()), MessageType.DECK);
            DataPacket guestDeckPacket = new DataPacket(gson.toJson(guest.getDeck()), MessageType.DECK);

            redisClient.getChannel(host.getAddress()).writeAndFlush(guestDeckPacket);
            redisClient.getChannel(guest.getAddress()).writeAndFlush(hostDeckPacket);
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

        int finger = Integer.parseInt(packet.getBody());
        if (room.isHost(address)) {
            room.getHost().setFinger(finger);
        }else {
            room.getGuest().setFinger(finger);
        }

        redisClient.updateRoom(room);

        if (isAllConnect()){
            Player host = room.getHost();
            Player guest = room.getGuest();

            DataPacket fail = new DataPacket("0", MessageType.FINGER_GUESS);
            DataPacket win = new DataPacket("1", MessageType.FINGER_GUESS);

            //分出胜负，房客是否胜利
            boolean result = guest.getFinger() % 3 + 1 == host.getFinger();

            redisClient.getChannel(host.getAddress()).writeAndFlush(result ? fail : win);
            redisClient.getChannel(guest.getAddress()).writeAndFlush(result ? win : fail);
        }

    }

    private boolean isAllConnect(){
        return room.equals(redisClient.getRoomByAddress(room.getHost().getAddress()))
                && room.equals(redisClient.getRoomByAddress(room.getHost().getAddress()));
    }

}
