package com.workcloud.server;

import com.workcloud.TypeAction;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class InHandlerServer extends ChannelInboundHandlerAdapter {
    private TypeAction typeAction = TypeAction.WAITING;
    private String fileName = "";
    private int fileSize = 0;
    private final String pathStorage = "Files_user1/";

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Подключился клиент");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBufIn = (ByteBuf) msg;
        if (typeAction == TypeAction.AUTH || byteBufIn.readableBytes() > 4) {
            StringBuilder stringBuilder = new StringBuilder();
            while (byteBufIn.isReadable()) {
                stringBuilder.append((char) byteBufIn.readByte());
            }
            String requestAuth = stringBuilder.toString();
            if (requestAuth.startsWith("auth")) {
                String[] strAuth = requestAuth.split(" ");
                String login = strAuth[1];
                String password = strAuth[2];
                if (Server.getUsers().containsKey(login)) {
                    String passBase = Server.getUsers().get(login);
                    if (passBase.equals(password)) {
                        ctx.write("authOK");
                        typeAction = TypeAction.WAITING;
                    } else {
                        ctx.write("authNO");
                    }
                } else {
                    ctx.write("authRegisterNO");
                }
            }
        }


        ByteBuf buf = (ByteBuf) msg;
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

    }
}
