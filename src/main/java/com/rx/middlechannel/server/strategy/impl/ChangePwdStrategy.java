package com.rx.middlechannel.server.strategy.impl;

import com.rx.middlechannel.common.Message;
import com.rx.middlechannel.common.MessageBody;
import com.rx.middlechannel.common.MessageHeader;
import com.rx.middlechannel.common.Receive;
import com.rx.middlechannel.common.cons.DownCommand;
import com.rx.middlechannel.mapper.ClientMapper;
import com.rx.middlechannel.mapper.DeviceMapper;
import com.rx.middlechannel.server.strategy.CommandStrategy;
import com.rx.middlechannel.utils.CRC16Util;
import com.rx.middlechannel.utils.ChannelMap;
import com.rx.middlechannel.utils.StatusMap;
import com.rx.middlechannel.utils.StringByteConverter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 更改密码  报文解析策略
 * @author bmr
 * @classname ChangePwdStrategy
 * @description
 * @date 2021/3/25 16:13:51
 */
@Component
public class ChangePwdStrategy implements CommandStrategy {

    /**
     * 解析报文
     * @param receive
     */
    @Override
    public void decode(ChannelHandlerContext ctx , Receive receive, ClientMapper clientMapper, DeviceMapper deviceMapper) {
        byte[] data = receive.getData();
        int a = data[0];
        if (a == 19){
            StatusMap.addStatus("changePassword",true);
        }else{
            StatusMap.addStatus("changePassword",false);
        }
    }

    /**
     * 更换密码命令 03
     * @param downCommand
     */
    @Override
    public void encode(DownCommand downCommand){
        //原密码：20  40  60
        //新密码：30  50  80
        //发送报文：01 03 03 14 28 3C 1E 32 50 6E 6D

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
