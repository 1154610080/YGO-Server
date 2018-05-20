package com.ygo.controller;

import com.ygo.constant.MessageType;
import com.ygo.constant.StatusCode;
import com.ygo.model.DataPacket;
import com.ygo.model.Lobby;
import com.ygo.model.ResponseStatus;
import io.netty.channel.Channel;

/**
 * 房间出入控制器
 * 用于处理玩家出入房间的消息
 *
 * @author Egan
 * @date 2018/5/20 22:57
 **/
public class IOboundController extends AbstractController{

    IOboundController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {

        if(Lobby.getChannel() == null){
            channel.writeAndFlush(
                    new DataPacket(new ResponseStatus(StatusCode.INTERNAL_SERVER_ERROR,
                    "游戏大厅服务器维护中"), MessageType.WARING));
            return;
        }

        Lobby.getChannel().writeAndFlush(new DataPacket("", MessageType.REQUIRED_ROOMS));

        switch (packet.getType()){
            case CREATE:
                createRoom();
                break;
            case JOIN:
                joinRoom();
                break;
            case LEAVE:
                leaveRoom();
                break;
            case KICK_OUT:
                kickOut();
                break;
            default:
                channel.writeAndFlush(
                        new ResponseStatus(
                                StatusCode.COMMUNICATION_ERROR, "Nonexistent Type"));
                channel.closeFuture();
        }
    }

    /**
     * 创建房间
     *
     * @date 2018/5/20 22:41
     * @param
     * @return void
     **/
    private void createRoom(){

    }

    /**
     * 加入房间
     *
     * @date 2018/5/20 22:41
     * @param
     * @return void
     **/
    private void joinRoom(){

    }

    /**
     * 离开房间
     *
     * @date 2018/5/20 22:42
     * @param
     * @return void
     **/
    private void leaveRoom(){

    }

    /**
     * 踢出房间
     *
     * @date 2018/5/20 22:53
     * @param
     * @return void
     **/
    private void kickOut(){

    }

}
