package com.ygo.model;

public class Token {

    /**
     * Token Id, 由客户端IP地址和端口号构成
     **/
    private String ID;

    /**
     * Token 值
     **/
    private String value;

    /**
     * 更新时间
     **/
    private Long updateTime;

    /**
     * 过期时间
     **/
    private Long expireTime;

    public Token(){}

    public Token(String ID, String value, Long updateTime, Long expireTime) {
        this.ID = ID;
        this.value = value;
        this.updateTime = updateTime;
        this.expireTime = expireTime;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }
}
