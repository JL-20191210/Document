package com.pancm.client;

import com.pancm.handler.ProtobufOneOfClientHandler;
import com.pancm.protobuf.MutipleMessageInfo;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

/**
 * ◎编码过程: 通过ProtobufVarint32LengthFieldPrepender 将整个消息体的长度作为
 * 消息头附加在消息体，然后使用ProtobufEncoder进行编码，编码完成后将编码
 * 结果字节数组通过Netty进行传输。
 *
 * ◎解码过程: 接受消息的时候，ProtobufVarint32FrameDecoder 先接受消息头，获取
 * 消息体的字节数组长度，直到获取到等于消息字节数组长度的字节数，才使用
 * ProtobufDecoder进行解码操作。
*
* @Title: NettyClientFilter
* @Description: Netty客户端 过滤器
* @Version:1.0.0  
* @author pancm
* @author hsj
* @date 2017年10月8日
*/
public class NettyClientFilter extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline ph = ch.pipeline();

        //传输的协议 Protobuf
        //解码
        ph.addLast(new ProtobufVarint32FrameDecoder());
        ph.addLast(new ProtobufDecoder(MutipleMessageInfo.Person.getDefaultInstance()));
        //编码
        ph.addLast(new ProtobufVarint32LengthFieldPrepender());
        ph.addLast(new ProtobufEncoder());
       
        //业务逻辑实现类
        ph.addLast(new ProtobufOneOfClientHandler());

    }
}
