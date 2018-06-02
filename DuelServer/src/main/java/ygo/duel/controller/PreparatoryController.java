package ygo.duel.controller;

import io.netty.channel.Channel;
import ygo.comn.controller.AbstractController;
import ygo.comn.model.DataPacket;

/**
 * 准备控制器
 * 处理游戏开始前的准备工作
 *
 * @author Egan
 * @date 2018/6/2 10:56
 **/
public class PreparatoryController extends AbstractController{

    public PreparatoryController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

    }

    /**
     * 玩家主机成功连接本服务器
     *
     * @date 2018/6/2 11:02
     * @param
     * @return void
     **/
    private void joinGame(){

    }

    /**
     * 玩家发送卡牌
     *
     * @date 2018/6/2 11:02
     * @param
     * @return void
     **/
    private void SendDeck(){

    }

    /**
     * 玩家出拳
     *
     * @date 2018/6/2 11:03
     * @param
     * @return void
     **/
    private void fingerGuess(){

    }
}
