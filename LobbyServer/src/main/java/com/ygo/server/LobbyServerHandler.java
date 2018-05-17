package com.ygo.server;

import com.ygo.controller.LobbyController;
import com.ygo.model.ResponseStatus;
import com.ygo.model.StatusCode;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

/**
 * 大厅服务器处理器
 *
 * @author Egan
 * @date 2018/5/7 22:54
 **/
public class LobbyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            FullHttpResponse response = new DefaultFullHttpResponse
                    (HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                            Unpooled.wrappedBuffer(LobbyController.response((HttpRequest)msg)));
            response.headers().set("CONTENT-TYPE", "text/plain");
            response.headers().set("CONTENT-LENGTH", response.content().readableBytes());
            ctx.write(response);
            ctx.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.writeAndFlush(new ResponseStatus(StatusCode.INTERNAL_SERVER_ERROR, "msg"));
        cause.printStackTrace();
        ctx.close();
    }


}
