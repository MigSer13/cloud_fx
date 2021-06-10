package com.workcloud;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;


public class OutHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise channelPromise) throws Exception {
        String s = (String) msg;
        ByteBuf buf = ctx.alloc().directBuffer();
        //buf.writableBytes(s.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }
}
