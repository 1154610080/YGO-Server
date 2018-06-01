package ygo.comn.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ygo.comn.constant.StatusCode;
import ygo.comn.constant.YGOP;

public class YgoLog{

    private static Log log;

    public YgoLog(String name) {

        log = LogFactory.getLog(name);

    }

    public void error(StatusCode code, String msg){
        log.error(new String(("(" + code + ")" + msg + "\n").getBytes(), YGOP.CHARSET));
    }

    public void warn(StatusCode code, String msg){
        log.warn(new String(("(" + code + ")" + msg + "\n").getBytes(), YGOP.CHARSET));
    }


    public void info(StatusCode code, String msg){
        log.info(new String(("(" + code + ")" + msg + "\n").getBytes(), YGOP.CHARSET));
    }

    public void fatal(String msg){
        log.fatal(new String((msg + "\n").getBytes(), YGOP.CHARSET));
    }

}
