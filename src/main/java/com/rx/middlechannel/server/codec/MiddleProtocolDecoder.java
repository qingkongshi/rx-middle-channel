package com.rx.middlechannel.server.codec;

import com.rx.middlechannel.bean.Client;
import com.rx.middlechannel.common.MessageHeader;
import com.rx.middlechannel.common.Receive;
import com.rx.middlechannel.mapper.ClientMapper;
import com.rx.middlechannel.utils.CRC16Util;
import com.rx.middlechannel.utils.ChannelMap;
import com.rx.middlechannel.utils.SpringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author bmr
 * 解码byteBuf
 */
@Slf4j
public class MiddleProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    private final int BASE_LENGTH = 4;

    private static ClientMapper clientMapper;
    static {
        clientMapper = SpringUtil.getBean(ClientMapper.class);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {

        log.info("server decode start");
        //标记当前读位置
        byteBuf.markReaderIndex();
        //如果可读取长度不足
        if (byteBuf.readableBytes()<BASE_LENGTH){
            //指针回到标记位
            byteBuf.resetReaderIndex();
            return;
        }
        //防止socket字节流攻击，防止，客户端传来的数据过大
        if (byteBuf.readableBytes() > 2048) {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
        //封装实体
        Receive receive = new Receive();
        receive.setLowerPositionAddress(byteBuf.readByte());
        receive.setFunctionCode(byteBuf.readByte());
        receive.setNumber(byteBuf.readByte());
        if (receive.getFunctionCode() == -1){
            log.info("注册包，客户端id为{}",receive.getNumber());
            int clientCode = 10000 + receive.getNumber();
            String uuid = ctx.channel().id().asLongText();
            ChannelMap.addChannel(uuid, ctx.channel());
            Client client = clientMapper.selectByClientCode(String.valueOf(clientCode));
            if (client == null){
                client = new Client();
                client.setClientCode(String.valueOf(clientCode));
                client.setUuid(uuid);
                client.setClientName("新建客户端"+receive.getNumber());
                client.setStatus(1);
                clientMapper.insertClient(client);
            }else{
                client.setUuid(uuid);
                client.setStatus(1);
                clientMapper.updateClientUUID(client);
            }
        }else{
            //判断可读取长度是否足够
            if (byteBuf.readableBytes() < 3 ) {
                log.error("可读取长度不足");
                byteBuf.resetReaderIndex();
                return;
            }

            byte[] bytes = new byte[1];
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
            String crchStr = String.format("%2s",Integer.toHexString(crch & 0xff)).replace(" ", "0").toUpperCase();
            String crclStr = String.format("%2s",Integer.toHexString(crcl & 0xff)).replace(" ", "0").toUpperCase();
            receive.setCrc(crchStr+crclStr);
            //计算校验位
            String crc = CRC16Util.getCRC3(receive.toBytes()).toUpperCase();
            //比对校验位
            if (!receive.getCrc().equals(crc)){
                log.error("校验失败");
                log.error("计算："+crc);
                log.error("接受："+receive.getCrc());
            }
            log.info("解析结果："+receive.toString());
            log.info("server decode end");
            out.add(receive);
        }
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
