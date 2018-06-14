package ygo.comn.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import ygo.comn.constant.MessageType;
import ygo.comn.constant.Secret;
import ygo.comn.constant.StatusCode;
import ygo.comn.constant.YGOP;
import ygo.comn.model.Player;
import ygo.comn.model.Room;
import ygo.comn.util.YgoLog;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Jedis包装类
 * 包装jedis方法
 *
 * @author Egan
 * @date 2018/6/7 10:17
 **/
class JedisWrapper {

    private static JedisPool pool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(YGOP.MAX_ROOMS);
        config.setMaxWaitMillis(-1);
        config.setMaxIdle(YGOP.MAX_ROOMS);
        config.setTestOnBorrow(false);
        pool  = new JedisPool(new JedisPoolConfig(), Secret.REDIS_HOST, Secret.REDIS_PORT,
                10000, Secret.REDIS_PWD);
    }

    private final String ROOM_MAP = "roomMap";
    private final String ROOM_RECORD = "record";
    /**
     * 是否为决斗服务器
     **/
    private boolean isDuelServer;

    private YgoLog log = new YgoLog("Redis-Client");

    private Jedis jedis;

    private Gson gson = new GsonBuilder().create();

    public JedisWrapper(boolean isDuelServer){
        try {
            synchronized (JedisPool.class){
                jedis  = pool.getResource();
                this.isDuelServer = isDuelServer;
            }

        }catch (Exception ex){
            log.fatal(ex.toString());
        }
    }

    public boolean isDuelServer(){
        return isDuelServer;
    }

    public int size(){
        return Math.toIntExact(jedis.hlen(ROOM_MAP));
    }

    public void addRecord(InetSocketAddress address, Room room){
        try {
            long r = jedis.hset(ROOM_RECORD, address.toString(), gson.toJson(room));
            //log.info(StatusCode.REDIS, "Add a record. addr:" + address.toString() + " result:" + r);
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
    }

    public void addRoom(int id, Room room){
        try {
            if(room.isPlaying() && !isDuelServer)
                return;
            long r = jedis.hset(ROOM_MAP, String.valueOf(id), gson.toJson(room));
            //log.info(StatusCode.REDIS, "Add a room. id:" + id + " result:" + r);
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
    }

    public Room getRoomByAddr(InetSocketAddress address){
        Room room = null;
        try {
            String json = jedis.hget(ROOM_RECORD, address.toString());
            room = gson.fromJson(json, Room.class);
        }catch (Exception ex){
            log.fatal(ex.toString());
        }

        return room;
    }

    public Room getRoomById(int id){
        Room room = null;
        try {
            room = gson.fromJson(jedis.hget(ROOM_MAP, String.valueOf(id)), Room.class);

        }catch (Exception ex){
            log.fatal(ex.toString());
        }

        return room;
    }

    public List<Room> getRooms(){
        List<Room> rooms = new ArrayList<>();
        try {
            List<String> roomStr = jedis.hvals(ROOM_MAP);
            for(String str : roomStr){
                rooms.add(gson.fromJson(str, Room.class));
            }
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
        return rooms;
    }

    public void removeRecord(InetSocketAddress address){
        try {
            long r = jedis.hdel(ROOM_RECORD, address.toString());
            log.info(StatusCode.REDIS, "Remove a record. addr:" + address.toString() + " result:" + r);
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
    }

    public void removeRoom(int id){
        try{
            //房间开始游戏后，只有决斗服务器有权删除房间
            Room room = getRoomById(id);
            if(room == null ||( room.isPlaying() && !isDuelServer)){
                return;
            }
            long r = jedis.hdel(ROOM_MAP, String.valueOf(id));
            //log.info(StatusCode.REDIS, "Remove a room . id:" + id + " result:" + r  + room.isPlaying() + !isDuelServer);
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
    }

    /**
     * 更新存在的键值对
     *
     * @date 2018/6/7 13:41
     * @param room 新的房间信息
     * @return void
     **/
    public void update(Room room){

        Room tRoom = getRoomById(room.getId());

        //大厅无法修改开始游戏的房间
        if(tRoom != null && tRoom.isPlaying() && !isDuelServer)
            return;

        if(getRoomById(room.getId()) != null)
            addRoom(room.getId(), room);

        Player host = room.getHost();
        Player guest = room.getGuest();

        if(host != null && getRoomByAddr(host.getAddress()) != null)
            addRecord(room.getHost().getAddress(), room);


        if(guest != null && getRoomByAddr(guest.getAddress()) != null){
            addRecord(guest.getAddress(), room);
        }

    }

    public void close(){
        log.info(StatusCode.REDIS, "closing");
        jedis.close();
    }
}
