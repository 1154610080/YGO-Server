package ygo.duel.controller;

import ygo.comn.controller.AbstractController;
import ygo.comn.model.DataPacket;
import io.netty.channel.Channel;

/**
 * 游戏控制器
 * 处理游戏开始后的消息
 *
 * @author Egan
 * @date 2018/5/20 22:36
 **/
public class GameController extends AbstractController {

    GameController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign(){



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
     * 玩家退出游戏
     *
     * @date 2018/5/20 23:16
     * @param
     * @return void
     **/
    private void exit(){

    }
}
