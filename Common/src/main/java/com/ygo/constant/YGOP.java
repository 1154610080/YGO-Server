package com.ygo.constant;

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
    /*
    * 版本的顺序
    */
    public static final int VERSION_ORDER = 0;
    /*
    * 类型的顺序
    */
    public static final int TYPE_ORDER = VERSION_ORDER + 1;
    /*
    * 魔数的顺序
    */
    public static final int MAGIC_ORDER = TYPE_ORDER + 1;
    /*
    * 消息体长度的顺序
    */
    public static final int LEN_ORDER = MAGIC_ORDER + 1;
    /*
    * 消息体的顺序
    */
    public static final int BODY_ORDER = LEN_ORDER + 1;

    /*
    * 版本的位置
    */
    public static final int VERSION_POS = 0;
    /*
    * 类型的位置
    */
    public static final int TYPE_POS = VERSION_POS + VERSION_LEN;
    /*
    * 魔数的位置
    */
    public static final int MAGIC_POS = TYPE_POS + TYPE_LEN;
    /*
    * 消息体长度的位置
    */
    public static final int LEN_POS = MAGIC_POS + MAGIC_LEN;
    /*
    * 消息体的位置
    */
    public static final int BODY_POS = LEN_POS + LEN_LEN;


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
    public static final int HEAD_LEN = 20;
    /*
     * 最大内容长度
     */
    public static final int MAX_LEN = 1024;
    /*
     * 协议的段数
     */
    public static final int PART_COUNT = 6;

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
