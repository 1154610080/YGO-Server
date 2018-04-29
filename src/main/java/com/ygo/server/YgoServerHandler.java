package com.ygo.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * 出入站事件处理类
 * @author EganChen
 * @date 2018/4/16 13:49
 */
@Sharable
public class YgoServerHandler extends ChannelInboundHandlerAdapter{

    /**
    * 处理入站事件
    * @date 2018/4/16
    * @param [ctx]
    * @return
    */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        System.out.println(ctx.channel().id() + "进来了");
    }

    /**
    * 处理出站事件
    * @date 2018/4/16
    * @param [ctx]
    * @return
    */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        System.out.println(ctx.channel().id() + "出去了");
    }

    /**
     * 处理接收到的消息
     * @date 2018/4/16
     * @param [ctx, msg]
     * @return
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;
        System.out.println("客户端"+ctx.channel().id()+"说"+in.toString(CharsetUtil.UTF_8));
    }

    /**
    * 将未决消息冲刷到远程节点，并关闭连接
    * @date 2018/4/16
    * @param [ctx]
    * @return
    */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
        ctx.close();
    }

    /**
    * 捕获并打印异常，关闭连接
    * @date 2018/4/16
    * @param [ctx, cause]
    * @return
    */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
