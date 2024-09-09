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
 * @description:ACK聊天handler
 * @date 2021/7/29 10:13
 */
@ChannelHandler.Sharable
@Slf4j
public class UserMsgServerAckClientHandler extends SimpleChannelInboundHandler<UserInfo.UserMsgServerAck> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UserInfo.UserMsgServerAck msg) throws Exception {
        //自动释放ByteBuf
         log.info("接受到服务器ACK的protobuf消息，{}",msg.getAllFields());
    }
}
