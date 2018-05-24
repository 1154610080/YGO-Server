package ygo.loby.controller;

import com.google.gson.GsonBuilder;
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

            //分配ip和端口

            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();

            Player host = room.getHost();
            host.setIp(address.getHostString());
            host.setPort(address.getPort());

            //分配房间ID

            int id = 0;
            while (id < lobby.size() && id == lobby.getRoomByIndex(id).getId()-1){id++;};
            room.setId(id + 1);
            lobby.addRoom(id, room);

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

        CommonLog.log.info(map.get("id"));
        CommonLog.log.info(map.get("gs"));
        CommonLog.log.info(map.get("pw"));

        int id = gson.fromJson(map.get("id").toString(), Integer.class);
        Player guest = gson.fromJson(map.get("gs").toString(), Player.class);
        String pw = map.get("pw").toString();

        Room room = GameLobby.getLobby().getRoomById(id);

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

        room.setGuest(guest);

        DataPacket packet = new DataPacket(
                gson.toJson(room), MessageType.JOIN);

        channel.writeAndFlush(packet);
    }
}
