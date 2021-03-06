package ygo.loby.controller;

import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.controller.AbstractController;
import ygo.comn.model.DataPacket;
import io.netty.channel.Channel;
import ygo.comn.util.YgoLog;

public class ChiefController extends AbstractController {

    public ChiefController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

        log = new YgoLog("Chief-Controller");

        int type = packet.getType().getCode();

        if(type == MessageType.TEST.getCode())
            return;

        if(type >= MessageType.GET_ROOMS.getCode() && type <= MessageType.JOIN.getCode())
            new LobbyController(packet, channel);
        else if(type >= MessageType.LEAVE.getCode() && type <= MessageType.CHAT.getCode())
            new RoomController(packet, channel);
        else {
            log.error(StatusCode.ERROR_CONTROLLER, "主控制器不能处理该类型 " + packet.getType());
            packet.setStatusCode(StatusCode.ERROR_CONTROLLER);
            channel.writeAndFlush(packet);

        }
    }
}
