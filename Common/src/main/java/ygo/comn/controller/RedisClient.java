package ygo.comn.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import ygo.comn.constant.Secret;
import ygo.comn.model.Player;
import ygo.comn.model.Room;
import ygo.comn.util.YgoLog;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class RedisClient {

    private final String ROOM_MAP = "roomMap";
    private final String ROOM_RECORD = "record";

    private YgoLog log = new YgoLog("Redis-Client");

    private Jedis jedis;

    private Gson gson = new GsonBuilder().create();

    public RedisClient(){
        try {
            jedis  = (new JedisPool(new JedisPoolConfig(), Secret.REDIS_HOST, Secret.REDIS_PORT,
                    10000, Secret.REDIS_PWD)).getResource();
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
    }

    public int size(){
        return Math.toIntExact(jedis.hlen(ROOM_MAP));
    }

    public void addRecord(InetSocketAddress address, Room room){
        try {
            jedis.hset(ROOM_RECORD, address.toString(), gson.toJson(room));
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
    }

    public void addRoom(int id, Room room){
        try {
            jedis.hset(ROOM_MAP, String.valueOf(id), gson.toJson(room));
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
            jedis.hdel(ROOM_RECORD, address.toString());
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
    }

    public void removeRoom(int id){
        try{
            jedis.hdel(ROOM_MAP, String.valueOf(id));
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
    }

    public void update(Room room){
        addRoom(room.getId(), room);
        addRecord(room.getHost().getAddress(), room);

        Player guest = room.getGuest();
        if(guest != null){
            addRecord(guest.getAddress(), room);
        }

    }
}
