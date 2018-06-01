package ygo.loby.controller;

import ygo.comn.model.*;
import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.constant.YGOP;
import ygo.comn.controller.AbstractController;
import ygo.comn.util.CommonLog;
import io.netty.channel.Channel;

import java.util.*;

/**
 * 大厅控制器
 * 处理来自玩家在游戏大厅内的请求
 *
 * @author Egan
 * @date 2018/5/14 21:32
 **/
public class LobbyController extends AbstractController {

    LobbyController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {
        switch (packet.getType()){
            case GET_ROOMS: getRooms(); break;
            case CREATE: createRoom(); break;
            case JOIN: joinRoom(); break;
            default:
                channel.writeAndFlush(
                        new DataPacket(
                                new ResponseStatus(
                                        StatusCode.COMMUNICATION_ERROR,
                                        "Unsupported Type")));
                CommonLog.log.
                        error("The type \""+packet.getType() + "\" is unsupported in Lobby-Controller\n");
        }
    }

    /**
     * 获取房间列表
     *
     * @date 2018/5/22 13:22
     * @param
     * @return void
     **/
    private void getRooms(){
        String lobbyStr = new String(gson.toJson(Lobby.getLobby()).getBytes(), YGOP.CHARSET);

        channel.writeAndFlush(new DataPacket(lobbyStr, MessageType.GET_ROOMS));

    }

    /**
     * 创建房间
     *
     * @date 2018/5/22 13:25
     * @param
     * @return void
     **/
    private synchronized void createRoom(){


        try{

            if(lobby.size() >= lobby.MAX_ROOMS){
                channel.writeAndFlush( new DataPacket(
                        new ResponseStatus(StatusCode.FULL_LOBBY)));
                CommonLog.log.error(StatusCode.FULL_LOBBY);
                channel.close();
            }

            //获取房间
            Room room = gson.fromJson(packet.getBody(), Room.class);

            //分配ip和端口
            Player host = room.getHost();
            host.setIp(address.getHostString());
            host.setPort(address.getPort());

            //分配房间ID
            int id = 0;
            while (id < lobby.size() && id == lobby.getRoomByIndex(id).getId()-1){id++;};
            room.setId(id + 1);

            //记录通道和房间
            host.setChannel(channel);
            if(!lobby.addRoom(room)){
                packet = new DataPacket(
                        new ResponseStatus(StatusCode.COMMUNICATION_ERROR));
                channel.writeAndFlush(packet);
                return;
            }

            //向客户端发送新的房间ID
            channel.writeAndFlush(new DataPacket(
                    gson.toJson(room.getId()), MessageType.CREATE)
            );

        }catch (Exception e){
            CommonLog.log.error(e + " in createRoom() of Lobby-Controller\n");
        }

    }

    /**
     * 加入房间
     *
     * @date 2018/5/22 13:25
     * @param
     * @return void
     **/
    private synchronized void joinRoom(){

        Map map = new HashMap<>();

        map = gson.fromJson(packet.getBody(), map.getClass());

        //解析消息体
        int id = gson.fromJson(map.get("id").toString(), Integer.class);
        Player guest = gson.fromJson(map.get("gs").toString(), Player.class);
        String pw = map.get("pw").toString();

        Room room = lobby.getRoomById(id);

        //判断是否满足加入房间的条件
        if(room == null){
            packet = new DataPacket(
                    new ResponseStatus(StatusCode.DISMISSED)
            );
            channel.writeAndFlush(packet);
            return;
        }else if(room.getGuest() != null){
            packet = new DataPacket(
                    new ResponseStatus(StatusCode.FULL_ROOM)
            );
            channel.writeAndFlush(packet);
            return;
        }else if(room.isHasPwd() && !pw.equals(room.getPassword())){
            packet = new DataPacket(
                    new ResponseStatus(StatusCode.INCORRECT)
            );
            channel.writeAndFlush(packet);
            return;
        }

        //分配ip给房客
        guest.setIp(address.getHostString());
        guest.setPort(address.getPort());

        //记录房客通道和房客
        guest.setChannel(channel);
        if(!lobby.addGuest(room, guest)){
            packet = new DataPacket(
                    new ResponseStatus(StatusCode.COMMUNICATION_ERROR));
            channel.writeAndFlush(packet);
            return;
        }

        //向房客返回目标房间
        packet.setType(MessageType.JOIN);
        packet.setBody(gson.toJson(room));
        channel.writeAndFlush(packet);

        //通知房主新房客的信息
        packet.setBody(gson.toJson(guest));
        room.getHost().getChannel().writeAndFlush(packet);
    }
}
