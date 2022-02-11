package com.rx.middlechannel.common.enums;

/**
 * @author： KeA
 * @date： 2021-03-29 14:42:49
 * @version: 1.0
 * @describe:
 */
public enum PasswordNumberEnum {
    THREE("三个",0x03),
    FOUR("四个",0x04),
    FIVE("五个",0x05);

    private String name;
    private Integer code;
    private PasswordNumberEnum(String name,Integer code){
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }
}
