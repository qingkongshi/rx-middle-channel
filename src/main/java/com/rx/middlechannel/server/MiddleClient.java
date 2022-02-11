package com.rx.middlechannel.server;

import com.rx.middlechannel.common.cons.Cipher;
import com.rx.middlechannel.server.Listener.ConnectionListener;
import com.rx.middlechannel.server.codec.ClientDecoder;
import com.rx.middlechannel.server.codec.MiddleProtocolDecoder;
import com.rx.middlechannel.server.codec.MiddleProtocolEncoder;
import com.rx.middlechannel.server.handler.ClientHandler;
import com.rx.middlechannel.server.handler.MiddleProtocolHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;

/**
 * @author： KeA
 * @date： 2021-03-26 13:09:42
 * @version: 1.0
 * @describe: Netty客户端
 */
@Component
@Slf4j
public class MiddleClient {

    private static final String host ="127.0.0.1";
    private static final int port = 9999;
    private static SocketChannel socketChannel = null;

    /**
     * netty客户端
     */
    @PostConstruct
    public static void start() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        new Thread(()->{
            try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
//                            ch.pipeline().addLast(new MiddleProtocolEncoder());
                            ch.pipeline().addLast(new ClientDecoder());
                            ch.pipeline().addLast(new ClientHandler());
//                            ch.pipeline().addLast(new MiddleProtocolHandler());
                        }
                    });

            ChannelFuture cf = bootstrap.connect(host,port).sync();
            log.info("TCP Client 已经在" + host + "的" + port + "端口建立Channel");
            if (cf.isSuccess()){
                socketChannel = (SocketChannel) cf.channel();
            }
            cf.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                group.shutdownGracefully();
            }
        }).start();
    }
    //获取channel管道
    public static SocketChannel getSocketChannel(){
        return socketChannel;
    }
    //发送信息
    public static void sendMsg(String msg) throws Exception {
        if (socketChannel != null) {
            socketChannel.writeAndFlush(msg).sync();
        } else {
            log.info("消息发送失败,连接尚未建立!");
        }
    }

}
