package io.demo.ch1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client {
    public static void main(String[] args) {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(worker);

        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LoggingHandler());
                ch.pipeline().addLast(new StringEncoder());
            }
        });

        Channel channel = null;
        try {
            channel = bootstrap.connect("127.0.0.1", 8081).sync().channel();
            channel.writeAndFlush("hello");
            channel.writeAndFlush("world");
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            log.info("shutdown");
            worker.shutdownGracefully();
        }

        //ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        //byteBuf.writeBytes("hello".getBytes());
        //byteBuf.retain();
        //channel.writeAndFlush(byteBuf);
        //
        ////byteBuf.writeBytes("world".getBytes());
        //channel.writeAndFlush(byteBuf);
    }
}
