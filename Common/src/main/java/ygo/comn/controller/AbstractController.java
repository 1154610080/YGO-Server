package ygo.comn.controller;

import com.google.gson.Gson;
import ygo.comn.model.DataPacket;
import io.netty.channel.Channel;

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

    protected AbstractController(DataPacket packet, Channel channel) {
        this.packet = packet;
        this.channel = channel;
    }

    /**
     * 根据数据包的消息类型分配不同的任务
     *
     * @date 2018/5/20 22:13
     * @param
     * @return void
     **/
    protected abstract void assign();

}
