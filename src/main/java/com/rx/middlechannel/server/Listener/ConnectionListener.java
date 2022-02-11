package com.rx.middlechannel.server.Listener;

import com.rx.middlechannel.server.MiddleClient;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

import java.util.concurrent.TimeUnit;

/**
 * @author： KeA
 * @date： 2021-03-26 16:16:08
 * @version: 1.0
 * @describe:
 */
public class ConnectionListener implements ChannelFutureListener {
    public void operationComplete(ChannelFuture future) throws Exception {
//        if (!future.isSuccess()) {
//            final EventLoop loop = future.channel().eventLoop();
//            loop.schedule(new Runnable() {
//                @Override
//                public void run() {
//                    System.err.println("服务端链接不上，开始重连操作...");
//                    try {
//                        MiddleClient.start();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }, 3, TimeUnit.SECONDS);
//        } else {
//            System.err.println("服务端链接成功...");
//        }
    }
}
