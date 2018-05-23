package com.ygo.util;

import com.ygo.constant.MessageType;
import com.ygo.constant.StatusCode;
import com.ygo.constant.YGOP;
import com.ygo.controller.IpFilterHandler;
import com.ygo.model.DataPacket;
import com.ygo.model.ResponseStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.ipfilter.IpFilterRule;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 自定义协议YGOP的解码器
 *
 * @author Egan
 * @date 2018/5/19 20:50
 **/
public class YGOPDecoder extends ByteToMessageDecoder{
    /**
     * 将字节流解码为协议
     *
     * @date 2018/5/19 20:57  
     * @param channelHandlerContext 上下文
	 * @param byteBuf 字节数据容器
	 * @param list 目标对象列表
     * @return void 
     **/  
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf, List<Object> list) throws Exception {
        //可读字节数必须大或等于消息头字节数
        if(byteBuf.readableBytes() >= YGOP.HEAD_LEN){

            //消息超出最大长度，加入黑名单
            if(byteBuf.readableBytes() > YGOP.HEAD_LEN + YGOP.MAX_LEN)
            {
                IpFilterHandler.addFilteredAddress(
                        channelHandlerContext.channel());
                return;
            }

            float version = byteBuf.readFloat();
            int typeInt =byteBuf.readInt();
            MessageType type = MessageType.fromInt32(typeInt);
            int magic = byteBuf.readInt();
            //校验码错误，加入黑名单
            if (magic != YGOP.MAGIC){
                IpFilterHandler.addFilteredAddress(
                        channelHandlerContext.channel());
                return;
            }
            int len = byteBuf.readInt();

            //如果消息体长度不满足要求，等待下一波数据
            if (byteBuf.readableBytes() < len){
                return;
            }

            String body = byteBuf.readBytes(len).toString(YGOP.CHARSET);

            DataPacket packet = new DataPacket(version, type, magic, len, body);

            System.out.println("");

            list.add(packet);

        }
    }

}