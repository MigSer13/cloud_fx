package com.workcloud.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class InHandlerServer extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf inBuf = (ByteBuf) msg;
        while (inBuf.isReadable()){
            System.out.print((char)inBuf.readByte());
            System.out.flush();
        }
    }
}
