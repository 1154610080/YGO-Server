package com.ygo.controller;

import com.ygo.model.DataPacket;
import io.netty.channel.Channel;

/**
 * 游戏准备工作控制器
 *
 * @author Egan
 * @date 2018/5/20 23:09
 **/
public class PreparatoryController extends AbstractController{

    PreparatoryController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

    }

    /**
     * 验证文件完整性
     *
     * @date 2018/5/20 23:10
     * @param
     * @return void
     **/
    private void verityFiles(){

    }

    /**
     * 交换卡组
     *
     * @date 2018/5/20 23:13
     * @param
     * @return void
     **/
    private void exchangeDeck(){

    }

    /**
     * 猜拳
     *
     * @date 2018/5/20 23:11
     * @param
     * @return void
     **/
    private void fingerGuess(){

    }
}
