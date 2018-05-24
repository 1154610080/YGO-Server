package ygo.duel.controller;

import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.controller.AbstractController;
import ygo.comn.model.DataPacket;
import ygo.duel.model.Lobby;
import ygo.comn.model.ResponseStatus;
import ygo.comn.util.CommonLog;
import io.netty.channel.Channel;

/**
 * 主控制器
 *
 * @author Egan
 * @date 2018/5/20 23:04
 **/
public class ChiefController extends AbstractController {

    public ChiefController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign(){

        int type = packet.getType().getCode();


        if(type <= MessageType.FINGER_GUESS.getCode())

            new PreparatoryController(packet, channel);

        else if (type <= MessageType.EXIT.getCode())

            new GameController(packet, channel);

        else if (type == MessageType.WARING.getCode())

            CommonLog.log.info(packet.getBody());

        else{
            channel.writeAndFlush(
                    new DataPacket(new ResponseStatus(StatusCode.COMMUNICATION_ERROR,
                            "Nonexistent Type")));
            channel.close();
        }


    }
}
