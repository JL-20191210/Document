package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * @author hsj
 * @description:handler心跳
 * @date 2021/8/11 15:38
 */
public class ClientChannelHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    /**
     * @Description:client心跳数据包
     * @author HeShengjin 2356899074@qq.com
     * @date 2021/8/11 15:39
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
            byte[] c = new byte[msg.content().readableBytes()];
            msg.content().readBytes(c);
            System.out.println(String.format("client接受到服务器心跳回应数据包：%s",new String(c, StandardCharsets.UTF_8)));
            InetSocketAddress serverIp = msg.sender();//服务端的IP地址
            ctx.writeAndFlush(
                    new DatagramPacket(
                            ctx.alloc().ioBuffer().writeBytes("client心跳数据包".getBytes()),
                            serverIp
                    )
            );
            //不再传递
            //SimpleChannelInboundHandler.channelRead(ChannelHandlerContext ctx, Object msg)负责释放
    }
}
