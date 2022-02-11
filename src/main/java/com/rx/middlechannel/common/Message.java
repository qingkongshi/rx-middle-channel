package com.rx.middlechannel.common;

import com.rx.middlechannel.utils.ArrayMerger;
import lombok.Data;

/**
 * @author： KeA
 * @date： 2021-03-30 10:02:22
 * @version: 1.0
 * @describe:
 */
@Data
public class Message {

    private MessageHeader messageHeader;
    private MessageBody messageBody;

    public byte[] toBytes(){
        return ArrayMerger.byteMerger(messageHeader.toBytes(),messageBody.getBody());
    }

    public byte[] toNodataBytes(){
        return ArrayMerger.byteMerger(messageHeader.toBytes(),messageBody.getCrc());
    }

    public byte[] toAllBytes(){
        return ArrayMerger.byteMerger(this.toBytes(),messageBody.getCrc());
    }
}
