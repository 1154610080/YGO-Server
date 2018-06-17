package ygo.comn.controller.redis;

import ygo.comn.constant.MessageType;
import io.netty.channel.Channel;
import ygo.comn.model.DataPacket;
import ygo.comn.model.GlobalMap;
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

    private JedisWrapper jedis;

    RedisClient(boolean isDuelServer){

        jedis = new JedisWrapper(isDuelServer);
    }

    void close(){
        jedis.close();
    }

    public int size(){
        return jedis.size();
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
        if(jedis.getRoomByAddr(address) == null){

            int id = room.getId();
            jedis.addRoom(room);
            jedis.addRecord(address, room);

            return true;
        }
        return false;
    }

    //解散房间
    private void removeRoom(Room room){

        Player host = room.getHost();
        Player guest = room.getGuest();

        //如果不是决斗服务器，或房间未开始游戏，可以删除房间
        if(!room.isPlaying() || jedis.isDuelServer())
            jedis.removeRoom(room.getId());

        //若房间有房主，删除房主记录
        if(host!=null){
            RedisFactory.closeRedis(host.getAddress());
            jedis.removeRecord(host.getAddress());
            GlobalMap.removeChannel(host.getAddress());
            if(!jedis.isDuelServer() && host.isSP()){
                Timer timer = GlobalMap.removeTimer(room.getId());
                if (timer != null)
                    timer.cancel();
            }
        }

        if(guest != null){
            RedisFactory.closeRedis(guest.getAddress());
            GlobalMap.removeChannel(guest.getAddress());
            jedis.removeRecord(guest.getAddress());
            GlobalMap.removeChannel(guest.getAddress());
        }

    }

    //决斗服务器删除超时房间
    public void removeRoom(int id){
        jedis.removeRoom(id);
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
        if(jedis.getRoomByAddr(address) == null){
            //在大厅服务器中，该方法主要用于房客加入房间
            if(!jedis.isDuelServer())
                room.setGuest(player);
            jedis.addRecord(player.getAddress(), room);
            jedis.update(room);
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

        RedisFactory.closeRedis(guest.getAddress());

        Room room = jedis.getRoomByAddr(address);
        Player host = room.getHost();
        //如果房间正在倒计时，取消房主的开始状态和倒计时
        if(host != null && host.isSP()){
            host.setSP(false);
            Timer timer = GlobalMap.removeTimer(room.getId());
            if(timer != null)
                timer.cancel();
        }
        if(!room.isPlaying() && !jedis.isDuelServer()){
            room.setGuest(null);
        }
        jedis.removeRecord(address);
        jedis.update(room);
        GlobalMap.removeChannel(address);

    }

    public Room getRoomById(int id){
        return jedis.getRoomById(id);
    }

    public Room getRoomByAddress(InetSocketAddress address){
        return jedis.getRoomByAddr(address);
    }

    public List<Room> getRooms(){
        return jedis.getRooms();
    }

    /**
     * 删除玩家并通知对方
     *
     * @date 2018/5/29 13:23
     * @param key 地址键
     * @return void
     **/
    public boolean removeAndInform(InetSocketAddress key){
        Room room = jedis.getRoomByAddr(key);
        //房间存在
        if(room != null){
            DataPacket packet = new DataPacket("", MessageType.LEAVE);
            //判断谁退出
            boolean isHost = room.isHost(key);

            Player host = room.getHost();
            Player guest = room.getGuest();

            if(isHost){
                //如果是房主，通知房客，并删除房间
                if(guest != null){
                    Channel channel = GlobalMap.getChannel(guest.getAddress());
                    if(channel != null)
                        channel.writeAndFlush(packet);
                }
                //解散房间
                removeRoom(room);
            }else {
                //如果是房客，通知房主
                if(host != null){
                    Channel channel = GlobalMap.getChannel(host.getAddress());
                    if(channel != null)
                        channel.writeAndFlush(packet);
                }
                //如果是决斗服务器，删除房间
                if(jedis.isDuelServer()){
                    removeRoom(room);
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
        jedis.update(room);
    }



    public void removeRecord(InetSocketAddress address){
        jedis.removeRecord(address);
    }

    public void flush(){
        for (Channel channel : GlobalMap.channels()){
            InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
            int id = jedis.getRoomByAddr(address).getId();
            jedis.removeRecord(address);
            jedis.removeRoom(id);
        }

        System.out.println("All data of the server has be deleted");
    }

}
