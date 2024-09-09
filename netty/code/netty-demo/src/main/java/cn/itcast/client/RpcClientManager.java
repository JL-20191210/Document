package cn.itcast.client;

import cn.itcast.client.handler.RpcResponseMessageHandler;
import cn.itcast.message.RpcRequestMessage;
import cn.itcast.protocol.MessageCodecSharable;
import cn.itcast.protocol.ProcotolFrameDecoder;
import cn.itcast.protocol.SequenceIdGenerator;
import cn.itcast.server.service.HelloService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Proxy;
/**
 * @Description: 代理的真相:https://mp.weixin.qq.com/s/AiZqQjXdkK0k0gekiBIVFg
 * //当你写下下面接口代码时候
 * public interface IUserService{
 *     Object login(String username, String password);
 * }
 *
 * //当你生成代理对象时候
 * public class Test{
 *     public static void main(String[] args){
 *
 *         IUserService userService = (IUserService) Proxy.newProxyInstance(IUserService.class.getClassLoader(),
 *                 new Class[]{IUserService.class},
 *                 new InvocationHandler() {
 *                     @Override
 *                     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 *                         System.out.println("method = " + method.getName() +" , args = " + Arrays.toString(args));
 *                         return null;
 *                     }
 *                 });
 *
 *         userService.login("zhy","123");
 *     }
 * }
 *
 * //那么实际jdk帮我们生成的代码呢，哈哈，如下：
 *
 * public class Proxy{
 *     protected InvocationHandler h;
 * }
 *
 * public final class $Proxy0 extends Proxy implements IUserService{
 *
 *     public $Proxy0(InvocationHandler invocationhandler){
 *         super(invocationhandler);
 *     }
 *
 *     public final Object login(String s, String s1){
 *         return super.h.invoke(this, m3, new Object[] {
 *             s, s1
 *         });
 *     }
 *
 *     private static Method m3;
 *
 *     static {
 *         m3 = Class.forName("IUserService").getMethod("login", new Class[] {
 *             Class.forName("java.lang.String"), Class.forName("java.lang.String")
 *         });
 *     }
 *
 * }
 * @author HeShengjin 2356899074@qq.com
 * @date 2021/7/30 16:47
 */
@Slf4j
public class RpcClientManager {


    public static void main(String[] args) {
        HelloService service = getProxyService(HelloService.class);
        System.out.println(service.sayHello("zhangsan"));
//        System.out.println(service.sayHello("lisi"));
//        System.out.println(service.sayHello("wangwu"));
    }

    // 创建代理类
    public static <T> T getProxyService(Class<T> serviceClass) {
        ClassLoader loader = serviceClass.getClassLoader();
        Class<?>[] interfaces = new Class[]{serviceClass};
        //                                                            sayHello  "张三"
        Object o = Proxy.newProxyInstance(loader, interfaces, (proxy, method, args) -> {
            // 1. 将方法调用转换为 消息对象
            int sequenceId = SequenceIdGenerator.nextId();
            RpcRequestMessage msg = new RpcRequestMessage(
                    sequenceId,
                    serviceClass.getName(),
                    method.getName(),
                    method.getReturnType(),
                    method.getParameterTypes(),
                    args
            );
            // 2. 将消息对象发送出去
            getChannel().writeAndFlush(msg);

            // 3. 准备一个空 Promise 对象，来接收结果             指定 promise 对象异步接收结果线程
            DefaultPromise<Object> promise = new DefaultPromise<>(getChannel().eventLoop());
            RpcResponseMessageHandler.PROMISES.put(sequenceId, promise);

//            promise.addListener(future -> {
//                // 线程
//            });

            // 4. 等待 promise 结果
            promise.await();
            if(promise.isSuccess()) {
                // 调用正常
                return promise.getNow();
            } else {
                // 调用失败
                throw new RuntimeException(promise.cause());
            }
        });
        return (T) o;
    }

    private static Channel channel = null;
    private static final Object LOCK = new Object();

    // 获取唯一的 channel 对象
    public static Channel getChannel() {
        if (channel != null) {
            return channel;
        }
        synchronized (LOCK) { //  t2
            if (channel != null) { // t1
                return channel;
            }
            initChannel();
            return channel;
        }
    }

    // 初始化 channel 方法
    private static void initChannel() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcResponseMessageHandler RPC_HANDLER = new RpcResponseMessageHandler();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(group);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ProcotolFrameDecoder());
                ch.pipeline().addLast(LOGGING_HANDLER);
                ch.pipeline().addLast(MESSAGE_CODEC);
                ch.pipeline().addLast(RPC_HANDLER);
            }
        });
        try {
            channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().addListener(future -> {
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            log.error("client error", e);
        }
    }
}
