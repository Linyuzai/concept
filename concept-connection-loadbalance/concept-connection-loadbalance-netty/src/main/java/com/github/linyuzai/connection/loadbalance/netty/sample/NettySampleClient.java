package com.github.linyuzai.connection.loadbalance.netty.sample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

public class NettySampleClient {

    public static void connect(String host, int port, Consumer<String> consumer) {
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new LineBasedFrameDecoder(1024));
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new ClientHandler(consumer));
                        }
                    });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            worker.shutdownGracefully();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class ClientHandler extends ChannelInboundHandlerAdapter {

        private final Consumer<String> consumer;

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            consumer.accept(String.valueOf(msg));
        }
    }
}
