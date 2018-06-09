package ygo.comn.controller;

import ygo.comn.constant.MessageType;
import io.netty.channel.Channel;
import ygo.comn.model.DataPacket;
import ygo.comn.model.Player;
import ygo.comn.model.Room;

import java.net.InetSocketAddress;
import java.util.*;

/**
 * Redis客户端类
 * 处理所有房间和玩家数据
 *
 * @author Egan
 * @date 2018/5/17 23:36
 **/
public class RedisClient {

    /**
     * 管理所有玩家通道（玩家地址，通道）
     **/
    private static Map<InetSocketAddress, Channel> channelGroup = new HashMap<>();
    /**
     * 管理所有房间计时器（房间ID， 计时器）
     **/
    private static Map<Integer, Timer> timerGroup = new HashMap<>();

    public static Set<InetSocketAddress> getChannelKeys(){
        return channelGroup.keySet();
    }

    public static int ChannelSize(){
        return channelGroup.size();
    }

    public static RedisClient getRedisForLobby(){
        return new RedisClient(false);
    }

    public static RedisClient getRedisForDuel(){
        return new RedisClient(true);
    }

    private JedisWrapper redis;

    private RedisClient(boolean isDuelServer){
        redis = new JedisWrapper(isDuelServer);
    }

    public void close(){
        redis.close();
    }

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
    private void removeRoom(InetSocketAddress Key){

        Room room = redis.getRoomByAddr(Key);

        if(room != null){

            Player host = room.getHost();
            Player guest = room.getGuest();

            if(!room.isPlaying() || redis.isDuelServer())
                redis.removeRoom(room.getId());

            if(host!=null){
                redis.removeRecord(host.getAddress());
                removeChannel(host.getAddress());
                if(!redis.isDuelServer() && host.isSP()){
                    Timer timer = timerGroup.remove(room.getId());
                    if (timer != null)
                        timer.cancel();
                }
            }

            if(guest != null){
                removeChannel(guest.getAddress());
                redis.removeRecord(guest.getAddress());
                removeChannel(guest.getAddress());
            }


        }
    }

    /**
     * 玩家加入房间
     *
     * @date 2018/5/30 17:01
     * @param room 目标房间
	 * @param player 房客
     * @return void
     **/
    public boolean addPlayer(Room room, Player player){
        InetSocketAddress address = player.getAddress();
        if(redis.getRoomByAddr(address) == null){
            //在大厅服务器中，该方法主要用于房客加入房间
            if(!redis.isDuelServer())
                room.setGuest(player);
            redis.addRecord(player.getAddress(), room);
            redis.update(room);
            return true;
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
        if(host != null && host.isSP()){
            host.setSP(false);
            Timer timer = timerGroup.remove(room.getId());
            if(timer != null)
                timer.cancel();
        }
        if(!room.isPlaying() && !redis.isDuelServer()){
            room.setGuest(null);
        }
        redis.removeRecord(address);
        redis.update(room);
        removeChannel(address);

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
            //判断谁退出
            boolean isHost = room.isHost(key);

            Player host = room.getHost();
            Player guest = room.getGuest();

            if(isHost){
                //如果是房主，通知房客，并删除房客的房间记录
                if(guest != null){
                    Channel channel = channelGroup.get(guest.getAddress());
                    if(channel != null)
                        channel.writeAndFlush(packet);
                }
                //解散房间
                removeRoom(host.getAddress());
            }else {
                //如果是房客，通知房主
                if(host != null){
                    Channel channel = channelGroup.get(host.getAddress());
                    if(channel != null)
                        channel.writeAndFlush(packet);
                }
                //如果是决斗服务器，删除房间
                if(redis.isDuelServer()){
                    removeRoom(guest.getAddress());
                }else {
                    //否则删除房客
                    removeGuest(guest);
                }


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

    public Timer removeTimer(int id){
        return timerGroup.remove(id);
    }

    public Channel removeChannel(InetSocketAddress address){
        return channelGroup.remove(address);
    }

    public void removeRecord(InetSocketAddress address){
        redis.removeRecord(address);
    }

    public void flush(){
        for (Channel channel : channelGroup.values()){
            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            int id = redis.getRoomByAddr(address).getId();
            redis.removeRecord(address);
            redis.removeRoom(id);
        }

        System.out.println("All data of the server has be deleted");
    }

}
