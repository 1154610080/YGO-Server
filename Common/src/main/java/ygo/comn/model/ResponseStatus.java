package ygo.comn.model;

import ygo.comn.constant.StatusCode;

import java.util.HashMap;

/**
 * 响应状态类
 *
 * @author Egan
 * @date 2018/5/12 9:15
 **/
public class ResponseStatus extends HashMap<String, Object>{

    public ResponseStatus(){

    }

    public ResponseStatus(int code){
        super.put("code", code);
    }

    public ResponseStatus(StatusCode status){
        super.put("code", status.getCode());
    }


}
