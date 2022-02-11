package com.rx.middlechannel.server.strategy.impl;

import com.rx.middlechannel.common.Message;
import com.rx.middlechannel.common.MessageBody;
import com.rx.middlechannel.common.MessageHeader;
import com.rx.middlechannel.common.Receive;
import com.rx.middlechannel.common.cons.DownCommand;
import com.rx.middlechannel.mapper.ClientMapper;
import com.rx.middlechannel.mapper.DeviceMapper;
import com.rx.middlechannel.server.MiddleClient;
import com.rx.middlechannel.server.strategy.CommandStrategy;
import com.rx.middlechannel.utils.CRC16Util;
import com.rx.middlechannel.utils.ChannelMap;
import com.rx.middlechannel.utils.StringByteConverter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * 更改机号(地址)命令  报文解析策略
 * @author bmr
 * @classname ChangeIpAddrStrategy
 * @description
 * @date 2021/3/25 16:14:51
 */
public class ChangeIpAddrStrategy implements CommandStrategy {
    /**
     * 解析报文
     * @param receive
     */
    @Override
    public void decode(ChannelHandlerContext ctx , Receive receive, ClientMapper clientMapper, DeviceMapper deviceMapper) {
        String s = new String(receive.getData());
        System.out.println(s);
    }

    /**
     * 更换地址命令 04
     * @param downCommand
     */
    @Override
    public void encode(DownCommand downCommand){
        //原地址：01           新地址：02
        //发送报文：01 04 03 14 28 3C 02 5B 7D
        //密码锁返回数据串：01 04 03 14 40 A2

        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setLowerPositionAddress((byte)(0xff & downCommand.getLowerPositionAddress()));
        messageHeader.setFunctionCode((byte)(0xff & downCommand.getFunctionCode()));
        messageHeader.setNumber((byte)(0xff & downCommand.getNumber()));
        //封装信息体
        MessageBody messageBody = new MessageBody();
        String data = downCommand.getData();
        messageBody.setBody(StringByteConverter.toBytes(data));
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
        //获取管道写入并发送
        ChannelMap.getChannelById(downCommand.getUuid()).writeAndFlush(Unpooled.copiedBuffer(message.toAllBytes()));
    }
}
