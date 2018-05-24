package ygo.comn.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 用户类
 * @author EganChen
 * @date 2018/4/16 14:10
 */
public class Player {

    /**
     * 玩家名
     **/
    @Expose()
    @SerializedName("nm")
    private String name;

    /**
     * 玩家头像图片
     **/
    @Expose()
    @SerializedName("hd")
    private String head;

    /**
     * 玩家主机IP地址
     **/
    @Expose(serialize = false, deserialize = false)
    @SerializedName("ad")
    private String ip;


    /**
     * 玩家主机端口号
     **/
    @Expose(serialize = false, deserialize = false)
    @SerializedName("pr")
    private int port;

    /**
     * 玩家猜拳的结果
     **/
    @Expose(serialize = false, deserialize = false)
    @SerializedName("fg")
    private int finger = 0x0;

    /**
     * 玩家（房主）是否进入开始状态
     **/
    @Expose()
    @SerializedName("iss")
    private boolean isStarting = false;

    /**
     * 玩家（房客）是否进入准备状态
     **/
    @Expose()
    @SerializedName("isp")
    private boolean isPrepared = false;

    @Override
    public boolean equals(Object obj) {

        return obj instanceof Player &&
                ((Player)obj).ip.equals(ip) &&
                ((Player)obj).port == port;
    }

    public Player(){}

    public Player(String name, String head,
                  String ip, int port,
                  boolean isStarting, boolean isPrepared) {
        this.name = name;
        this.head = head;
        this.ip = ip;
        this.port = port;
        this.isStarting = isStarting;
        this.isPrepared = isPrepared;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getFinger() {
        return finger;
    }

    public void setFinger(int finger) {
        this.finger = finger;
    }

    public boolean isStarting() {
        return isStarting;
    }

    public void setStarting(boolean starting) {
        isStarting = starting;
    }

    public boolean isPrepared() {
        return isPrepared;
    }

    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
    }
}
