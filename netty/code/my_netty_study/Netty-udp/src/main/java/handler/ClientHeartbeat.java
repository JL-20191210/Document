package handler;

import io.netty.channel.DefaultEventLoop;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author hsj
 * @description:心跳
 * @date 2021/8/11 17:57
 */
public final class ClientHeartbeat {
    //KEY-IP地址
    //VALUE-数据包接受到的时间
    private final static Map<String, Long> allServers = new ConcurrentHashMap<String, Long>();
    private final static DefaultEventLoop hearbitCheckLoop = new DefaultEventLoop();

    public final static void start() {
        //心跳检测1分钟一次检测
        hearbitCheckLoop.scheduleAtFixedRate(()->{
            System.out.println("******************************");
            System.out.println("*****心跳检测1分钟一次检测*****");
            System.out.println("******************************");
            Set<String> keys = allServers.keySet();
            Iterator<String> mIterator = keys.iterator();
            while (mIterator.hasNext()) {
                String ip = mIterator.next();
                long lastTimePack = allServers.get(ip);
                //30秒未收到UDP数据包，就认为掉线
                if (lastTimePack > 0 && System.currentTimeMillis() - lastTimePack > 30 * 1000) {
                    mIterator.remove();
                    System.out.println("server: " + ip + "掉线,已经30秒没有接受到服务器心跳回应数据包！");
                }
            }
        },0,1, TimeUnit.MINUTES);
    }

    public final static void offer(String serverIp, Long timeMills) {
        allServers.put(serverIp,timeMills);
    }
}
