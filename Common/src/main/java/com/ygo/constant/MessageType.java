package com.ygo.constant;

import java.util.HashMap;
import java.util.Map;

public enum MessageType {
    CHAT(0x1),
    OPERATE(0x2);

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
