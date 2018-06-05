package ygo.duel.controller;

import io.netty.channel.Channel;
import ygo.comn.controller.AbstractController;
import ygo.comn.model.DataPacket;

/**
 * 主控制器
 *
 * @author Egan
 * @date 2018/6/2 11:11
 **/
public class ChiefController extends AbstractController{


    public ChiefController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

        switch (packet.getType()) {
            case LEAVE:
                lobby.removeAndInform(address);
                break;
            case CREATE:
            case JOIN:
            case DECK:
            case FINGER_GUESS:
                new PreparatoryController(packet, channel);
                break;
            case CHAT:
            case OPERATE:
                new GameController(packet, channel);
            case WARING:
                break;
        }
    }

    /**
     * 双方完成准备工作后，进入决斗
     *
     * @date 2018/6/2 11:16
     * @param
     * @return void
     **/
    private void duel(){

    }
}
