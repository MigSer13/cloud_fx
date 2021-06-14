package com.workcloud.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;


public class OutHandlerServer extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise channelPromise) throws Exception {
        ByteBuf buf = ctx.alloc().directBuffer(4);
        String s = (String) msg;
        buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }
}
