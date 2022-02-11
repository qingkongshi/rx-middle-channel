package com.rx.middlechannel.server.strategy;

import com.rx.middlechannel.common.Message;
import com.rx.middlechannel.common.MessageBody;
import com.rx.middlechannel.common.MessageHeader;
import com.rx.middlechannel.common.Receive;
import com.rx.middlechannel.utils.CRC16Util;
import com.rx.middlechannel.utils.StringByteConverter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author： KeA
 * @date： 2021-05-08 15:21:11
 * @version: 1.0
 * @describe:
 */
@Slf4j
public class ClientMessageHandler {

    public void init(ChannelHandlerContext ctx , Receive receive){
        //封装信息头
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setLowerPositionAddress((byte)(0xff & receive.getLowerPositionAddress()));
        messageHeader.setFunctionCode((byte)(0xff & receive.getFunctionCode()));
        messageHeader.setNumber((byte)(0xff & receive.getNumber()));
        //封装信息体
        MessageBody messageBody = new MessageBody();
        byte[] bytes = new byte[1];
        bytes[0] = 16;
        messageBody.setBody(bytes);
        Message message = new Message();
        message.setMessageHeader(messageHeader);
        message.setMessageBody(messageBody);
        //计算校验位
        String crc3 = CRC16Util.getCRC3(message.toBytes());
        byte[] bytesCrc = StringByteConverter.hexToBytes(crc3);
        messageBody.setCrc(bytesCrc);
        messageBody.setCrch(bytesCrc[0]);
        messageBody.setCrcl(bytesCrc[1]);
        System.out.println(message);
        ctx.writeAndFlush(Unpooled.copiedBuffer(message.toAllBytes()));
    }

    public void unlocking(ChannelHandlerContext ctx , Receive receive){
        //封装信息头
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setLowerPositionAddress((byte)(0xff & receive.getLowerPositionAddress()));
        messageHeader.setFunctionCode((byte)(0xff & receive.getFunctionCode()));
        messageHeader.setNumber((byte)(0xff & receive.getNumber()));
        //封装信息体
        MessageBody messageBody = new MessageBody();
        byte[] bytes = new byte[1];
        bytes[0] = 17;
        messageBody.setBody(bytes);
        Message message = new Message();
        message.setMessageHeader(messageHeader);
        message.setMessageBody(messageBody);
        //计算校验位
        String crc3 = CRC16Util.getCRC3(message.toBytes());
        byte[] bytesCrc = StringByteConverter.hexToBytes(crc3);
        messageBody.setCrc(bytesCrc);
        messageBody.setCrch(bytesCrc[0]);
        messageBody.setCrcl(bytesCrc[1]);
        System.out.println(message);
        ctx.writeAndFlush(Unpooled.copiedBuffer(message.toAllBytes()));
    }

    public void locking(ChannelHandlerContext ctx , Receive receive){
        //封装信息头
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setLowerPositionAddress((byte)(0xff & receive.getLowerPositionAddress()));
        messageHeader.setFunctionCode((byte)(0xff & receive.getFunctionCode()));
        messageHeader.setNumber((byte)(0xff & receive.getNumber()));
        //封装信息体
        MessageBody messageBody = new MessageBody();
        byte[] bytes = new byte[1];
        bytes[0] = 18;
        messageBody.setBody(bytes);
        Message message = new Message();
        message.setMessageHeader(messageHeader);
        message.setMessageBody(messageBody);
        //计算校验位
        String crc3 = CRC16Util.getCRC3(message.toBytes());
        byte[] bytesCrc = StringByteConverter.hexToBytes(crc3);
        messageBody.setCrc(bytesCrc);
        messageBody.setCrch(bytesCrc[0]);
        messageBody.setCrcl(bytesCrc[1]);
        System.out.println(message);
        ctx.writeAndFlush(Unpooled.copiedBuffer(message.toAllBytes()));
    }

    public void password(ChannelHandlerContext ctx , Receive receive){
        //封装信息头
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setLowerPositionAddress((byte)(0xff & receive.getLowerPositionAddress()));
        messageHeader.setFunctionCode((byte)(0xff & receive.getFunctionCode()));
        messageHeader.setNumber((byte)(0xff & receive.getNumber()));
        //封装信息体
        MessageBody messageBody = new MessageBody();
        byte[] bytes = new byte[1];
        bytes[0] = 19;
        messageBody.setBody(bytes);
        Message message = new Message();
        message.setMessageHeader(messageHeader);
        message.setMessageBody(messageBody);
        //计算校验位
        String crc3 = CRC16Util.getCRC3(message.toBytes());
        byte[] bytesCrc = StringByteConverter.hexToBytes(crc3);
        messageBody.setCrc(bytesCrc);
        messageBody.setCrch(bytesCrc[0]);
        messageBody.setCrcl(bytesCrc[1]);
        System.out.println(message);
        ctx.writeAndFlush(Unpooled.copiedBuffer(message.toAllBytes()));
    }
    public void address(ChannelHandlerContext ctx , Receive receive){
        //封装信息头
        MessageHeader messageHeader = new MessageHeader();
        //消息体的第四位是更换后的地址
        byte[] data = receive.getData();
        messageHeader.setLowerPositionAddress((byte)(0xff & data[3]));
        messageHeader.setFunctionCode((byte)(0xff & receive.getFunctionCode()));
        messageHeader.setNumber((byte)(0xff & receive.getNumber()));
        //封装信息体
        MessageBody messageBody = new MessageBody();
        byte[] bytes = new byte[1];
        bytes[0] = 20;
        messageBody.setBody(bytes);
        Message message = new Message();
        message.setMessageHeader(messageHeader);
        message.setMessageBody(messageBody);
        //计算校验位
        String crc3 = CRC16Util.getCRC3(message.toBytes());
        byte[] bytesCrc = StringByteConverter.hexToBytes(crc3);
        messageBody.setCrc(bytesCrc);
        messageBody.setCrch(bytesCrc[0]);
        messageBody.setCrcl(bytesCrc[1]);
        System.out.println(message);
        ctx.writeAndFlush(Unpooled.copiedBuffer(message.toAllBytes()));
    }

    public void status(ChannelHandlerContext ctx , Receive receive){
        //封装信息头
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setLowerPositionAddress((byte)(0xff & receive.getLowerPositionAddress()));
        messageHeader.setFunctionCode((byte)(0xff & receive.getFunctionCode()));
        messageHeader.setNumber((byte)(0xff & receive.getNumber()));
        //封装信息体
        MessageBody messageBody = new MessageBody();
        byte[] bytes = new byte[1];
        Random random = new Random();
        int i = random.nextInt(100);
        if (i%2 == 0){
            log.info("开锁");
            bytes[0] = 32;
        }else{
            log.info("关锁");
            bytes[0] = 33;
        }
        messageBody.setBody(bytes);
        Message message = new Message();
        message.setMessageHeader(messageHeader);
        message.setMessageBody(messageBody);
        //计算校验位
        String crc3 = CRC16Util.getCRC3(message.toBytes());
        byte[] bytesCrc = StringByteConverter.hexToBytes(crc3);
        messageBody.setCrc(bytesCrc);
        messageBody.setCrch(bytesCrc[0]);
        messageBody.setCrcl(bytesCrc[1]);
        System.out.println(message);
        ctx.writeAndFlush(Unpooled.copiedBuffer(message.toAllBytes()));
    }
}
