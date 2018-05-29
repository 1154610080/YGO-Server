package ygo.comn.model;

import ygo.comn.constant.MessageType;
import ygo.comn.util.CommonLog;
import java.net.InetSocketAddress;
import java.util.HashMap;

/**
 * 房间记录类
 * 根据玩家IP地址和端口号映射其所在的房间
 * 采用单例模式（懒汉模式）
 *
 * @author Egan
 * @date 2018/5/17 23:36
 **/
public class RoomRecord extends HashMap<InetSocketAddress, Room>{

    private static RoomRecord record =
            new RoomRecord();

    public static RoomRecord getRecord() {
        return record;
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
     * 删除记录并通知对方
     *
     * @date 2018/5/29 13:23
     * @param key 地址键
     * @return void
     **/
    public boolean removeAndInform(InetSocketAddress key){
        Room room = remove(key);
        //房间存在
        if(room != null){
            DataPacket packet = new DataPacket("", MessageType.LEAVE);
            //判断对方
            boolean isHost = isHost(key, room);


            if(isHost){
                //如果是房主，通知房客，并删除房客的房间记录
                Player guest = room.getGuest();
                if(guest != null){
                    guest.getChannel().writeAndFlush(packet);
                    remove(new InetSocketAddress(guest.getIp(), guest.getPort()));
                }
            }else {
                //如果是房客，通知房主(房主一般不会为空)
                Player host = room.getHost();
                if(host == null)
                    CommonLog.log.error(
                            "Unexpected Error: The host was null when guest left the room.");
                else
                    host.getChannel().writeAndFlush(packet);
                return true;
            }
        }
        return false;
    }

}
