package com.ygo.controller;

import com.ygo.model.GameLobby;
import com.ygo.model.Room;
import com.ygo.util.GsonWrapper;
import com.ygo.util.ResponseStatus;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

/**
 * 大厅控制类
 *
 * @author Egan
 * @date 2018/5/14 21:32
 **/
public class LobbyController {


    /**
     * 处理客户端的GET和POST请求
     *
     * @author Egan
     * @date 2018/5/14 22:08
     **/
    public static byte[] response(HttpRequest request){

        GsonWrapper wrapper = new GsonWrapper();

        if (request.method() == HttpMethod.GET)
            return wrapper.toJson(GameLobby.getLobby());
        else if(request.method() == HttpMethod.POST)
            return wrapper.toJson(addRoom((FullHttpRequest)request));
        return wrapper.toJson(new ResponseStatus().error("不支持其他方法"));
    }

    /**
     * 创建房间
     * 处理客户端的创建新房间请求(POST)
     * 
     * @author Egan
     * @date 2018/5/14 21:55
     **/
    public static ResponseStatus addRoom(FullHttpRequest request){
        System.out.println("add a new room");

        byte[] bytes = new byte[request.content().readableBytes()];
        request.content().readBytes(bytes);
        System.out.println(new String(bytes));
        Room room = new GsonWrapper().toObject(bytes, Room.class);
        GameLobby.getLobby().getRooms().add(room);

        return new ResponseStatus().ok();
    }

    public static ResponseStatus joinRoom(FullHttpRequest request){
        return new ResponseStatus().ok();
    }
    
    /**
     * 决斗服务器拥有删除房间的权限
     * 
     * @author Egan
     * @date 2018/5/14 21:44
     **/
    public static ResponseStatus removeRoom(FullHttpRequest request){
        return new ResponseStatus().ok();
    }
}
