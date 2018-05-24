package ygo.comn.model;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * 房间记录类
 * 根据玩家IP地址和端口号映射其所在的房间
 * 采用单例模式（懒汉模式）
 *
 * @author Egan
 * @date 2018/5/17 23:36
 **/
public class RoomRecord {

    private Map<InetSocketAddress, Room> record =
            new HashMap<>();

    public Map<InetSocketAddress, Room> getRecord() {
        return record;
    }

    public void setRecord(Map<InetSocketAddress, Room> record) {
        this.record = record;
    }

    /**
     * 将主机名和端口作为键，向记录映射中推入键值
     *
     * @date 2018/5/17 23:47
     * @param hostname 主机名
     * @param port 端口号
     * @param room 房间
     * @return boolean 键值对不存在，推入成功
     **/
    public boolean put(String hostname, int port, Room room){
        InetSocketAddress key = new InetSocketAddress(hostname, port);
        if(record.get(key) != null)
            return false;
        record.put(key, room);
        return true;
    }

    /**
     * 将主机名和端口作为键，删除记录映射中的键值对
     *
     * @date 2018/5/18 0:11
     * @param hostname 主机名
	 * @param port 端口号
	 * @param room 房间
     * @return boolean 键值对存在，删除成功
     **/
    public boolean remove(String hostname, int port, Room room){
        return record.remove(new InetSocketAddress(hostname, port)) != null;
    }
}
