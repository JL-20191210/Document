package com.pancm.handler;

import com.pancm.protobuf.UserInfo;
import com.pancm.protocol.Constant;
import com.pancm.protocol.MsgTypes;
import com.pancm.protocol.SequenceIdGenerator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hsj
 * @description:single聊天handler
 * @date 2021/7/29 10:13
 */
@ChannelHandler.Sharable
@Slf4j
public class UserMsgSingleServerHandler extends SimpleChannelInboundHandler<UserInfo.UserMsgSingle> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserInfo.UserMsgSingle msg) throws Exception {
        //自动释放ByteBuf
        //ACK时候，client不再发起消息
        log.info("接受到client的single聊天protobuf消息，{}",msg.getAllFields());

        //server回复client消息ACK
        UserInfo.UserMsgServerAck userMsgServerAck = UserInfo.UserMsgServerAck.newBuilder()
                //消息id
                .setId(SequenceIdGenerator.nextId())
                //ACK
                .setType(MsgTypes.TYPE_SERVER_ACK.getMsgType())
                .build();
        ctx.channel().writeAndFlush(userMsgServerAck);
    }
}
