package com.workcloud.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;

public class OutClientHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        //String s = (String) msg;
//        ByteBuf buf = ctx.alloc().directBuffer(4);
//        Integer i = (Integer)msg;
//        //buf.writableBytes(s.getBytes(StandardCharsets.UTF_8));
//        //buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
//        ctx.writeAndFlush(buf);
        ByteBuf buf = ctx.alloc().directBuffer(4);
        String s = (String) msg;
        buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }
}
