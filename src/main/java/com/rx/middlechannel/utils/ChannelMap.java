package com.rx.middlechannel.utils;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author： KeA
 * @date： 2021-04-19 10:41:10
 * @version: 1.0
 * @describe:
 */
public class ChannelMap {

    private static Map<String, SocketChannel> map = new ConcurrentHashMap<>();

    //添加管道
    public static void addChannel(String id, Channel channel){
        map.put(id,(SocketChannel) channel);
    }

    //获取全部管道
    public static Map<String, SocketChannel> getChannels(){
        return map;
    }

    //根据id获取管道
    public static SocketChannel getChannelById(String id){
        return map.get(id);
    }

    //删除管道
    public static void removeChannel(String id){
        map.remove(id);
    }
}
