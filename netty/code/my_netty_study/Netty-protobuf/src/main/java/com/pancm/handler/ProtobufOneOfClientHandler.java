package com.pancm.handler;/**
 * @author HeShengjin 2356899074@qq.com
 * @Description TODO
 * @createTime 2021年07月29日 17:39:00
 */

import com.pancm.protobuf.MutipleMessageInfo;
import com.pancm.protocol.Constant;
import com.pancm.protocol.SequenceIdGenerator;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hsj
 * @description:多个protobuf的类handler
 * @date 2021/7/29 17:39
 */
@ChannelHandler.Sharable
@Slf4j
public class ProtobufOneOfClientHandler extends SimpleChannelInboundHandler<MutipleMessageInfo.Person> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MutipleMessageInfo.Person msg) throws Exception {
            MutipleMessageInfo.Person.DataType dataType = msg.getDataType();
            switch (dataType) {
                case StudentType:
                    MutipleMessageInfo.Student sutdent = msg.getStudent();
                    log.info("Student类型：{}",sutdent.getAllFields());
                    break;
                case TeacherType:
                    MutipleMessageInfo.Teacher teacher = msg.getTeacher();
                    log.info("Teacher类型：{}",teacher.getAllFields());
                    break;
                case LeaderType:
                    MutipleMessageInfo.Leader leader = msg.getLeader();
                    log.info("Leader类型：{}",leader.getAllFields());
                    break;
                default:
                    log.error("未找到protobuf类型");
            }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        MutipleMessageInfo.Person person = getPerson4Student();
        //连接上发送消息学生
        ctx.channel().writeAndFlush(person);
    }

    private MutipleMessageInfo.Person getPerson4Student() {
        MutipleMessageInfo.Person person = MutipleMessageInfo.Person.newBuilder()
                    .setDataType(MutipleMessageInfo.Person.DataType.StudentType)
                    .setStudent(
                            MutipleMessageInfo.Student.newBuilder()
                            .setId(SequenceIdGenerator.nextId())
                            .setStuId(Constant.FROM_STUDENT_ID)
                            .setStuName(Constant.FROM_STUDENT_NAME)
                            .build()
                    )
                    .build();
        return person;
    }
}
