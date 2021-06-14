package com.workcloud.server;

import com.workcloud.TypeAction;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InHandlerServer extends ChannelInboundHandlerAdapter {
    private TypeAction typeAction = TypeAction.WAITING;
    private String fileName = "";
    private int fileSize = 0;
    private final String pathStorage = "Files_user1/";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        StringBuilder stringBuilder = new StringBuilder();
        while (buf.isReadable()) {
            stringBuilder.append((char) buf.readByte());
        }
        String s = stringBuilder.toString();
        System.out.println(s);
        ctx.write(s);

//        ByteBuf inBuf = (ByteBuf) msg;
//        int size = (int)inBuf.readInt();
//        System.out.println(size);

//        while (inBuf.isReadable()){
//            System.out.print((char)inBuf.readByte());
//            System.out.flush();
//        }

//        ByteBuf buf = byteBufIn.readBytes(fileSize);
//        Path path = Paths.get(pathStorage + fileName);
//        Files.write(path, buf.array());
//        //добавить в List и отправить список на клиента
//        typeAction = TypeAction.WAITING;
//        byteBufIn.release();
    }
}
