package com.ygo.controller;

import com.ygo.constant.MessageType;
import com.ygo.constant.StatusCode;
import com.ygo.constant.YGOP;
import com.ygo.model.*;
import com.ygo.util.CommonLog;
import com.ygo.util.GsonWrapper;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.List;

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
            CommonLog.log.error("The lobby is null");
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
    private synchronized void createRoom(){

        try{



            GsonWrapper gson = new GsonWrapper();

            Room room = gson.toObject(packet.getBody(), Room.class);
            if(room == null || room.getHost() == null)
            {
                channel.writeAndFlush(
                        new DataPacket("房间信息不全", MessageType.WARING)
                );
                return;
            }

            //分配ID
            List<Room> rooms = Lobby.getRooms();
            int id = 0;
            System.out.println(rooms.size());
            for (; id < rooms.size() && id == rooms.get(id).getId()-1; id++ ){
                System.out.println(id);
            };
            room.setId(id + 1);

            //分配ip地址和端口号
            Player host = room.getHost();
            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            host.setIp(address.getHostName());
            host.setPort(address.getPort());

            Lobby.getChannel().writeAndFlush(
                    new DataPacket(new String(gson.toJson(room), YGOP.CHARSET), MessageType.CREATE)
            );
        }catch(Exception e){
            CommonLog.log.error(e);
        }

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
