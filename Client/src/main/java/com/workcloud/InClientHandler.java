package com.workcloud;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class InClientHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
       ctx.alloc().buffer(1);
       ByteBuf buf = (ByteBuf)msg;
       ctx.writeAndFlush(buf.readBytes(1));
        buf.release();
    }
}
