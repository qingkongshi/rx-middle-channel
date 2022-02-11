package com.rx.middlechannel.server.strategy.impl;

import com.rx.middlechannel.bean.Client;
import com.rx.middlechannel.bean.Device;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 获取锁具状态命令  报文解析策略
 * @author bmr
 * @classname GetLockStatusStrategy
 * @description
 * @date 2021/3/25 16:15:51
 */
@Slf4j
@Component
public class GetLockStatusStrategy implements CommandStrategy {
    /**
     * 解析报文
     * @param receive
     */
    @Override
    public void decode(ChannelHandlerContext ctx , Receive receive, ClientMapper clientMapper, DeviceMapper deviceMapper) {
        byte[] data = receive.getData();
        int a = data[0];
        //根据uuid查询串口服务器   再根据串口服务器id和设备地址码查询设备，修改设备状态
        String uuid = ctx.channel().id().asLongText();
        Client client = clientMapper.selectByUuid(uuid);
        Device device = new Device();
        device.setClientId(client.getId());
        String address = String.format("%2s",receive.getLowerPositionAddress()+"").replace(" ","0");
        device.setAddressCode(address);
        if (a == 32){
            System.out.println("开启状态");
            device.setStatus(1);
        }else if(a == 33){
            System.out.println("关闭状态");
            device.setStatus(0);
        }else{
            System.out.println("读取失败");
        }
        deviceMapper.updateStatusByCodeAndId(device);
    }

    /**
     * 读取锁具状态命令 05
     * @param downCommand
     */
    @Override
    public void encode(DownCommand downCommand){
        //发送报文：01 05 03 14 28 3C 93 9B
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
