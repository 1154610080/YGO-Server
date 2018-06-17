package ygo.comn.controller;

import ygo.comn.controller.redis.RedisClient;
import ygo.comn.controller.redis.RedisFactory;
import ygo.comn.model.GlobalMap;

import java.util.Scanner;

/**
 * 控制台类
 *
 * @author Egan
 * @date 2018/6/14 17:40
 **/
public class Console {

    private RedisClient redis;

    public Console(RedisClient redis){
        this.redis = redis;
    }

    public Console(){}

    public void start(){

        Scanner scanner = new Scanner(System.in);
        while (true){
            String str = scanner.nextLine();

            if("close".equals(str)){
                break;
            }

            switch (str){
                case "channel -s":
                    System.out.println(GlobalMap.channels().size());
                    break;
                case "redis -c":
                    System.out.println(RedisFactory.redisCount());
                    break;
            }

        }
    }


}
