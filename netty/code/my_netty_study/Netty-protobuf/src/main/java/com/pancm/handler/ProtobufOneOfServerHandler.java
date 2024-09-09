package com.pancm.handler;

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
 * @date 2021/7/29 17:27
 */
@ChannelHandler.Sharable
@Slf4j
public class ProtobufOneOfServerHandler extends SimpleChannelInboundHandler<MutipleMessageInfo.Person> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MutipleMessageInfo.Person msg) throws Exception {
        MutipleMessageInfo.Person.DataType dataType = msg.getDataType();
        switch (dataType) {
            case StudentType:
                MutipleMessageInfo.Student sutdent = msg.getStudent();
                log.info("Student类型：{}",sutdent.getAllFields());

                MutipleMessageInfo.Person person = getPerson4Teacher();
                //发送消息老师
                ctx.channel().writeAndFlush(person);
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

    private MutipleMessageInfo.Person getPerson4Teacher() {
        MutipleMessageInfo.Person person = MutipleMessageInfo.Person.newBuilder()
                .setDataType(MutipleMessageInfo.Person.DataType.TeacherType)
                .setTeacher(
                        MutipleMessageInfo.Teacher.newBuilder()
                                .setId(SequenceIdGenerator.nextId())
                                .setTId(Constant.TO_TEACHER_ID)
                                .setTName(Constant.TO_TEACHER_NAME)
                                .build()
                )
                .build();
        return person;
    }
}
