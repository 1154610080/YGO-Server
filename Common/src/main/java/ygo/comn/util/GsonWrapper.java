//package ygo.comn.util;
//
//import com.google.gson.ExclusionStrategy;
//import com.google.gson.FieldAttributes;
//import com.google.gson.GsonBuilder;
//import io.netty.util.CharsetUtil;
//
//import java.lang.reflect.Type;
//
///**
// * Gson封装类
// * 统一设置字段忽略策略
// *
// * @author Egan
// * @date 2018/5/12 10:26
// **/
//
//public class GsonWrapper{
//
//    private GsonBuilder builder;
//
//    public GsonWrapper(){
//        builder = new GsonBuilder();
//        builder.excludeFieldsWithoutExposeAnnotation();
//    }
//
//
//    /**
//     * 将对象转换成json字符串
//     *
//     * @date 2018/5/17 19:40
//     * @param object 需要转换的对象
//     * @return byte[] json字符串
//     **/
//    public String toJsonStr(Object object){
//        return builder.create().toJson(object);
//    }
//
//    /**
//     * 将对象转换成json
//     *
//     * @date 2018/5/17 19:40
//     * @param object 需要转换的对象
//     * @return byte[] json字节数组
//     **/
//    public byte[] toJson(Object object){
//        return builder.create().toJson(object).getBytes(CharsetUtil.UTF_8);
//    }
//
//    /**
//     * 将json字节数组转换成目标类的实例
//     *
//     * @date 2018/5/17 19:35
//     * @param json json字节数组
//     * @param type 目标类类型
//     * @return T
//     **/
//    public <T> T toObject(byte[] json, Type type){
//        return builder.create().fromJson(new String(json), type);
//    }
//
//    /**
//     * 将json字符串转换成目标类的实例
//     *
//     * @date 2018/5/21 8:27
//     * @param json json字符串
//	 * @param type 目标类类型
//     * @return T
//     **/
//    public <T> T toObject(String json, Type type){
//        return builder.create().fromJson(json, type);
//    }
//
//
//}
