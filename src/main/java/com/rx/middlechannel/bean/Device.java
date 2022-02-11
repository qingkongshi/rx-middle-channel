package com.rx.middlechannel.bean;

import lombok.Data;

/**
 * @author： KeA
 * @date： 2021-04-23 18:40:10
 * @version: 1.0
 * @describe:
 */
@Data
public class Device {
    // id
    private Integer id;
    // 所属客户端
    private Integer clientId;
    // 地址码
    private String addressCode;
    // 密码数量
    private String number;
    // 设备名称
    private String deviceName;
    // 回零调整系数
    private String param1;
    // 手动位置码
    private String param2;
    // 对位调整系数
    private String param3;
    // 平移电机移动圈数
    private String param4;
    // 平移电机移动格数
    private String param5;
    // 旋转电机移动格数
    private String param6;
    // 旋转电机移动修正系数
    private String param7;
    // 密码
    private String password;
    // 锁具状态 0 关  1 开
    private Integer status;

    public String getData(){
        return param1+param2+param3+password+param4+param5+param6+param7;
    }
}
