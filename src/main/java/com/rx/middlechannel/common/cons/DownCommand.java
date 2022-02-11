package com.rx.middlechannel.common.cons;

import lombok.Data;

/**
 * @author： KeA
 * @date： 2021-03-29 15:45:09
 * @version: 1.0
 * @describe:
 */
@Data
public class DownCommand {
    private Integer lowerPositionAddress;
    private Integer functionCode;
    private Integer number;
    private String data;
    //要发送的设备id
    private String uuid;
}
