package com.rx.middlechannel.server;

import com.rx.middlechannel.server.codec.MiddleProtocolDecoder;
import com.rx.middlechannel.server.handler.MiddleProtocolHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author bmr
 * 服务端
 */
@Component
@Slf4j
public class MiddleServer {

    private static int port = 9999;

    @PostConstruct
    public static void start() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);

        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
        NioEventLoopGroup boss = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        NioEventLoopGroup worker = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
        serverBootstrap.group(boss, worker);
        new Thread(() -> {
            try{
                serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                        pipeline.addLast("middleProtocolDecoder", new MiddleProtocolDecoder());
                        pipeline.addLast("middleMessageHandler", new MiddleProtocolHandler());
                    }
                });

                ChannelFuture channelFuture = serverBootstrap.bind(port).sync();

                channelFuture.channel().closeFuture().sync();
            } catch (InterruptedException e){
                log.error("netty启动失败："+e.getMessage());
            }
            finally {
                boss.shutdownGracefully();
                worker.shutdownGracefully();
            }
        }).start();

    }
}
