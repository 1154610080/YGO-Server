package com.ygo.constant;

import java.util.HashMap;
import java.util.Map;

public enum MessageType {
    /**
     * 聊天消息
     **/
    CHAT(0x1),
    /**
     * 操作消息
     **/
    OPERATE(CHAT.code << 1),
    /**
     * 玩家加入房间
     **/
    JOIN(OPERATE.code << 1),
    /**
     * 玩家离开房间
     **/
    LEAVE(OPERATE.code << 1),
    /**
     * 玩家进入\取消准备状态
     **/
    PREPARED(LEAVE.code << 1),
    /**
     * 玩家进入\取消开始状态
     **/
    STARTED(PREPARED.code << 1),
    /**
     * 请玩家开始倒计时
     **/
    COUNT_DOWN(STARTED.code << 1),
    /**
     * 校验客户端文件完整性
     **/
    VERITY(COUNT_DOWN.code << 1),
    /**
     * 发送卡牌信息
     **/
    DECK(VERITY.code << 1),
    /**
     * 玩家退出游戏
     **/
    EXIT(DECK.code << 1),
    /**
     * 发送警告信息
     **/
    WARING(EXIT.code << 1);


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
