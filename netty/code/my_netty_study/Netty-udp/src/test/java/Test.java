import io.netty.channel.DefaultEventLoop;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author hsj
 * @description:  DefaultEventLoop
 * @date 2021/8/11 17:35
 */
public class Test {
    public static void main(String[] args) {
        DefaultEventLoop hearbitCheckLoop = new DefaultEventLoop();
        //心跳检测1分钟一次检测
        hearbitCheckLoop.scheduleAtFixedRate(()->{
            System.out.println("******************************");
            System.out.println("*****心跳检测1分钟一次检测*****");
            System.out.println("******************************");
        },0,1, TimeUnit.MINUTES);
    }
}
