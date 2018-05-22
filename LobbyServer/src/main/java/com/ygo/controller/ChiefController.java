package com.ygo.controller;

import com.ygo.constant.MessageType;
import com.ygo.constant.StatusCode;
import com.ygo.model.DataPacket;
import com.ygo.model.ResponseStatus;
import io.netty.channel.Channel;

public class ChiefController extends AbstractController{

    public ChiefController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

        int type = packet.getType().getCode();

        if(type >= MessageType.GET_ROOMS.getCode() && type <= MessageType.JOIN.getCode())
            new LobbyController(packet, channel);
        else if(type >= MessageType.LEAVE.getCode() && type <= MessageType.COUNT_DOWN.getCode())
            new RoomController(packet, channel);
        else {
            channel.writeAndFlush(
                    new ResponseStatus(StatusCode.COMMUNICATION_ERROR, "Unsupported Type"));
        }
    }
}
