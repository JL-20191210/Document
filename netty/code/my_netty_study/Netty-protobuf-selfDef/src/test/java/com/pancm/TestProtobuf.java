package com.pancm;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.google.protobuf.*;
import com.pancm.protobuf.UserInfo;
import com.pancm.protocol.Constant;
import com.pancm.protocol.MsgTypeFeilds;
import com.pancm.protocol.MsgTypes;
import com.pancm.protocol.SequenceIdGenerator;
import com.pancm.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * description:测试序列化和反序列化
 * @author HeShengjin 2356899074@qq.com
 * @date 2021/7/28 0028 22:00
 */
@Slf4j
public class TestProtobuf {
    public static void main(String[] args) throws NoSuchFieldException {
        try {
            /*UserInfo.UserMsgSingle*/GeneratedMessageV3 userMsgSingle = UserInfo.UserMsgSingle.newBuilder()
                    .setId(SequenceIdGenerator.nextId())
                    .setType(MsgTypes.TYPE_SINGLE.getMsgType())
                    .setContent("内容")
                    .setFromId(Constant.FROM_ZHANGSAN_ID)
                    .setFromName(Constant.FROM_ZHANGSAN_NAME)
                    .setToId(Constant.TO_LISI_ID)
                    .setToName(Constant.TO_LISI_NAME)
                    .build();
            //序列化成字节
            byte[] bytes = userMsgSingle.toByteArray();
            log.info("protobuf 序列化成字节,长度：{}",bytes.length);
            Integer typeValue = MessageUtil.parseMessageFeildTypeFromProtobufMessage(userMsgSingle);
            log.info("type is:{}",typeValue.intValue());

            log.info("////////////////////////////////////////////////////////////////");
            //反序列化成java对象
            UserInfo.UserMsgSingle userMsgSingleDecode  = UserInfo.UserMsgSingle.parseFrom(bytes);
            log.info("protobuf 反序列化成java对象：{}", userMsgSingleDecode.getAllFields());

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }


}
