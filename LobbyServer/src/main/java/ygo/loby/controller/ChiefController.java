package ygo.loby.controller;

import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.controller.AbstractController;
import ygo.comn.model.DataPacket;
import ygo.comn.model.ResponseStatus;
import io.netty.channel.Channel;

public class ChiefController extends AbstractController {

    public ChiefController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

        int type = packet.getType().getCode();

        if(type >= MessageType.GET_ROOMS.getCode() && type <= MessageType.JOIN.getCode())
            new LobbyController(packet, channel);
        else if(type >= MessageType.LEAVE.getCode() && type <= MessageType.CHAT.getCode())
            new RoomController(packet, channel);
        else {
            channel.writeAndFlush(
                    new ResponseStatus(StatusCode.COMMUNICATION_ERROR, "Unsupported Type"));
        }
    }
}
