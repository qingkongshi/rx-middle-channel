package com.rx.middlechannel.utils;

/**
 * @ClassName ArrayMerger
 * @Author KeA
 * @Date 2019/2/18 16:27
 * @Version 1.0
 */
public class ArrayMerger {
    //System.arraycopy()方法
    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }
}
