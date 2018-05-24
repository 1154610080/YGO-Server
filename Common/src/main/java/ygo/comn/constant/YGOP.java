package ygo.comn.constant;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * YGOP协议的常量
 *
 * @author Egan
 * @date 2018/5/19 15:44
 **/
public class YGOP {

    /*
    * 版本所占字节数
    */
    public static final int VERSION_LEN = 4;
    /*
    * 消息类型所占字节数
    */
    public static final int TYPE_LEN = 4;
    /*
    * 魔数所占字节数
    */
    public static final int MAGIC_LEN = 4;
    /*
    * 消息体长度所占字节数
    */
    public static final int LEN_LEN = 4;

    /**
     * 默认编码
     **/
    public static final Charset CHARSET = Charset.forName("utf-8");

    /**
     * 大厅服务器的决斗客户端的地址和端口
     **/
    public static final InetSocketAddress LOBBY_SERVER_ADDR = new InetSocketAddress("127.0.0.1", 19208);

    /*
     * 当前版本
     */
    public static final float VERSION = 1.0F;
    /*
     * 魔数
     */
    public static final int MAGIC = 0x3cb;
    /*
     * 数据包头的固定长度
     */
    public static final int HEAD_LEN = VERSION_LEN + TYPE_LEN + MAGIC_LEN + LEN_LEN;
    /*
     * 最大内容长度
     */
    public static final int MAX_LEN = 1024;

    //其他相关常量

    /**
     * 未出拳
     **/
    public static final int NO_FINGER = 0x0;
    /**
     * 剪刀
     **/
    public static final int SCISSORS = 0x1;
    /**
     * 石头
     **/
    public static final int ROCK = 0x10;
    /**
     * 布
     **/
    public static final int PAPER = 0X100;
}
