package ygo.loby.controller;

import com.google.gson.GsonBuilder;
import ygo.comn.model.*;
import ygo.loby.model.GameLobby;
import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.constant.YGOP;
import ygo.comn.controller.AbstractController;
import ygo.comn.util.CommonLog;
import io.netty.channel.Channel;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

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
        gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        assign();

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

        String lobbyStr = new String(gson.toJson(GameLobby.getLobby()).getBytes(), YGOP.CHARSET);

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
            GameLobby lobby = GameLobby.getLobby();

            if(lobby.size() >= lobby.getMAXIMUM()){
                channel.writeAndFlush( new DataPacket(
                        new ResponseStatus(StatusCode.FULL_LOBBY)));
                CommonLog.log.error(StatusCode.FULL_LOBBY);
                channel.close();
            }

            //获取房间
            Room room = gson.fromJson(packet.getBody(), Room.class);

            //分配ip和端口，并在房间记录中记录玩家
            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            Player host = room.getHost();
            host.setIp(address.getHostString());
            host.setPort(address.getPort());
            RoomRecord.getRecord().put(address, room);

            //分配房间ID
            int id = 0;
            while (id < lobby.size() && id == lobby.getRoomByIndex(id).getId()-1){id++;};
            room.setId(id + 1);
            lobby.addRoom(id, room);

            //记录通道
            host.setChannel(channel);

            //记录房主
            RoomRecord.getRecord().put(address, room);

            //向客户端发送新的房间ID
            channel.writeAndFlush(new DataPacket(
                    gson.toJson(room.getId()), MessageType.CREATE)
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
    private synchronized void joinRoom(){

        Map map = new HashMap<>();

        map = gson.fromJson(packet.getBody(), map.getClass());

        int id = gson.fromJson(map.get("id").toString(), Integer.class);
        Player guest = gson.fromJson(map.get("gs").toString(), Player.class);
        String pw = map.get("pw").toString();

        Room room = GameLobby.getLobby().getRoomById(id);

        //判断是否满足加入房间的条件
        if(room == null){
            DataPacket packet = new DataPacket(
                    new ResponseStatus(StatusCode.DISMISSED)
            );
            channel.writeAndFlush(packet);
            return;
        }else if(room.getGuest() != null){
            DataPacket packet = new DataPacket(
                    new ResponseStatus(StatusCode.FULL_ROOM)
            );
            channel.writeAndFlush(packet);
            return;
        }else if(room.isHasPwd() && !pw.equals(room.getPassword())){
            DataPacket packet = new DataPacket(
                    new ResponseStatus(StatusCode.INCORRECT)
            );
            channel.writeAndFlush(packet);
            return;
        }

        //分配ip给房客
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        guest.setIp(address.getHostString());
        guest.setPort(address.getPort());
        RoomRecord.getRecord().put(address, room);

        //记录房客通道和房客
        guest.setChannel(channel);
        room.setGuest(guest);

        //向房客返回目标房间
        DataPacket packet = new DataPacket(
                gson.toJson(room), MessageType.JOIN);
        channel.writeAndFlush(packet);

        //通知房主新房客的信息
        packet = new DataPacket(
                gson.toJson(guest), MessageType.JOIN);
        room.getHost().getChannel().writeAndFlush(packet);
    }
}
