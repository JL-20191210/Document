package cn.itcast.xml;
/**
 * @author hsj
 * @description:xml解码器
 * @date 2021/8/3 13:41
 */

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class XmlJsonDecoder extends ByteToMessageDecoder {
    //XML数据
    String lastStr = "";
    //XML开始DOM节点
    final static String SATRT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    //XML结束DOM节点
    final static String END_XML = "</SOAP-ENV:Envelope>";
    //至少四个字符<??>
    final static String LESS = "<??>";

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //至少四个字符
        //<??>
        int len = in.readableBytes();
        if (len < LESS.length()) return;

        byte[] bytes = new byte[len];
        in.getBytes(0, bytes);

        String temp = new String(bytes, StandardCharsets.UTF_8);
        int startIndex = temp.indexOf(SATRT_XML);
        if(startIndex != -1){
            //丢弃非XML开始DOM节点
            temp = temp.substring(startIndex,temp.length());
        }
        int index = temp.indexOf(END_XML);//这里为XML结束标签，自行替换
        if (index == -1) {
            lastStr += temp;
        } else {
            lastStr += temp.substring(0, index + END_XML.length());//20为结束标签所占字节位数
            Document doc= DocumentHelper.parseText(lastStr);
            JSONObject json = new JSONObject();
            Xml2JsonUtil.dom4j2Json(doc.getRootElement(),json);
            out.add(json);
            //重置XML数据
            lastStr = "";
        }
        //跳过len字节
        in.skipBytes(len);
    }

}
