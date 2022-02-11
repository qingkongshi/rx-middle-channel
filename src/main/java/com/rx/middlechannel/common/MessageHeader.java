package com.rx.middlechannel.common;

import com.rx.middlechannel.utils.ArrayMerger;
import lombok.Data;

/**
 * @author： KeA
 * @date： 2021-03-30 09:55:04
 * @version: 1.0
 * @describe:
 */
@Data
public class MessageHeader {
    private byte lowerPositionAddress;
    private byte functionCode;
    private byte number;

    public byte[] toBytes(){
        byte[] a = new byte[1];
        a[0] = lowerPositionAddress;
        byte[] b = new byte[1];
        b[0] = functionCode;
        byte[] c = new byte[1];
        c[0] = number;
        return ArrayMerger.byteMerger(ArrayMerger.byteMerger(a,b),c);

    }
}
