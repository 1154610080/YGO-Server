package ygo.comn.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import ygo.comn.constant.Secret;
import ygo.comn.model.Room;
import ygo.comn.util.YgoLog;
import java.net.InetSocketAddress;

public class RedisClient {

    private final String ROOM_MAP = "roomMap";
    private final String ROOM_RECORD = "record";

    private YgoLog log = new YgoLog("Redis-Client");

    private Jedis jedis;

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public RedisClient(){
        try {
            jedis  = (new JedisPool(new JedisPoolConfig(), Secret.REDIS_HOST, Secret.REDIS_PORT,
                    10000, Secret.REDIS_PWD)).getResource();
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
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

    public void getRecord(InetSocketAddress address){
        try {
            jedis.hget(ROOM_RECORD, address.toString());
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
    }

    public void getRoom(int id){
        try {
            jedis.hget(ROOM_MAP, String.valueOf(id));
        }catch (Exception ex){
            log.fatal(ex.toString());
        }
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
}
