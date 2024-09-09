package com.pancm.netty.server;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;


/**
 * 
* Title: NettyServerFilter
* Description: Netty 服务端过滤器
* Version:1.0.0  
* @author pancm
* @date 2017年10月26日
 */
public class NettyServerFilter extends ChannelInitializer<SocketChannel> {
 
     @Override
     protected void initChannel(SocketChannel ch) throws Exception {
         ChannelPipeline ph = ch.pipeline();
         //处理http服务的关键handler
         //HttpServerCodec包含：HttpResponseEncoder、HttpRequestDecoder
         //ph.addLast("httpServerCodec",new HttpServerCodec());
         ph.addLast("encoder",new HttpResponseEncoder());
         ph.addLast("decoder",new HttpRequestDecoder());
         //HttpObjectAggregator
         //当我们用POST方式请求服务器的时候，对应的参数信息是保存在message body中的,
         //如果只是单纯的用HttpServerCodec是无法完全的解析Http POST请求的，因为HttpServerCodec只能获取uri中参数，所以需要加上HttpObjectAggregator
         ph.addLast("aggregator", new HttpObjectAggregator(10*1024*1024)); 
         ph.addLast("handler", new NettyServerHandler());// 服务端业务逻辑
     }
 }
