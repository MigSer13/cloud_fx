package com.workcloud.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;


public class OutClientHandler extends ChannelOutboundHandlerAdapter {
    private static TypeAction typeAction = TypeAction.WAITING;

    public static void setTypeAction(TypeAction newTypeAction) {
        typeAction = newTypeAction;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (typeAction == TypeAction.WAITING) {
            ByteBuf buf = ctx.alloc().directBuffer(1);
            String s = (String) msg;
            buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
            ctx.writeAndFlush(buf);
        }
        if (typeAction == TypeAction.SEND_4Bytes) {
            ByteBuf buf = ctx.alloc().directBuffer(4);
            String s = (String) msg;
            buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
            ctx.writeAndFlush(buf);
        }
        if (typeAction == TypeAction.SEND_FILESIZE) {
            ByteBuf buf = (ByteBuf) msg;
            buf.writeBytes(buf);
            ctx.writeAndFlush(buf);
        }
        if (typeAction == TypeAction.UPLOAD) {
            ByteBuf buf = (ByteBuf) msg;
            buf.writeBytes(buf);
            ctx.writeAndFlush(buf);
        }
    }
}
