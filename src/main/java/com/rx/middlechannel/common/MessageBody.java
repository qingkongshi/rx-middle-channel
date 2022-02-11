package com.rx.middlechannel.common;

import lombok.Data;

/**
 * @author： KeA
 * @date： 2021-03-30 10:02:09
 * @version: 1.0
 * @describe:
 */
@Data
public class MessageBody {
    private byte[] body;
    private byte[] crc;
    private byte crch;
    private byte crcl;
}
