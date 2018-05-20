package com.ygo.controller;

import com.ygo.constant.MessageType;
import com.ygo.constant.StatusCode;
import com.ygo.model.DataPacket;
import com.ygo.model.ResponseStatus;
import io.netty.channel.Channel;

/**
 * 主控制器
 *
 * @author Egan
 * @date 2018/5/20 23:04
 **/
public class ChiefController extends AbstractController{

    ChiefController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

        int type = packet.getType().getCode();

        if(type >= MessageType.CREATE.getCode() && type <= MessageType.KICK_OUT.getCode())

            new IOboundController(packet, channel);

        else if(type <= MessageType.COUNT_DOWN.getCode())

            new RoomStatusController(packet, channel);

        else if(type <= MessageType.FINGER_GUESS.getCode())

            new PreparatoryController(packet, channel);

        else if (type <= MessageType.EXIT.getCode())

            new GameController(packet, channel);

        else{
            channel.writeAndFlush(new ResponseStatus(StatusCode.COMMUNICATION_ERROR));
            channel.closeFuture();
        }


    }
}
