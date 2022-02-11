package com.rx.middlechannel.common.enums;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author： KeA
 * @date： 2021-03-29 14:12:21
 * @version: 1.0
 * @describe: 功能码枚举类型
 */

public enum FunctionEnum {
    InitCmdStrategy(0,"com.rx.middlechannel.server.strategy.impl.InitCmdStrategy"),
    OpenLockStrategy(1,"com.rx.middlechannel.server.strategy.impl.OpenLockStrategy"),
    LockedStrategy(2,"com.rx.middlechannel.server.strategy.impl.LockedStrategy"),
    ChangePwdStrategy(3,"com.rx.middlechannel.server.strategy.impl.ChangePwdStrategy"),
    ChangeIpAddrStrategy(4,"com.rx.middlechannel.server.strategy.impl.ChangeIpAddrStrategy"),
    GetLockStatusStrategy(5,"com.rx.middlechannel.server.strategy.impl.GetLockStatusStrategy");

    private Integer code;
    private String name;

    FunctionEnum(Integer code,String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static Map<Integer,String> getAllClazz() {
        FunctionEnum[] functionEnums = FunctionEnum.values();
        Map<Integer, String> map = new HashMap<>();
        for (FunctionEnum commandEnum : functionEnums) {
            map.put(commandEnum.getCode(), commandEnum.getName());
        }
        return map;
    }
}
