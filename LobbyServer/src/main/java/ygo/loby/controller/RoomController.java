package ygo.loby.controller;

import ygo.comn.constant.StatusCode;
import ygo.comn.controller.AbstractController;
import ygo.comn.model.DataPacket;
import io.netty.channel.Channel;
import ygo.comn.model.ResponseStatus;
import ygo.comn.util.CommonLog;

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
            case CHAT: chat(); break;
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

}
