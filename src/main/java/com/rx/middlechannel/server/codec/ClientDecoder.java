package com.rx.middlechannel.server.codec;

import com.rx.middlechannel.common.Receive;
import com.rx.middlechannel.utils.CRC16Util;
import com.rx.middlechannel.utils.StringByteConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author： KeA
 * @date： 2021-04-02 09:24:25
 * @version: 1.0
 * @describe:
 */
@Slf4j
public class ClientDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        log.info("client decode start");
        byteBuf.markReaderIndex();
        //如果可读取长度不足
        if (byteBuf.readableBytes()<4){
            //指针回到标记位
            byteBuf.resetReaderIndex();
            return;
        }
        //防止socket字节流攻击，防止，客户端传来的数据过大
        if (byteBuf.readableBytes() > 2048) {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }

        Receive receive = new Receive();
        receive.setLowerPositionAddress(byteBuf.readByte());
        receive.setFunctionCode(byteBuf.readByte());
        receive.setNumber(byteBuf.readByte());

        //获取要读取的长度
        Integer byteNumber = getByteNumber(receive);
        //判断可读取长度是否足够
        if (byteBuf.readableBytes() < (byteNumber + 2)) {
            log.error("可读取长度不足");
            byteBuf.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[byteNumber];
        try{
            byteBuf.readBytes(bytes);
        }catch (Exception e ){
            byteBuf.resetReaderIndex();
            return;
        }
        //封装实体
        receive.setData(bytes);
        //封装校验位
        byte crch = byteBuf.readByte();
        byte crcl = byteBuf.readByte();

        receive.setCrc((Integer.toHexString(crch & 0xff)+Integer.toHexString(crcl & 0xff)).toUpperCase());
        //计算校验位
        String crc = CRC16Util.getCRC3(receive.toBytes()).toUpperCase();
        //比对校验位
        if (!receive.getCrc().equals(crc)){
            log.error("校验失败");
            log.error("计算："+crc);
            log.error("接受："+receive.getCrc());
        }
        log.info("解析结果："+receive.toString());
        log.info("client decode end");
        out.add(receive);

    }
    private Integer getByteNumber(Receive receive){
        if (receive.getFunctionCode()==0){
            if (receive.getNumber() == 3){
                return 10;
            }else if (receive.getNumber()==4){
                return 11;
            }else if (receive.getNumber()==5){
                return 12;
            }
        }else if (receive.getFunctionCode() == 1){
            if (receive.getNumber() == 3){
                return 3;
            }else if (receive.getNumber()==4){
                return 4;
            }else if (receive.getNumber()==5){
                return 5;
            }
        }else if (receive.getFunctionCode() == 2){
            return 0;
        }else if (receive.getFunctionCode() == 3){
            if (receive.getNumber() == 3){
                return 6;
            }else if (receive.getNumber()==4){
                return 8;
            }else if (receive.getNumber()==5){
                return 10;
            }
        }else if (receive.getFunctionCode() == 4){
            if (receive.getNumber() == 3){
                return 4;
            }else if (receive.getNumber()==4){
                return 5;
            }else if (receive.getNumber()==5){
                return 6;
            }
        }else if (receive.getFunctionCode() == 5){
            if (receive.getNumber() == 3){
                return 3;
            }else if (receive.getNumber()==4){
                return 4;
            }else if (receive.getNumber()==5){
                return 5;
            }
        }else{
            try {
                throw new Exception("状态码有误");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return 0;
    }
}
