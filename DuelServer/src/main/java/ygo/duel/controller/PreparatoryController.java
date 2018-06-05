package ygo.duel.controller;

import io.netty.channel.Channel;
import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.controller.AbstractController;
import ygo.comn.model.DataPacket;
import ygo.comn.model.Player;
import ygo.comn.model.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 准备控制器
 * 处理游戏开始前的准备工作
 *
 * @author Egan
 * @date 2018/6/2 10:56
 **/
public class PreparatoryController extends AbstractController{

    PreparatoryController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {
        switch (packet.getType()) {
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
            default:
                log.error(StatusCode.ERROR_CONTROLLER, "准备控制器不能处理该消息类型 " + packet.getType());
                packet.setStatusCode(StatusCode.ERROR_CONTROLLER);
                channel.writeAndFlush(packet);
                break;
        }
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

        Room room = lobby.getRoomById(id);

        if(room == null){
            packet.setStatusCode(StatusCode.DISMISSED);
            channel.writeAndFlush(packet);
            return;
        }
        //记录通道
        lobby.addChannel(address, channel);

        Player player = isHost ? room.getHost() : room.getGuest();

        player.setIp(address.getHostString());
        player.setPort(address.getPort());

        lobby.updateRoom(room);

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

        lobby.updateRoom(room);
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

        lobby.updateRoom(room);
    }
}
