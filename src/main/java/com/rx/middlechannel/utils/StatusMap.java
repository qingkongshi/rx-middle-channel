package com.rx.middlechannel.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author： KeA
 * @date： 2021-05-18 15:18:32
 * @version: 1.0
 * @describe:
 */
public class StatusMap {

    private static Map<String, Boolean> map = new ConcurrentHashMap<>();

    //添加状态
    public static void addStatus(String statusName, Boolean status){
        map.put(statusName, status);
    }

    //获取全部管道
    public static Map<String, Boolean> getStatus(){
        return map;
    }

    //根据id获取管道
    public static Boolean getStatusByName(String statusName){
        return map.get(statusName);
    }

    //删除管道
    public static void removeStatus(String statusName){
        map.remove(statusName);
    }
}
