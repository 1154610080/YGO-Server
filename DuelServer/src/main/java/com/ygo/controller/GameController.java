package com.ygo.controller;

import com.ygo.model.DataPacket;
import io.netty.channel.Channel;

/**
 * 游戏控制器
 * 处理游戏开始后的消息
 *
 * @author Egan
 * @date 2018/5/20 22:36
 **/
public class GameController extends AbstractController{

    public GameController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {
        super.channel.closeFuture();
    }

    /**
     * 发送聊天消息
     *
     * @date 2018/5/20 23:15
     * @param
     * @return void
     **/
    private void chat(){

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
