package ygo.loby.controller;


import ygo.loby.model.GameLobby;
import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.constant.YGOP;
import ygo.comn.controller.AbstractController;
import ygo.comn.model.DataPacket;
import ygo.comn.model.Player;
import ygo.comn.model.ResponseStatus;
import ygo.comn.model.Room;
import ygo.comn.util.CommonLog;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.List;

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
                        error("The type \""+packet.getType() + "\" is unsupported in Lobby-Controller");
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

        String lobbyStr = new String(wrapper.toJson(GameLobby.getLobby()), YGOP.CHARSET);

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
            List<Room> rooms = GameLobby.getLobby().getRooms();

            if(rooms.size() >= GameLobby.getLobby().getMAXIMUM()){
                channel.writeAndFlush( new DataPacket(
                        new ResponseStatus(StatusCode.FULL_LOBBY)));
                CommonLog.log.error(StatusCode.FULL_LOBBY);
            }

            //获取房间
            Room room = wrapper.toObject(packet.getBody(), Room.class);

            //分配ip和端口

            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();

            Player host = room.getHost();
            host.setIp(address.getAddress().toString());
            host.setPort(address.getPort());

            //分配房间ID

            int id = 0;
            while (id < rooms.size() && id == rooms.get(id).getId()-1){id++;};
            room.setId(id + 1);
            rooms.add(id, room);

            //向客户端发送新的房间ID
            channel.writeAndFlush(new DataPacket(
                    wrapper.toJsonStr(room.getId()), MessageType.CREATE)
            );

        }catch (Exception e){
            CommonLog.log.error(e + " in creatRoom() of Lobby-Controller");
        }

    }

    /**
     * 加入房间
     *
     * @date 2018/5/22 13:25
     * @param
     * @return void
     **/
    private void joinRoom(){

    }
}
