package ygo.loby.model;

import com.google.gson.annotations.Expose;
import ygo.comn.model.Room;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 游戏大厅类
 * 采用线程安全的单列模式
 *
 * @author EganChen
 * @date 2018/4/16 14:15
 */
public class GameLobby {

    private static volatile GameLobby lobby;

    public static GameLobby getLobby() {
        if(lobby == null){
            synchronized (GameLobby.class){
                if (lobby == null)
                    lobby = new GameLobby();
            }
        }
        return lobby;
    }

    @Expose()
    @SerializedName("mx")
    private final int MAXIMUM = 128;

    @Expose()
    @SerializedName("rm")
    private List<Room> rooms = new LinkedList<>();

    private Map<Integer, Room> roomMap = new HashMap<>();

    public int getMAXIMUM() {
        return MAXIMUM;
    }

    public void addRoom(int index, Room room) {
        rooms.add(index, room);
        roomMap.put(room.getId(), room);
    }

    public void addRoom(Room room) {
        rooms.add(room);
        roomMap.put(room.getId(), room);
    }

    public int size(){
        return rooms.size();
    }

    public void removeRooms(int id) {
        rooms.remove(id);
        roomMap.remove(id);
    }

    public Room getRoomByIndex(int index) {
        return rooms.get(index);
    }

    public Room getRoomById(int id){
        return roomMap.get(id);
    }

    public boolean contains(int id){
        return roomMap.containsKey(id);
    }
}
