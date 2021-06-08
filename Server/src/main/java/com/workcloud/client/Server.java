package com.workcloud.client;

import com.workcloud.InHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;

public class Server {
    private static int PORT = 8188;

    public static void main(String[] args) {
        EventLoopGroup mainGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(); 
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(mainGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new InHandler()
                            );
                        }
                    });
            ChannelFuture channelFuture = sb.bind(PORT).sync();
            System.out.println("Сервер запущен");
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            mainGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
