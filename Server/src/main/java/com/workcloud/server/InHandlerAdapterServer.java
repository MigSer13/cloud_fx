package com.workcloud.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;

public class InHandlerAdapterServer extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("In активирован");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Сервером получено " );
        ByteBuf buf = (ByteBuf) msg;
        StringBuilder stringBuilder = new StringBuilder();
        while (buf.readableBytes() > 0) {
            stringBuilder.append((char) buf.readByte());
        }
        ctx.write("authok");
    }

//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf) msg;
//        StringBuilder stringBuilder = new StringBuilder();
//        while (buf.readableBytes() > 0) {
//            stringBuilder.append((char) buf.readByte());
//        }
//        ctx.writeAndFlush("authok");
//    }
}
