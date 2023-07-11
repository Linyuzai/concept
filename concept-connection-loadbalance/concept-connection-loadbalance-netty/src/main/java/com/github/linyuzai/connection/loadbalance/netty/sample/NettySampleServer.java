package com.github.linyuzai.connection.loadbalance.netty.sample;

import com.github.linyuzai.connection.loadbalance.netty.concept.NettyLoadBalanceConcept;
import com.github.linyuzai.connection.loadbalance.netty.concept.NettyLoadBalanceHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class NettySampleServer {

    private final NettyLoadBalanceConcept concept;

    @Setter
    private String group;

    public void send(String msg) {
        concept.send(msg);
    }

    public void start(int port) {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            pipeline.addLast(new LineBasedFrameDecoder(1024));
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new StringDecoder());
                            if (group == null) {
                                pipeline.addLast(new NettyLoadBalanceHandler(concept));
                            } else {
                                pipeline.addLast(new NettyLoadBalanceHandler(concept, group));
                            }
                        }
                    });
            ChannelFuture future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
