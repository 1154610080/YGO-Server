package ygo.comn.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.net.InetSocketAddress;
import java.util.Timer;

/**
 * 房间类
 * @author EganChen
 * @date 2018/4/16 14:11
 */
public class Room {

    /**
     * 房间ID
     **/
    @Expose()
    @SerializedName("id")
    private int id;

    /**
     * 房间名称
     **/
    @Expose()
    @SerializedName("nm")
    private String name;

    /**
     * 房间描述
     **/
    @Expose()
    @SerializedName("ds")
    private String desc;

    /**
     * 房间密码
     **/
    @Expose(serialize = false)
    @SerializedName("pw")
    private String password;

    /**
     * 房间是否存在密码
     **/
    @Expose()
    @SerializedName("hp")
    private boolean hasPwd = false;

    /**
     * 房间是否已经开始游戏
     **/
    @Expose()
    @SerializedName("isp")
    private boolean isPlaying = false;

    /**
     * 房主
     **/
    @Expose()
    @SerializedName("hs")
    private Player host;

    /**
     * 房客
     **/
    @Expose()
    @SerializedName("gs")
    private Player guest;

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Room && id == ((Room) obj).id;
    }

    public Room(){}

    public Room(int id, String name, String desc, String password,
                boolean hasPwd, boolean isPlaying,
                Player host, Player guest) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.password = password;
        this.hasPwd = hasPwd;
        this.isPlaying = isPlaying;
        this.host = host;
        this.guest = guest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasPwd() {
        return hasPwd;
    }

    public void setHasPwd(boolean hasPwd) {
        this.hasPwd = hasPwd;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public Player getHost() {
        return host;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public Player getGuest() {
        return guest;
    }

    public void setGuest(Player guest) {
        this.guest = guest;
    }


    public boolean isHost(InetSocketAddress address) {
        return host != null && host.getAddress().equals(address);
    }

}
