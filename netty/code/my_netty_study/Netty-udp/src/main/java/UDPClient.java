/**
 * @author HeShengjin 2356899074@qq.com
 * @Description UDP客户端
 * @createTime 2021年08月11日 15:30:00
 */

import handler.ClientChannelHandler;
import handler.ClientHeartbeat;
import handler.ClientHeartbeatHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class UDPClient {
    public static void main(String[] args) {
                    NioEventLoopGroup boss$work = new NioEventLoopGroup();
                    Bootstrap strap = new Bootstrap();
                    strap
                            .group(boss$work)
                            .channel(NioDatagramChannel.class)
                            .handler(new ChannelInitializer<Channel>() {
                                @Override
                                protected void initChannel(Channel channel) throws Exception {
                                    channel.pipeline().addLast(
                                            new LoggingHandler(LogLevel.INFO),
                                            new StringEncoder(),
                                            new StringDecoder());
                                    //心跳检测handler
                                    channel.pipeline().addLast("client-heartbeat",new ClientHeartbeatHandler());
                                    channel.pipeline().addLast("client-handler",new ClientChannelHandler());
                                }
                            });
                    try {
                        ChannelFuture future = strap.connect("127.0.0.1", 7777).sync();
                        future.channel().writeAndFlush("你好，我是UDP数据包。")
                                    .addListener(new GenericFutureListener<Future<? super Void>>() {
                                                @Override
                                                public void operationComplete(Future<? super Void> paramF)throws Exception {
                                                    System.out.println(String.format("发送数据包：%s",paramF.isSuccess()));
                                                    ClientHeartbeat.start();
                                                }
                                            });
                        // 阻塞
                        future.channel().closeFuture().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }  finally {
                        boss$work.shutdownGracefully();
                    }

                };

}

