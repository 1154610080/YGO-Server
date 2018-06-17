package ygo.comn.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ygo.comn.constant.StatusCode;
import ygo.comn.controller.redis.RedisClient;
import ygo.comn.model.DataPacket;
import io.netty.channel.Channel;
import ygo.comn.model.GlobalMap;
import ygo.comn.model.Player;
import ygo.comn.model.Room;
import ygo.comn.util.YgoLog;

import java.net.InetSocketAddress;

/**
 * 抽象控制器
 *
 * @author Egan
 * @date 2018/5/20 22:11
 **/
public abstract class AbstractController {

    protected YgoLog log;

    protected DataPacket packet;

    protected Channel channel;

    protected Gson gson;

    protected InetSocketAddress address;

    protected RedisClient redis;

    protected Room room;

    protected AbstractController(DataPacket packet, Channel channel) {
        this.packet = packet;
        this.channel = channel;
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        this.address = (InetSocketAddress) channel.remoteAddress();
        this.log = new YgoLog("AbstractController");

        assign();

    }

    /**
     * 根据数据包的消息类型分配不同的任务
     *
     * @date 2018/5/20 22:13
     * @param
     * @return void
     **/
    protected abstract void assign();

    protected boolean isNormalRoom()
    {
        if(room == null){
            packet.setStatusCode(StatusCode.DISMISSED);
            channel.writeAndFlush(packet);
            return true;
        }

        if(room.getHost() == null){
            packet.setStatusCode(StatusCode.ILLEGAL_DATA);
            channel.writeAndFlush(packet);
            log.error(StatusCode.ILLEGAL_DATA, "The room(" + room.getName() +") without a host");
            return true;
        }

        return false;
    }

    protected boolean isMatchedChannel(Room room, boolean isHost){

        boolean result = isHost ? channel.equals(GlobalMap.getChannel(room.getHost().getAddress())) :
                room.getGuest() != null && channel.equals(GlobalMap.getChannel(room.getGuest().getAddress()));

        if(!result){
            log.error(StatusCode.UNMATCHED_CHANNEL, "玩家没有权限执行该操作");
            packet.setStatusCode(StatusCode.UNMATCHED_CHANNEL);
            channel.writeAndFlush(packet);
        }

        return !result;
    }

    /**
     * 发送聊天消息
     *
     * @date 2018/5/25 22:31
     * @param
     * @return void
     **/
    protected void chat()
    {

        Player host = room.getHost();
        Player guest = room.getGuest();

        //向双方广播消息
        if(host != null)
            GlobalMap.getChannel(host.getAddress()).writeAndFlush(packet);
        if(guest!=null)
            GlobalMap.getChannel(guest.getAddress()).writeAndFlush(packet);
    }
}
