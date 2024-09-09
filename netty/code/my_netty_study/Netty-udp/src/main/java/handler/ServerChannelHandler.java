package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author HeShengjin 2356899074@qq.com
 * @Description handler心跳
 * @createTime 2021年08月11日 15:27:00
 */

public class ServerChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
            byte[] c = new byte[msg.content().readableBytes()];
            msg.content().readBytes(c);
            System.out.println(String.format("服务器接受到client心跳数据包：%s",new String(c, StandardCharsets.UTF_8)));
            InetSocketAddress clientIp = msg.sender();//客户端IP地址
            ctx.writeAndFlush(
                    new DatagramPacket(
                            ctx.alloc().ioBuffer().writeBytes("服务器ACK".getBytes()),
                            clientIp
                    )
            );
           //不再传递
           //SimpleChannelInboundHandler.channelRead(ChannelHandlerContext ctx, Object msg)负责释放
    }
}
