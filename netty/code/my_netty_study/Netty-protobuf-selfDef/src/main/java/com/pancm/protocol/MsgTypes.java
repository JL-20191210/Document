package com.pancm.protocol;

/**
 * @author HeShengjin 2356899074@qq.com
 * @Description 消息type指令类型等， 类型，1 = login 、2 = single、3 = mutiple、4 = quit、5 = server_ack
 * @createTime 2021年07月29日 09:14:00
 */
public enum MsgTypes {

    TYPE_LOGIN(1,"login"),
    TYPE_SINGLE(2,"single"),
    TYPE_MUTIPLE(3,"mutiple"),
    TYPE_QUIT(4,"quit"),
    TYPE_SERVER_ACK(5,"ack");

    //指令
    //类型，1 = login 、2 = single、3 = mutiple、4 = quit、5 = server_ack
    private int msgType;
    private String msgTypeName;


    MsgTypes(int msgType, String msgTypeName) {
        this.msgType = msgType;
        this.msgTypeName = msgTypeName;
    }

    public String getMsgTypeName() {
        return msgTypeName;
    }

    public void setMsgTypeName(String msgTypeName) {
        this.msgTypeName = msgTypeName;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
}
