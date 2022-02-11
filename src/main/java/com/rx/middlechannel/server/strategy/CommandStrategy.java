package com.rx.middlechannel.server.strategy;

import com.rx.middlechannel.common.Receive;
import com.rx.middlechannel.common.cons.DownCommand;
import com.rx.middlechannel.mapper.ClientMapper;
import com.rx.middlechannel.mapper.DeviceMapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author bmr
 * @classname CommandStrategy
 * @description
 * @date 2021/3/25 16:05:51
 */
public interface CommandStrategy {

//    /**
//     * 解析报文
//     * @param byteBuf
//     */
    void decode(ChannelHandlerContext ctx , Receive receive , ClientMapper clientMapper, DeviceMapper deviceMapper);

    void encode(DownCommand downCommand);
}
