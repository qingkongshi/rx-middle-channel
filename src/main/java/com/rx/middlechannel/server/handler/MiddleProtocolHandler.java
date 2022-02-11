package com.rx.middlechannel.server.handler;

import com.rx.middlechannel.common.Receive;
import com.rx.middlechannel.common.cons.Cipher;
import com.rx.middlechannel.common.cons.DownCommand;
import com.rx.middlechannel.common.enums.FunctionEnum;
import com.rx.middlechannel.mapper.ClientMapper;
import com.rx.middlechannel.mapper.DeviceMapper;
import com.rx.middlechannel.utils.ChannelMap;
import com.rx.middlechannel.utils.SpringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author： KeA
 * @date： 2021-03-26 14:12:36
 * @version: 1.0
 * @describe:
 */
public class MiddleProtocolHandler extends ChannelInboundHandlerAdapter {

    private static ClientMapper clientMapper;
    static {
        clientMapper = SpringUtil.getBean(ClientMapper.class);
    }

    private static DeviceMapper deviceMapper;
    static {
        deviceMapper = SpringUtil.getBean(DeviceMapper.class);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead: read server back");
        Receive receive = (Receive) msg;
        Map<Integer, String> allClazz = FunctionEnum.getAllClazz();
        String className = allClazz.get(Integer.valueOf(receive.getFunctionCode()));
        Class clazz = Class.forName(className);
        Method method = clazz.getMethod("decode",ChannelHandlerContext.class, Receive.class,ClientMapper.class,DeviceMapper.class);
        method.invoke(clazz.newInstance(),ctx,receive,clientMapper,deviceMapper);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String uuid = ctx.channel().id().asLongText();
        ChannelMap.removeChannel(uuid);
        clientMapper.offline(uuid);
        System.out.println("连接出现异常，id: " + uuid);
        // 当出现异常时就关闭连接
        cause.printStackTrace();
        ctx.close();
    }


    // 连接成功后，向server接到信息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String uuid = ctx.channel().id().asLongText();
//        ChannelMap.addChannel(uuid, ctx.channel());
        System.out.println("新连接已建立，uuid: " + uuid);
        super.channelActive(ctx);
    }
}
