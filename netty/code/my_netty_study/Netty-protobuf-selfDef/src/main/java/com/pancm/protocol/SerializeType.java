package com.pancm.protocol;

/**
 * @author HeShengjin 2356899074@qq.com
 * @Description 序列化类型
 * @createTime 2021年07月29日 09:14:00
 */
public enum SerializeType {

    PROTOBUF(0);

    //字节的序列化方式 protobuf 0
    private int serializeType;


    SerializeType(int serializeType) {
        this.serializeType = serializeType;
    }

    public int getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(int serializeType) {
        this.serializeType = serializeType;
    }


}
