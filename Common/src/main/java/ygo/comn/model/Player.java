package ygo.comn.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.List;

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
     * 玩家是否进入开始/准备状态
     **/
    @Expose()
    @SerializedName("isp")
    private boolean isSP = false;

    /**
     * 玩家卡组
     **/
    @Expose(serialize = false, deserialize = false)
    @SerializedName("dc")
    private List<Integer> deck;

    @Override
    public boolean equals(Object obj) {

        return obj instanceof Player &&
                ((Player)obj).ip.equals(ip) &&
                ((Player)obj).port == port;
    }

    public Player(){}

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

    public boolean isSP() {
        return isSP;
    }

    public void setSP(boolean SP) {
        isSP = SP;
    }

    public InetSocketAddress getAddress(){
        return new InetSocketAddress(ip, port);
    }

    public List<Integer> getDeck() {
        return deck;
    }

    public void setDeck(List<Integer> deck) {
        this.deck = deck;
    }
}
