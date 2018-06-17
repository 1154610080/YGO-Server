package ygo.comn.model;
import io.netty.channel.Channel;

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

    /**
     * 大厅服务器通道
     **/
    private static Channel DuelChannel = null;

    public static Channel getDuelChannel() {
        return DuelChannel;
    }

    public static void setDuelChannel(Channel duelChannel) {
        GlobalMap.DuelChannel = duelChannel;
    }

    public static Set<InetSocketAddress> getChannelKeys(){
        return channelGroup.keySet();
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
