package ygo.comn.constant;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

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


    /*
     * 当前版本
     */
    public static final float VERSION = 1.0F;

    public static final List<Float> VERSIONS = new ArrayList<>();

    static {
        VERSIONS.add(1.0F);
    }

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

    /**
     * 最大房间数
     **/

    public static final int MAX_ROOMS = 128;

    /**
     * 超时时间
     **/
    public static int TIME_OUT = 5000;

    //其他相关常量

    /**
     * 剪刀
     **/
    public static final int SCISSORS = 1;
    /**
     * 石头
     **/
    public static final int ROCK = 2;
    /**
     * 布
     **/
    public static final int PAPER = 3;

    /**
     * 开始倒计时
     **/
    public static final int COUNTDOWN_SECONDS = 10;
}
