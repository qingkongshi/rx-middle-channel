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
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import org.springframework.stereotype.Component;

/**
 * 上锁命令  报文解析策略
 * @author bmr
 * @classname LockedStrategy
 * @description
 * @date 2021/3/25 16:12:51
 */
@Component
public class LockedStrategy implements CommandStrategy {
    /**
     * 解析报文
     * @param receive
     */
    @Override
    public void decode(ChannelHandlerContext ctx , Receive receive, ClientMapper clientMapper, DeviceMapper deviceMapper) {
        byte[] data = receive.getData();
        int a = data[0];
        if (a == 18){
            System.out.println("关锁成功");
        }else{
            System.out.println("关锁失败");
        }
        //根据uuid查询串口服务器   再根据串口服务器id和设备地址码查询设备，修改设备状态
        String uuid = ctx.channel().id().asLongText();
        Client client = clientMapper.selectByUuid(uuid);
        Device device = new Device();
        device.setClientId(client.getId());
        String address = String.format("%2s",receive.getLowerPositionAddress()+"").replace(" ","0");
        device.setAddressCode(address);
        device.setStatus(0);
        deviceMapper.updateStatusByCodeAndId(device);
    }

    /**
     * 上锁命令 02
     * @param downCommand
     */
    @Override
    public void encode(DownCommand downCommand){
        //上锁命令：01 02 03 61 61
        //传递参数：01 02 03 12 20 E5
        //封装信息头
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setLowerPositionAddress((byte)(0xff & downCommand.getLowerPositionAddress()));
        messageHeader.setFunctionCode((byte)(0xff & downCommand.getFunctionCode()));
        messageHeader.setNumber((byte)(0xff & downCommand.getNumber()));
        //计算校验位并封装
        String crc3 = CRC16Util.getCRC3(messageHeader.toBytes());
        byte[] bytesCrc = StringByteConverter.hexToBytes(crc3);
        MessageBody messageBody = new MessageBody();
        messageBody.setCrc(bytesCrc);
        messageBody.setCrch(bytesCrc[0]);
        messageBody.setCrcl(bytesCrc[1]);
        Message message = new Message();
        message.setMessageHeader(messageHeader);
        message.setMessageBody(messageBody);
        //获取管道写入并发送
        ChannelMap.getChannelById(downCommand.getUuid()).writeAndFlush(Unpooled.copiedBuffer(message.toNodataBytes()));
    }
}
