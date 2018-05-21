package com.ygo.controller;


import com.ygo.model.GameLobby;
import com.ygo.util.GsonWrapper;
import com.ygo.util.HttpUtils;
import io.netty.handler.codec.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 大厅控制器
 * 处理来自客户端的Http请求
 *
 * @author Egan
 * @date 2018/5/14 21:32
 **/
public class LobbyController {


    private HttpRequest request;
    private GsonWrapper wrapper;

    public LobbyController(HttpRequest request){
        this.request = request;
        this.wrapper = new GsonWrapper();
    }


    /**
     * 根据URL响应不同的信息
     *
     * @date 2018/5/21 9:29
     * @param
     * @return byte[] 响应结果
     **/
    public byte[] response(){

        GsonWrapper wrapper = new GsonWrapper();

        String parameter =
                HttpUtils.getRequestParams(request).keySet().iterator().next();


        switch (parameter){
            case "bulletin":
                return getBulletin();
            case "version":
                return getVersion();
            case "rooms":
                default:
                    return getRooms();
        }

    }

    /**
     * 获取房间列表
     *
     * @date 2018/5/21 9:27
     * @param
     * @return byte[]
     **/
    public byte[] getRooms(){

        return wrapper.toJson(GameLobby.getLobby());

    }

    /**
     * 获取公告信息
     *
     * @date 2018/5/21 9:42
     * @param
     * @return byte[]
     **/
    public byte[] getBulletin(){

        Map<String, String> bulletin = new HashMap<>();

        bulletin.put("bt", "欢迎使用YGO\n以下模块以开放：\n 1.游戏大厅");

        return wrapper.toJson(bulletin);

    }

    /**
     * 获取版本信息
     *
     * @date 2018/5/21 9:42
     * @param
     * @return byte[]
     **/
    public byte[] getVersion(){

        Map<String, Float> version = new HashMap<>();

        version.put("vs", 1.0f);

        return wrapper.toJson(version);
    }

}
