package ygo.comn.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import ygo.comn.constant.MessageType;
import ygo.comn.util.CommonLog;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 大厅类
 * 存储所有房间的数据
 *
 * @author Egan
 * @date 2018/5/17 23:36
 **/
public class Lobby{

    private static Lobby lobby =
            new Lobby();

    /**
     * 最大房间数
     **/
    @Expose()
    @SerializedName("mx")
    public final int MAX_ROOMS = 128;

    /**
     * 房间列表
     **/
    @Expose()
    @SerializedName("rm")
    private List<Room> rooms = new LinkedList<>();

    /**
     * 房间映射
     * id-房间映射
     **/
    private Map<Integer, Room> roomMap = new HashMap<>();

    /**
     * 房间记录
     * 地址-房间映射
     **/
    private Map<InetSocketAddress, Room> record = new HashMap<>();

    public int size(){
        return rooms.size();
    }

    public static Lobby getLobby() {
        return lobby;
    }

    public void updateRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    /**
     * 根据地址判断消息是否来自房主
     *
     * @date 2018/5/26 9:15
     * @param address 地址
	 * @param room 所在房间
     * @return boolean 是否来自房主
     **/
    public static boolean isHost(InetSocketAddress address, Room room){
        if(room.getHost() == null)
            return false;
        InetSocketAddress host =
                new InetSocketAddress(room.getHost().getIp(), room.getHost().getPort());
        return host.equals(address);
    }

    /**
     * 创建房间
     *
     * @date 2018/5/30 17:15
     * @param room 新房间
     * @return void
     **/
    public boolean addRoom(Room room){

        Player host = room.getHost();
        if (host == null){
            CommonLog.log.error("The room " + room.getName() +
                    " can't be created cause it lack the host.");
            return  false;
        }

        InetSocketAddress address = new InetSocketAddress(host.getIp(), host.getPort());

        //如果玩家不在其他房间，创建成功
        if(record.get(address) == null){
            int id = room.getId();
            rooms.add(id-1, room);
            roomMap.put(id, room);
            record.put(address, room);
            return true;
        }
        CommonLog.log.error("The player " + room.getHost().getName() +
                " can't create the room cause he/she in another one.");
        return false;
    }

    //解散房间
    private void removeRoom(InetSocketAddress hostKey){
        Room room = record.remove(hostKey);
        if(room != null){

            if(room.getHost().isStarting()){
                room.timer.cancel();
            }
            roomMap.remove(room.getId());
            rooms.remove(room);
            Player guest = room.getGuest();
            if(guest != null){
                record.remove(new InetSocketAddress(guest.getIp(), guest.getPort()));
            }
        }
    }

    /**
     * 房客加入房间
     *
     * @date 2018/5/30 17:01
     * @param room 目标房间
	 * @param guest 房客
     * @return void
     **/
    public boolean addGuest(Room room, Player guest){
        InetSocketAddress address = new InetSocketAddress(guest.getIp(), guest.getPort());
        if(record.get(address) == null){
            room.setGuest(guest);
            record.put(address, room);
            return true;
        }
        CommonLog.log.error("The player " + guest.getName() +
                " can't join the room cause he/she in another one.");
        return false;
    }

    /**
     * 将房客从房间移除
     *
     * @date 2018/5/30 17:05
     * @param guest 房客
     * @return void
     **/
    public void removeGuest(Player guest){
        InetSocketAddress address = new InetSocketAddress(guest.getIp(), guest.getPort());
        Room room = record.get(address);
        Player host = room.getHost();
        //如果房间正在倒计时，取消房主的开始状态和倒计时
        if(host.isStarting()){
            host.setStarting(false);
            room.timer.cancel();
        }
        record.remove(address);
    }

    public Room getRoomById(int id){
        return roomMap.get(id);
    }

    public Room getRoomByIndex(int index){
        return rooms.get(index);
    }

    public Room getRoomByAddress(InetSocketAddress address){
        return record.get(address);
    }

    /**
     * 删除玩家并通知对方
     *
     * @date 2018/5/29 13:23
     * @param key 地址键
     * @return void
     **/
    public boolean removeAndInform(InetSocketAddress key){
        Room room = record.get(key);
        //房间存在
        if(room != null){
            DataPacket packet = new DataPacket("", MessageType.LEAVE);
            //判断对方
            boolean isHost = isHost(key, room);

            Player host = room.getHost();
            Player guest = room.getGuest();

            if(isHost){
                //如果是房主，通知房客，并删除房客的房间记录
                if(guest != null)
                    guest.getChannel().writeAndFlush(packet);
                    //解散房间
                removeRoom(new InetSocketAddress(host.getIp(), host.getPort()));
            }else {
                //如果是房客，通知房主(房主一般不会为空)
                removeGuest(guest);
                //删除房客
                room.setGuest(null);
                if(host == null)
                    CommonLog.log.error(
                            "Unexpected Error: The host was null when guest left the room.");
                else
                    host.getChannel().writeAndFlush(packet);
            }
            return true;
        }
        return false;
    }

}
