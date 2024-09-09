/**
 * @author HeShengjin 2356899074@qq.com
 * @Description UDP服务端
 * @createTime 2021年08月11日 15:26:00
 */

import handler.ServerChannelHandler;
import handler.ServerHeartbeat;
import handler.ServerHeartbeatChannelHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class UDPServer {
    public static void main(String[] args) {
        Bootstrap strap = new Bootstrap();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        strap
                .channel(NioDatagramChannel.class)
                .group(workerGroup)
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel)throws Exception {
                            channel.pipeline().addLast(
                                    new LoggingHandler(LogLevel.INFO),
                                    new StringEncoder(),
                                    new StringDecoder());
                            //心跳检测handler
                            channel.pipeline().addLast("server-heartbeat",new ServerHeartbeatChannelHandler());
                            channel.pipeline().addLast("server-handler",new ServerChannelHandler());
                    }
                });
        try {
            ChannelFuture future = strap.bind(7777).sync();
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    ServerHeartbeat.start();
                }
            });
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
