package com.rx.middlechannel.common.cons;

import lombok.Data;

/**
 * @author： KeA
 * @date： 2021-03-26 13:27:36
 * @version: 1.0
 * @describe: 通信规约实体类
 */
@Data
public class Cipher {
    private byte lowerPositionAddress;
    private byte functionCode;
    private byte number;
    private String data;
    private String checkCode;
}
