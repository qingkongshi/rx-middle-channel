package com.rx.middlechannel.server.strategy.impl;

import com.rx.middlechannel.bean.Device;
import com.rx.middlechannel.common.Message;
import com.rx.middlechannel.common.MessageBody;
import com.rx.middlechannel.common.MessageHeader;
import com.rx.middlechannel.common.Receive;
import com.rx.middlechannel.common.cons.DownCommand;
import com.rx.middlechannel.mapper.ClientMapper;
import com.rx.middlechannel.mapper.DeviceMapper;
import com.rx.middlechannel.server.DeviceService;
import com.rx.middlechannel.server.MiddleClient;
import com.rx.middlechannel.server.strategy.CommandStrategy;
import com.rx.middlechannel.utils.CRC16Util;
import com.rx.middlechannel.utils.ChannelMap;
import com.rx.middlechannel.utils.StringByteConverter;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 初始化命令 报文解析策略
 * @author bmr
 * @classname InitCmdStrategy
 * @description
 * @date 2021/3/25 16:09:51
 */
@Component
public class InitCmdStrategy implements CommandStrategy {

    /**
     * 解析报文
     * @param receive
     */
    @Override
    public void decode(ChannelHandlerContext ctx , Receive receive, ClientMapper clientMapper, DeviceMapper deviceMapper) {
        // 初始化验证成功后，写入数据库
        String s = new String(receive.getData());
        System.out.println(s);
    }

    /**
     * 初始化命令 00
     * @param downCommand
     */
    @Override
    public void encode(DownCommand downCommand){
        // 发送报文：01 00 03 32 46 32 14 28 3C 0C 32 19 00 9C 96

        // 1.地址码：#01；2.功能码：#00H；3.特征码：#03；                              发送报文: 01 00 03
        // 4.回零码：#505.开锁位置码：#70；6.对位码：#50；                                      32 46 32
        // 7.密码1：#20；8.密码2：#40；9.密码3：#60；                                          14 28 3C
        // 10.平移圈数：#12；11.平移格数：#50；12.旋转格数：#25；13.旋转调整：#0；                 0C 32 19 00
        // 14：CRCH（自动生成）；15. CRCL（自动生成）。                                         9C 96

        //封装信息头
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
