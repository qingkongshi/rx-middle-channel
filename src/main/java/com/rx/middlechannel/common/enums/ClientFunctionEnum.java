package com.rx.middlechannel.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author： KeA
 * @date： 2021-05-08 15:04:33
 * @version: 1.0
 * @describe:
 */
@Getter
public enum ClientFunctionEnum {

    init(0,"init"),
    unlocking(1,"unlocking"),
    locking(2,"locking"),
    password(3,"password"),
    address(4,"address"),
    status(5,"status");

    Integer code;
    String message;

    ClientFunctionEnum(Integer code,String message){
        this.code = code;
        this.message = message;
    }
    public static Map<Integer,String> getAllClazz() {
        ClientFunctionEnum[] commandEnums = ClientFunctionEnum.values();
        Map<Integer, String> map = new HashMap<>();
        for (ClientFunctionEnum commandEnum : commandEnums) {
            map.put(commandEnum.getCode(), commandEnum.getMessage());
        }
        return map;
    }

}
