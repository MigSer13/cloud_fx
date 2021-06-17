package com.workcloud.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.scene.control.Button;


public class Client {
    private static String ID = "localhost";
    private static int PORT = 8187;
    private static SocketChannel channel;
    public static OutClientHandler outClientHandler = null;
    private Button buttonsignIn = null;

    public static SocketChannel getChannel() {
        return channel;
    }

    public Client(Button buttonsignIn) {

        new Thread(() -> {
            EventLoopGroup workGroup = new NioEventLoopGroup();
            try {
                Bootstrap b = new Bootstrap();
                b.group(workGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel socketChannel) throws Exception {
                                channel = socketChannel;
                                socketChannel.pipeline().addLast(
                                        new OutClientHandler(),
                                        new InClientHandler(buttonsignIn)
                                );
                            }
                        });
                ChannelFuture channelFuture = b.connect(ID, PORT).sync();
                channelFuture.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                workGroup.shutdownGracefully();
            }
        }).start();
    }

    public void upload(String str){
        channel.writeAndFlush(str);
    }

}
