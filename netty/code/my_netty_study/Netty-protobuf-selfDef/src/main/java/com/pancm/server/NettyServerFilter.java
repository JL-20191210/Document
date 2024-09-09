package com.pancm.server;

import com.pancm.handler.UserMsgSingleServerHandler;
import com.pancm.protocol.MessageCodecSharable;
import com.pancm.protocol.ProcotolFrameDecoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * ◎编码过程: 通过ProtobufVarint32LengthFieldPrepender 将整个消息体的长度作为
 * 消息头附加在消息体，然后使用ProtobufEncoder进行编码，编码完成后将编码
 * 结果字节数组通过Netty进行传输。
 *
 * ◎解码过程: 接受消息的时候，ProtobufVarint32FrameDecoder 先接受消息头，获取
 * 消息体的字节数组长度，直到获取到等于消息字节数组长度的字节数，才使用
 * ProtobufDecoder进行解码操作。
 *
 * @Title: HelloServerInitializer
 * @Description: Netty 服务端过滤器
 * @Version:1.0.0  
 * @author pancm
 * @date 2017年10月8日
  */
public class NettyServerFilter extends ChannelInitializer<SocketChannel> {
	
     @Override
     protected void initChannel(SocketChannel ch) throws Exception {
         ChannelPipeline ph = ch.pipeline();

         // 解码和编码，应和客户端一致
         //传输的协议 Protobuf
         ph.addLast("LengthFieldBasedFrameDecoder", new ProcotolFrameDecoder());
         ph.addLast("decoc-protobuf",new MessageCodecSharable());
         //业务逻辑实现类
         ph.addLast("UserMsgSingleServerHandler", new UserMsgSingleServerHandler());
     }
 }
