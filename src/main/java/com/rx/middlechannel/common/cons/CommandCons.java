package com.rx.middlechannel.common.cons;

/**
 * 命令码 常量
 * @author bmr
 * @classname CommandCons
 * @description
 * @date 2021/3/24 16:18:51
 */
public class CommandCons {

    /** 初始化命令 */
    public static final int INIT = 0;

    /** 开锁命令 */
    public static final int UNLOCK = 1;

    /** 上锁命令 */
    public static final int LOCKED = 2;

    /** 更改密码命令 */
    public static final int CHANGE_PWD = 3;

    /** 更改机号（地址）命令 */
    public static final int CHANGE_IP_ADDR = 4;

    /** 读取锁具状态命令 */
    public static final int GET_LOCK_STATUS = 5;
}
