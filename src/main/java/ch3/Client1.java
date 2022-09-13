package ch3;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Client1 {

    public static void main(String[] args) throws InterruptedException {
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // connect is async
                .connect("127.0.0.1", 8080);

        Channel channel = channelFuture.channel();
        log.debug("{}", channel);
        channelFuture.sync();
        log.debug("{}", channel);
        channel.writeAndFlush("HelloWorld");

        //channelFuture.addListener(new ChannelFutureListener() {
        //    @Override
        //    public void operationComplete(ChannelFuture future) throws Exception {
        //        log.debug(future.channel());
        //        future.channel().writeAndFlush("HelloWorld");
        //    }
        //});
    }
}
