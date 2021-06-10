package com.workcloud.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.concurrent.EventExecutorGroup;

import java.nio.charset.StandardCharsets;

public class OutHandlerAdapterServer extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String s = (String)msg;
        ByteBuf buf = ctx.alloc().directBuffer();
        buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }
}
