package com.github.linyuzai.connection.loadbalance.netty;

import com.github.linyuzai.connection.loadbalance.netty.handler.WebSocketLoadBalanceHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Netty实现WebSocket实例
 * Http连接是无状态的，且每一个请求只会响应一次，下次需要重新连接。服务端不可主动向客户端发送消息
 * 使用WebSocket实现一个客户端与服务端可互相通信的偿连接
 *
 * @author makeDoBetter
 * @version 1.0
 * @date 2021/4/29 15:54
 * @since JDK 1.8
 */
public class NettyMain {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        try {
            bootstrap.group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //基于http协议，因此添加http编解码器
                            pipeline.addLast(new HttpServerCodec());
                            //提供大文件写的处理器，尤其适用于大文件写，方便管理状态，不需要用户过分关心
                            //一个{@link ChannelHandler}，它增加了对写入大型数据流的支持既不花费大量内存
                            // 也不获取{@link OutOfMemoryError}。
                            // 大型数据流（例如文件传输）需要在{@link ChannelHandler}实现中进行复杂的状态管理。
                            // {@link ChunkedWriteHandler}管理如此复杂的状态以便您可以毫无困难地发送大量数据流。
                            pipeline.addLast(new ChunkedWriteHandler());
                            //将http协议下分段传输的数据聚合到一起，用于响应请求是需要添加在 HttpServerCodec()之后
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            //将服务器协议升级为WebSocket协议保持长连接 处理握手及帧的传递
                            //升级协议是通过修改状态码实现的 200升级为101
                            //WebSocket 长连接消息传递是通过帧的形式进行传递的
                            //帧 继承抽象类 WebSocketFrame 有六个子类 帧的处理由管道中下一个handler进行处理
                            //WebSocket请求形式 ：ws://localhost:1234/hello
                            pipeline.addLast(new WebSocketServerProtocolHandler("/hello"));
                            pipeline.addLast(new WebSocketLoadBalanceHandler(null));
                        }
                    });
            ChannelFuture channelFuture = bootstrap.bind(1234).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("服务端启动");
                    } else {
                        System.out.println("服务端启动失败");
                    }
                }
            });
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }


}

