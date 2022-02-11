package com.rx.middlechannel.server.codec;

import com.rx.middlechannel.common.cons.Cipher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author： KeA
 * @date： 2021-03-26 13:16:11
 * @version: 1.0
 * @describe: 编码工具
 */
public class MiddleProtocolEncoder extends MessageToByteEncoder<Cipher> {
    protected void encode(ChannelHandlerContext channelHandlerContext, Cipher cipher, ByteBuf byteBuf) throws Exception {
        byteBuf.writeByte(cipher.getLowerPositionAddress());
        byteBuf.writeByte(cipher.getFunctionCode());
        byteBuf.writeByte(cipher.getNumber());
        byteBuf.writeBytes(cipher.getData().getBytes());
    }
}
