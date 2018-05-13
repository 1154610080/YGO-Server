package com.ygo.server;

import com.ygo.model.GameLobby;
import com.ygo.util.GsonWrapper;
import com.ygo.util.RequestParser;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.Map;


/**
 * 处理客户端请求
 *
 * @author Egan
 * @date 2018/5/7 22:54
 **/
public class LobbyServerInboundServer extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpResponse response = new DefaultFullHttpResponse
                (HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                        Unpooled.wrappedBuffer(
                                new GsonWrapper().create().
                                toJson(GameLobby.getRoomMap().values())
                                        .getBytes(CharsetUtil.UTF_8)));
        if(msg instanceof HttpRequest){
            Map<String, String> paramMap = RequestParser.parse((HttpRequest) msg);
            for(Map.Entry<String, String>entry : paramMap.entrySet()){
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }
        }
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
        cause.printStackTrace();
        ctx.close();
    }


}
