package com.pancm.protocol;

/**
 * @author HeShengjin 2356899074@qq.com
 * @Description 消息的字段域
 * @createTime 2021年07月29日 15:43:00
 */
public enum MsgTypeFeilds {
    //字段域type
    TYPE("type");

    private String name;

    MsgTypeFeilds(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
