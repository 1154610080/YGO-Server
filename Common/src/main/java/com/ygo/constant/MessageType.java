package com.ygo.constant;

public enum MessageType {
    CHAT(0x2),
    OPERATE(0X4);

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
