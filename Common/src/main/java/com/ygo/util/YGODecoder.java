package com.ygo.util;

import com.ygo.constant.MessageType;
import com.ygo.constant.StatusCode;
import com.ygo.constant.YGOP;
import com.ygo.model.DataPacket;
import com.ygo.model.ResponseStatus;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 自定义协议YGOP的解码器
 *
 * @author Egan
 * @date 2018/5/19 20:50
 **/
public class YGODecoder extends ByteToMessageDecoder{
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

            //消息超出最大长度，响应错误信息，并跳过消息
            if(byteBuf.readableBytes() > YGOP.HEAD_LEN + YGOP.MAX_LEN)
            {
                channelHandlerContext.channel().writeAndFlush(
                        new ResponseStatus(StatusCode.OUT_OF_SCOPE));
                byteBuf.skipBytes(byteBuf.readableBytes());
                return;
            }

            int version = byteBuf.readInt();
            MessageType type = MessageType.values()[byteBuf.readInt()];
            int magic = byteBuf.readInt();
            int len = byteBuf.readInt();

            //如果消息体长度不满足要求，等待下一波数据
            if (byteBuf.readableBytes() < len){
                return;
            }

            String body = byteBuf.readBytes(len).toString(YGOP.CHARSET);

            DataPacket packet = new DataPacket(version, type, magic, len, body);

            list.add(packet);
        }
    }
}
