package ygo.comn.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ygo.comn.model.DataPacket;
import io.netty.channel.Channel;
import ygo.comn.model.Player;
import ygo.comn.model.Room;
import ygo.comn.model.RoomRecord;
import java.net.InetSocketAddress;

/**
 * 抽象控制器
 *
 * @author Egan
 * @date 2018/5/20 22:11
 **/
public abstract class AbstractController {

    protected DataPacket packet;

    protected Channel channel;

    protected Gson gson;

    protected InetSocketAddress address;

    protected AbstractController(DataPacket packet, Channel channel) {
        this.packet = packet;
        this.channel = channel;
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        this.address = (InetSocketAddress) channel.remoteAddress();
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

    /**
     * 发送聊天消息
     *
     * @date 2018/5/25 22:31
     * @param
     * @return void
     **/
    protected void chat()
    {
        InetSocketAddress address = (InetSocketAddress) channel.remoteAddress();
        Room room = RoomRecord.getRecord().get(address);
        //如果找不到房间，说明房间已解散
        if (room == null){
            channel.writeAndFlush(
                    new ygo.comn.model.ResponseStatus(ygo.comn.constant.StatusCode.DISMISSED)
            );
            return;
        }

        Player host = room.getHost();
        Player guest = room.getGuest();

        //向双方广播消息
        if (host!=null)
            host.getChannel().writeAndFlush(packet);
        if(guest!=null)
            guest.getChannel().writeAndFlush(packet);
    }
}
