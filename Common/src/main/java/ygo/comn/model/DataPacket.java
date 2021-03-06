package ygo.comn.model;

import com.google.gson.GsonBuilder;
import ygo.comn.constant.MessageType;
import ygo.comn.constant.StatusCode;
import ygo.comn.constant.YGOP;
/**
 * 自定义协议YGOP的数据包类
 *  +——---------——+——------------——+——------------——+——-------——+
 *  |  版本号(4)  |  消息类型(4)    |  魔数校验码(4)  |  长度(4)  |
 *  +——--------——+——------------——+——------------——+——-------——+
 *  |                   JSON数据(UNKNOW)                       |
 *  +——-----------------------------------------------------——+
 * @author Egan
 * @date 2018/5/19 11:02
 **/
public class DataPacket {

    /**
     * 版本号
     **/
    private float version;

    /**
     * 消息类型
     **/
    private MessageType type;

    /**
     * 魔数校验码
     **/
    private int magic;

    /**
     * 消息体长度
     **/
    private int len;

    /**
     * 消息体
     **/
    private String body;

    public DataPacket(ResponseStatus status) {
        this.version = YGOP.VERSION;
        this.type = MessageType.WARING;
        this.magic = YGOP.MAGIC;
        this.body = new GsonBuilder().create().toJson(status);
        this.len = body.getBytes(YGOP.CHARSET).length;
    }

    public DataPacket(String body, MessageType type) {
        this.version = YGOP.VERSION;
        this.type = type;
        this.magic = YGOP.MAGIC;
        this.len = body.getBytes(YGOP.CHARSET).length;
        this.body = body;
    }

    public DataPacket(float version, MessageType type, int magic, int len, String body) {
        this.version = version;
        this.type = type;
        this.magic = magic;
        this.len = len;
        this.body = body;
    }
    public float getVersion() {
        return version;
    }

    public void setVersion(float version) {
        this.version = version;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
        this.len = body.getBytes(YGOP.CHARSET).length;
    }

    public void setStatusCode(StatusCode code){
        setType(MessageType.WARING);
        setBody(new GsonBuilder().create().toJson(new ResponseStatus(code)));
    }
}
