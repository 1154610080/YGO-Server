package com.ygo.controller;

import com.ygo.model.DataPacket;
import io.netty.channel.Channel;

/**
 * 房间状态控制器
 * 处理房间状态变化的消息
 *
 * @author Egan
 * @date 2018/5/20 22:10
 **/
public class RoomStatusController extends AbstractController {

    RoomStatusController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

    }


    /**
     * 开始状态改变
     *
     * @date 2018/5/20 22:43
     * @param
     * @return void
     **/
    private void changeStartedStatus(){

    }

    /**
     * 准备状态改变
     *
     * @date 2018/5/20 23:02
     * @param
     * @return void
     **/
    private void changePreparedStatus(){

    }

    /**
     * 开始倒计时
     *
     * @date 2018/5/20 23:06
     * @param
     * @return void
     **/
    private void countdown(){

    }

}
