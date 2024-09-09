package com.pancm.protocol;

import com.google.protobuf.BytesValue;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import com.pancm.protobuf.UserInfo;
import com.pancm.utils.MessageUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@ChannelHandler.Sharable
/**
 * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接到的 ByteBuf 消息是完整的
 */
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, GeneratedMessageV3> {
    /**
     * @Description:protobuf编码
     * @author HeShengjin 2356899074@qq.com
     * @date 2021/7/29 9:51
     */
    @Override
    public void encode(ChannelHandlerContext ctx, GeneratedMessageV3 msg, List<Object> outList) throws Exception {
        ByteBuf out = ctx.alloc().buffer();
        // 1. 4 字节的魔数
        out.writeBytes(new byte[]{1, 2, 3, 4});
        // 2. 1 字节的版本,
        out.writeByte(1);
        // 3. 1 字节的序列化方式 protobuf 0
        out.writeByte(SerializeType.PROTOBUF.getSerializeType());
        // 4. 4 字节的消息类型
        out.writeInt(MessageUtil.parseMessageFeildTypeFromProtobufMessage(msg).intValue());

        // 5. 获取protobuf的字节数组
        byte[] bytes = msg.toByteArray();
        // 6. 长度
        out.writeInt(bytes.length);
        // 7. 写入protobuf
        out.writeBytes(bytes);
        outList.add(out);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1. 4 字节的魔数
        int magicNum = in.readInt();
        // 2. 1 字节的版本,
        byte version = in.readByte();
        // 3. 1 字节的序列化方式 protobuf 0
        byte serializerAlgorithm = in.readByte(); // 0
        // 4. 4 字节的消息类型
        int messageType = in.readInt();

        // 5. 长度
        int length = in.readInt();
        byte[] bytes = new byte[length];
        // 6. 读取protobuf字节数组
        in.readBytes(bytes, 0, length);

        //反序列化成java对象
        out.add(MessageUtil.parseMessageType2Message(messageType, bytes));
    }

}
