package com.pancm.utils;/**
 * @author HeShengjin 2356899074@qq.com
 * @Description TODO
 * @createTime 2021年07月29日 15:47:00
 */

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pancm.protobuf.UserInfo;
import com.pancm.protocol.MsgTypeFeilds;
import com.pancm.protocol.MsgTypes;

import java.util.Iterator;
import java.util.Map;

/**
 * @author hsj
 * @description:message工具
 * @date 2021/7/29 15:47
 */
public final class MessageUtil {
    /**
     * @Description:protobuf获取消息的类型type
     * @author HeShengjin 2356899074@qq.com
     * @date 2021/7/29 15:36
     */
    public static Integer parseMessageFeildTypeFromProtobufMessage(GeneratedMessageV3 userMsgSingle) {
        Map<Descriptors.FieldDescriptor, Object> map = userMsgSingle.getAllFields();
        Iterator<Map.Entry<Descriptors.FieldDescriptor, Object>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Descriptors.FieldDescriptor, Object> next = it.next();
            if (next.getKey().getJsonName().equals(MsgTypeFeilds.TYPE.getName())) {
                return (Integer) next.getValue();
            }
        }
        return null;
    }

    /**
     * @Description:根据消息类型反序列化
     * @author HeShengjin 2356899074@qq.com
     * @date 2021/7/29 15:57
     */
    public static GeneratedMessageV3 parseMessageType2Message(int messageType, byte[] bytes) throws InvalidProtocolBufferException {
        if (MsgTypes.TYPE_LOGIN.getMsgType() == messageType) {
            return UserInfo.UserMsgLogin.parseFrom(bytes);
        }
        if (MsgTypes.TYPE_SINGLE.getMsgType() == messageType) {
            return UserInfo.UserMsgSingle.parseFrom(bytes);
        }
        if (MsgTypes.TYPE_MUTIPLE.getMsgType() == messageType) {
            return UserInfo.UserMsgMutiple.parseFrom(bytes);
        }
        if (MsgTypes.TYPE_QUIT.getMsgType() == messageType) {
            return UserInfo.UserMsgQuit.parseFrom(bytes);
        }
        if (MsgTypes.TYPE_SERVER_ACK.getMsgType() == messageType) {
            return UserInfo.UserMsgServerAck.parseFrom(bytes);
        }
        return null;
    }
}
