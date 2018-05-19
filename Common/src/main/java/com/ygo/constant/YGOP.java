package com.ygo.constant;

/**
 * YGOP协议的常量
 *
 * @author Egan
 * @date 2018/5/19 15:44
 **/
public class YGOP {
    /// <summary>
    /// 当前版本
    /// </summary>
    public static final   float VERSION = 1.0F;
    /// <summary>
    /// 魔数
    /// </summary>
    public static final int MAGIC = 0x3cb;
    /// <summary>
    /// 数据包头的固定长度
    /// </summary>
    public static final int HEAD_LEN = 16;
    /// <summary>
    /// 最大内容长度
    /// </summary>
    public static final int MAX_LEN = 1024;
    /// <summary>
    /// 协议的段数
    /// </summary>
    public static final int PART_COUNT = 5;
    /// <summary>
    /// 版本所占字节数
    /// </summary>
    public static final int VERSION_LEN = 4;
    /// <summary>
    /// 消息类型所占字节数
    /// </summary>
    public static final int TYPE_LEN = 4;
    /// <summary>
    /// 魔数所占字节数
    /// </summary>
    public static final int MAGIC_LEN = 4;
    /// <summary>
    /// 消息体长度所占字节数
    /// </summary>
    public static final int LEN_LEN = 4;
    /// <summary>
    /// 版本的顺序
    /// </summary>
    public static final int VERSION_ORDER = 0;
    /// <summary>
    /// 类型的顺序
    /// </summary>
    public static final int TYPE_ORDER = 1;
    /// <summary>
    /// 魔数的顺序
    /// </summary>
    public static final int MAGIC_ORDER = 2;
    /// <summary>
    /// 消息体长度的顺序
    /// </summary>
    public static final int LEN_ORDER = 3;
    /// <summary>
    /// 消息体的顺序
    /// </summary>
    public static final int BODY_ORDER = 4;
    /// <summary>
    /// 版本的位置
    /// </summary>
    public static final int VERSION_POS = 0;
    /// <summary>
    /// 类型的位置
    /// </summary>
    public static final int TYPE_POS = VERSION_POS + VERSION_LEN;
    /// <summary>
    /// 魔数的位置
    /// </summary>
    public static final int MAGIC_POS = TYPE_POS + TYPE_LEN;
    /// <summary>
    /// 消息体长度的位置
    /// </summary>
    public static final int LEN_POS = MAGIC_POS + MAGIC_POS;
    /// <summary>
    /// 消息体的位置
    /// </summary>
    public static final int BODY_POS = LEN_POS + LEN_LEN;
}
