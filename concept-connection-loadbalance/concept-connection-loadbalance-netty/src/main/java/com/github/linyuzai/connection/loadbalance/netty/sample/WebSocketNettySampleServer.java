package com.github.linyuzai.connection.loadbalance.netty.sample;

import com.github.linyuzai.connection.loadbalance.netty.concept.NettyLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.netty.websocket.WebSocketNettyLoadBalanceHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class WebSocketNettySampleServer {

    private final NettyLoadBalanceConcept concept;

    @Setter
    private String group;

    public void send(String msg) {
        concept.send(msg);
    }

    public void start(String path, int port) throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(@NonNull SocketChannel channel) {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(65536));
                            //WebSocketServerCompressionHandler
                            pipeline.addLast(new WebSocketServerProtocolHandler(path));
                            if (group == null) {
                                pipeline.addLast(new WebSocketNettyLoadBalanceHandler(concept));
                            } else {
                                pipeline.addLast(new WebSocketNettyLoadBalanceHandler(concept, group));
                            }
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
