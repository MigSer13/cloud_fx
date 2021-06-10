package com.workcloud.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class OutClientHandler extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
       ByteBuf buf = ctx.alloc().directBuffer();
       String s = (String)msg;
       ctx.writeAndFlush(s.getBytes());
       //buf.release();
    }
}
