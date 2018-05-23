package com.ygo.constant;

import java.util.HashMap;
import java.util.Map;

public enum MessageType {
    /**
     * 获取房间列表
     **/
    GET_ROOMS(0x1),
    /**
     * 玩家创建房间
     **/
    CREATE(GET_ROOMS.code << 1),
    /**
     * 玩家加入房间
     **/
    JOIN(CREATE.code << 1),
    /**
     * 玩家离开房间
     **/
    LEAVE(JOIN.code << 1),
    /**
     * 玩家被踢出房间
     **/
    KICK_OUT(LEAVE.code << 1),
    /**
     * 玩家进入\取消准备状态
     **/
    READY(KICK_OUT.code << 1),
    /**
     * 玩家进入\取消开始状态
     **/
    STARTED(READY.code << 1),
    /**
     * 请玩家开始倒计时
     **/
    COUNT_DOWN(STARTED.code << 1),
    /**
     * 发送卡牌信息
     **/
    DECK(COUNT_DOWN.code << 1),
    /**
     * 猜拳消息
     **/
    FINGER_GUESS(DECK.code << 1),
    /**
     * 聊天消息
     **/
    CHAT(FINGER_GUESS.code << 1),
    /**
     * 操作消息
     **/
    OPERATE(CHAT.code << 1),
    /**
     * 玩家退出游戏
     **/
    EXIT(OPERATE.code << 1),
    /**
     * 获取版本信息
     **/
    GET_VERSION(EXIT.code << 1),
    /**
     * 获取公告板
     **/
    GET_BULLETIN(GET_VERSION.code << 1),
    /**
     * 校验客户端文件完整性
     **/
    VERITY(GET_BULLETIN.code << 1),
    /**
     * 发送警告信息
     **/
    WARING(VERITY.code << 1);


    private static final Map<Integer, MessageType> integerToMessageType = new HashMap<>();

    static {
        for (MessageType type : values()){
            integerToMessageType.put(type.getCode(), type);
        }
    }

    public static MessageType fromInt32(Integer code){
        return integerToMessageType.get(code);
    }

    /**
     * 类型代码
     **/
    private int code;

    MessageType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }


}
