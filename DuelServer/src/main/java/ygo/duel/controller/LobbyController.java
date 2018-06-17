package ygo.duel.controller;

import io.netty.channel.Channel;
import org.omg.CORBA.TIMEOUT;
import ygo.comn.constant.Secret;
import ygo.comn.constant.StatusCode;
import ygo.comn.constant.YGOP;
import ygo.comn.controller.AbstractController;
import ygo.comn.controller.redis.RedisFactory;
import ygo.comn.model.DataPacket;
import ygo.comn.model.GlobalMap;
import ygo.comn.util.YgoLog;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 大厅控制器
 * 
 * @author Egan
 * @date 2018/6/18 1:46
 **/
public class LobbyController extends AbstractController{

    public LobbyController(DataPacket packet, Channel channel) {
        super(packet, channel);
    }

    @Override
    protected void assign() {
        address = new InetSocketAddress(Secret.DUEL_PORT);
        redis = RedisFactory.getRedisforDuel(address);
        log = new YgoLog("Lobby-Controller");

        switch (packet.getType()) {
            case COUNT_DOWN:
                countdown();
                break;
            default:
                log.error(StatusCode.ERROR_CONTROLLER, "大厅控制器不能处理该消息类型 " + packet.getType());
                break;
        }
    }

    private void countdown(){
        int id = Integer.parseInt(packet.getBody());
        if(GlobalMap.getTimer(id) == null){
            Timer timer = new Timer();
            GlobalMap.addTimer(id, timer);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(this.scheduledExecutionTime() >= YGOP.TIME_OUT){
                        //房间内的玩家都未在规定时间内连接服务器
                        log.error(StatusCode.TIME_OUT, "房间 " + id +" 等待超时");
                        //删除房间
                        redis.removeRoom(id);
                    }
                }
            }, 0, YGOP.TIME_OUT);
        }
    }
}
