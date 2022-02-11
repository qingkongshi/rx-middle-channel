package com.rx.middlechannel.server.handler;

import com.rx.middlechannel.common.Message;
import com.rx.middlechannel.common.MessageBody;
import com.rx.middlechannel.common.MessageHeader;
import com.rx.middlechannel.common.Receive;
import com.rx.middlechannel.common.enums.ClientFunctionEnum;
import com.rx.middlechannel.common.enums.FunctionEnum;
import com.rx.middlechannel.server.MiddleClient;
import com.rx.middlechannel.server.strategy.ClientMessageHandler;
import com.rx.middlechannel.utils.CRC16Util;
import com.rx.middlechannel.utils.ChannelMap;
import com.google.common.reflect.Invokable;
import com.rx.middlechannel.utils.StringByteConverter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author： KeA
 * @date： 2021-04-23 14:18:06
 * @version: 1.0
 * @describe:
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead: read server message");
        Receive receive = (Receive) msg;
        int code = receive.getFunctionCode();
        Map<Integer, String> allClazz = ClientFunctionEnum.getAllClazz();
        String clazz = allClazz.get(code);
        Method method = ClientMessageHandler.class.getDeclaredMethod(clazz,ChannelHandlerContext.class, Receive.class);
        Invokable<ClientMessageHandler, Object> invokable = (Invokable<ClientMessageHandler, Object>) Invokable.from(method);
        invokable.invoke(new ClientMessageHandler(),ctx,receive);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常时就关闭连接
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端登录");
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setLowerPositionAddress((byte)(0xff & 01));
        messageHeader.setFunctionCode((byte)(0xff & 0xff));
        messageHeader.setNumber((byte)(0xff & 03));
        String crc3 = CRC16Util.getCRC3(messageHeader.toBytes());
        byte[] bytesCrc = StringByteConverter.hexToBytes(crc3);
        MessageBody messageBody = new MessageBody();
        messageBody.setCrc(bytesCrc);
        messageBody.setCrch(bytesCrc[0]);
        messageBody.setCrcl(bytesCrc[1]);
        Message message = new Message();
        message.setMessageHeader(messageHeader);
        message.setMessageBody(messageBody);
        Thread.sleep(5000);
        MiddleClient.getSocketChannel().writeAndFlush(Unpooled.copiedBuffer(message.toNodataBytes()));
        super.channelActive(ctx);
    }
}
