package ygo.comn.model;
import io.netty.channel.Channel;
import ygo.comn.controller.RedisClient;
import java.net.InetSocketAddress;
import java.util.*;

/**
 * 全局静态映射集
 *
 * @author Egan
 * @date 2018/6/9 10:06
 **/
public class GlobalMap {

    /**
     * 管理所有玩家通道（玩家地址，通道）
     **/
    private static Map<InetSocketAddress, Channel> channelGroup = new HashMap<>();
    /**
     * 管理所有房间计时器（房间ID， 计时器）
     **/
    private static Map<Integer, Timer> timerGroup = new HashMap<>();

    private static Map<InetSocketAddress, RedisClient> redisGroup = new HashMap<>();

    public static Set<InetSocketAddress> getChannelKeys(){
        return channelGroup.keySet();
    }

    public static RedisClient getRedisforLobby(InetSocketAddress address){
        return getRedisClient(address, false);
    }

    public static RedisClient getRedisforDuel(InetSocketAddress address){
        return getRedisClient(address, true);
    }

    private static RedisClient getRedisClient(InetSocketAddress address, boolean isDuelServer){
        RedisClient redis = redisGroup.get(address);
        if(redis == null){
            redis = new RedisClient(isDuelServer);
            redisGroup.put(address, redis);
        }
        return redis;
    }

    public static void closeRedis(InetSocketAddress address){
        RedisClient redis = redisGroup.remove(address);
        if(redis != null)
            redis.close();
    }

    public static int ChannelSize(){
        return channelGroup.size();
    }

    public static Timer getTimer(int id){
        return timerGroup.get(id);
    }

    public static Channel getChannel(InetSocketAddress address){
        return channelGroup.get(address);
    }

    public static void addTimer(int id, Timer timer){
        timerGroup.put(id, timer);
    }

    public static void addChannel(InetSocketAddress address, Channel channel){
        channelGroup.put(address, channel);
    }

    public static Timer removeTimer(int id){
        return timerGroup.remove(id);
    }

    public static Channel removeChannel(InetSocketAddress address){
        return channelGroup.remove(address);
    }

    public static Collection<Channel> channels(){
        return channelGroup.values();
    }
}
