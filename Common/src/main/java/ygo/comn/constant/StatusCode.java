package ygo.comn.constant;

/**
 * 响应状态码枚举
 *
 * @author Egan
 * @date 2018/5/16 12:32
 **/
public enum StatusCode {


    RECEIVE(1001),
    SEND(1002),
    INBOUND(1004),
    OUTBOUND(1008),
    REDIS(1012),
    /**
     * 数据不合法
     **/
    ILLEGAL_DATA(4000),
    /**
     * 不遵守协议
     **/
    DISOBEY_PROTOCOL(4121),
    OUT_OF_SCOPE(4131),
    UNSUPPORTED_METHOD(4050),
    LOST_CONNECTION(4055),
    BLACKLISTED(4314),
    INCORRECT(4316),
    UNPREPARED(4382),
    DISMISSED(4383),
    FULL_LOBBY(4391),
    FULL_ROOM(4392),
    INCOMPLETE_ROOM(4393),
    INTERNAL_SERVER_ERROR(5000),
    ERROR_CONTROLLER(6001),
    /**
     * 玩家已在其他房间
     **/
    BE_IN_ANOTHER(6008),
    /**
     * 玩家不在本房间
     **/
    NOT_IN_HERE(6012),
    /**
     * 通道不匹配
     **/
    UNMATCHED_CHANNEL(6024);

    private int code;

    StatusCode(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
