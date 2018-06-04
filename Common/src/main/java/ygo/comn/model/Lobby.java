package ygo.comn.model;

import io.netty.channel.ChannelFuture;
import ygo.comn.constant.MessageType;
import io.netty.channel.Channel;
import ygo.comn.controller.RedisClient;
import java.net.InetSocketAddress;
import java.sql.Time;
import java.util.*;

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
     * 管理所有玩家通道（玩家地址，通道）
     **/
    private static Map<InetSocketAddress, Channel> channelGroup = new HashMap<>();
    /**
     * 管理所有房间计时器（房间ID， 计时器）
     **/
    private static Map<Integer, Timer> timerGroup = new HashMap<>();

    public static Lobby getLobby() {
        return lobby;
    }


    private RedisClient redis = new RedisClient();


    public int size(){
        return redis.size();
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

        InetSocketAddress address = host.getAddress();


        //如果玩家不在其他房间，创建成功
        if(redis.getRoomByAddr(address) == null){

            int id = room.getId();
            redis.addRoom(id, room);
            redis.addRecord(address, room);

            return true;
        }
        return false;
    }

    //解散房间
    private void removeRoom(InetSocketAddress hostKey){

        Room room = redis.getRoomByAddr(hostKey);

        if(room != null){

            if(room.getHost().isSP()){
                timerGroup.get(room.getId()).cancel();
                timerGroup.remove(room.getId());
            }

            redis.removeRoom(room.getId());
            redis.removeRecord(hostKey);

            Player guest = room.getGuest();
            if(guest != null){
                redis.removeRecord(guest.getAddress());
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
        InetSocketAddress address = guest.getAddress();
        if(redis.getRoomByAddr(address) == null){
            room.setGuest(guest);
            redis.update(room);
        }
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
        InetSocketAddress address = guest.getAddress();
        Room room = redis.getRoomByAddr(address);
        Player host = room.getHost();
        //如果房间正在倒计时，取消房主的开始状态和倒计时
        if(host.isSP()){
            host.setSP(false);
            timerGroup.get(room.getId()).cancel();
            timerGroup.remove(room.getId());
        }
        room.setGuest(null);
        redis.removeRecord(address);
        redis.update(room);
    }

    public Room getRoomById(int id){
        return redis.getRoomById(id);
    }

    public Room getRoomByAddress(InetSocketAddress address){
        return redis.getRoomByAddr(address);
    }

    public List<Room> getRooms(){
        return redis.getRooms();
    }

    /**
     * 删除玩家并通知对方
     *
     * @date 2018/5/29 13:23
     * @param key 地址键
     * @return void
     **/
    public boolean removeAndInform(InetSocketAddress key){
        Room room = redis.getRoomByAddr(key);
        //房间存在
        if(room != null){
            DataPacket packet = new DataPacket("", MessageType.LEAVE);
            //判断对方
            boolean isHost = room.isHost(key);

            Player host = room.getHost();
            Player guest = room.getGuest();

            if(isHost){
                //如果是房主，通知房客，并删除房客的房间记录
                if(guest != null)
                    channelGroup.get(guest.getAddress()).writeAndFlush(packet);
                //解散房间
                removeRoom(host.getAddress());
            }else {
                //如果是房客，通知房主
                removeGuest(guest);
                //删除房客
                room.setGuest(null);
                channelGroup.get(host.getAddress()).writeAndFlush(packet);
            }
            return true;
        }

        return false;
    }

    public void updateRoom(Room room){
        redis.update(room);
    }

    public Timer getTimer(int id){
        return timerGroup.get(id);
    }

    public Channel getChannel(InetSocketAddress address){
        return channelGroup.get(address);
    }

    public void addTimer(int id, Timer timer){
        timerGroup.put(id, timer);
    }

    public void addChannel(InetSocketAddress address, Channel channel){
        channelGroup.put(address, channel);
    }

}
