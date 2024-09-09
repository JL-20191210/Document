package handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

/**
 * @author hsj
 * @description:心跳
 * @date 2021/8/11 17:26
 */
public class ClientHeartbeatHandler  extends ChannelInboundHandlerAdapter {

    /**
     * @Description:read
     * @author HeShengjin 2356899074@qq.com
     * @date 2021/8/11 17:08
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        DatagramPacket dp = (DatagramPacket) msg;
        ClientHeartbeat.offer(dp.sender().toString(),System.currentTimeMillis());
        //不释放，继续传递
        ctx.fireChannelRead(msg);
    }
}
