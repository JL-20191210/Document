package cn.itcast.xml;

import com.alibaba.fastjson.JSONObject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;

/**
 * @author hsj
 * @description:测试netty接受数据包
 * @date 2021/8/3 12:15
 */
@Slf4j
public class XmlJsonServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
                ServerBootstrap serverBootstrap = new ServerBootstrap()
                .group(boss, worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch){
                        // 日志
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new HttpServerCodec());
                        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(10*1024*1024));
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
                                   log.debug("*************接受到一个数据包(xml已解析)*****************");
                                   log.info(getBody(msg).toString());
                                   log.debug("*************END****************************");
                            }
                            /**
                             * 获取body参数
                             * @param request
                             * @return
                             */
                            private String getBody(FullHttpRequest request){
                                ByteBuf buf = request.content();
                                return buf.toString(CharsetUtil.UTF_8);
                            }
                        });
                        //http xml解密json
//                        ch.pipeline().pipelineaddLast(new XmlJsonDecoder());

                        // 读取数据
//                        ch.pipeline().addLast( new SimpleChannelInboundHandler<JSONObject>() {
//                            @Override                                         // ByteBuf -> JSONObject
//                            public void channelRead0(ChannelHandlerContext ctx, JSONObject msg) throws Exception {
//                               try {
//                                   log.debug("*************接受到一个数据包(xml已解析->json)*****************");
//                                   log.info(msg.toJSONString());
//                                   log.debug("*************END****************************");
//                               } catch (Exception e){
//                                   e.printStackTrace();
//                                   log.error(e.getMessage());
//                               } finally{
//                                   ReferenceCountUtil.release(msg);
//                               }
//                            }
//                        });
                    }
                });
                // 绑定 9988
                Channel channel = serverBootstrap.bind(9988).sync().channel();
                channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("netty 发生错误：", e.getCause().getMessage());
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
