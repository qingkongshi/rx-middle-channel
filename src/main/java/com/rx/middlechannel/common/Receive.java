package com.rx.middlechannel.common;

import com.rx.middlechannel.utils.ArrayMerger;
import lombok.Data;

/**
 * @author： KeA
 * @date： 2021-04-01 10:04:27
 * @version: 1.0
 * @describe: 接收客户端发来的信息的实体
 */
@Data
public class Receive {
    private byte lowerPositionAddress;
    private byte functionCode;
    private byte number;
    private byte[] data;
    private String crc;

    public byte[] toBytes(){
        byte[] a = new byte[1];
        a[0] = lowerPositionAddress;
        byte[] b = new byte[1];
        b[0] = functionCode;
        byte[] c = new byte[1];
        c[0] = number;
        return ArrayMerger.byteMerger(ArrayMerger.byteMerger(ArrayMerger.byteMerger(a,b),c),data);
    }
}
