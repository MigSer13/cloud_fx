package com.workcloud;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class InHandler extends ByteToMessageDecoder {
    private TypeAction typeAction = TypeAction.WAITING;
    private String fileName = "";
    private int fileSize = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Подключился клиент");
    }

    @Override
    protected void decode(ChannelHandlerContext channel_ctx, ByteBuf byteBufIn, List<Object> out) throws Exception {
        if (typeAction == TypeAction.WAITING || byteBufIn.readableBytes() > 1) {
            ByteBuf buf = byteBufIn.readBytes(1);
            StringBuilder stringBuilder = new StringBuilder();
            while (buf.isReadable()) {
                stringBuilder.append((char) buf.readByte());
            }
            if (stringBuilder.toString().trim().equalsIgnoreCase("upload")) {
                channel_ctx.fireChannelRead("filename");
                typeAction = TypeAction.GET_FILENAME;
            }
            byteBufIn.release();
        }

        if (typeAction == TypeAction.GET_FILENAME || byteBufIn.readableBytes() > 4) {
            ByteBuf buf = byteBufIn.readBytes(4);
            StringBuilder stringBuilder = new StringBuilder();
            while (buf.isReadable()) {
                stringBuilder.append((char) buf.readByte());
            }
            fileName = stringBuilder.toString().trim();
            channel_ctx.fireChannelRead("filesize");
            typeAction = TypeAction.GET_FILESIZE;
            byteBufIn.release();
        }

        if (typeAction == TypeAction.GET_FILESIZE || byteBufIn.readableBytes() > 4) {
            ByteBuf buf = byteBufIn.readBytes(4);
//            StringBuilder stringBuilder = new StringBuilder();
//            while (buf.isReadable()) {
//                stringBuilder.append((char) buf.readByte());
//            }
            //fileSize = ;
            channel_ctx.fireChannelRead("upload");
            typeAction = TypeAction.UPLOAD;
            byteBufIn.release();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf = (ByteBuf) msg;
//        if (typeAction == TypeAction.WAITING) {
//            StringBuilder command = new StringBuilder();
//            while (buf.readableBytes() >= 1) {
//                command.append((char) buf.readByte());
//            }
//            if (command.toString().trim().equalsIgnoreCase("upload")) {
//                ctx.fireChannelRead("filename");
//                typeAction = TypeAction.GET_FILENAME;
//            }
//            buf.release();
//        }
//        if (typeAction == TypeAction.GET_FILENAME) {
//            StringBuilder stringBuilder = new StringBuilder();
//            while (buf.readableBytes() >= 4) {
//                stringBuilder.append((char) buf.readByte());
//            }
//            fileName = stringBuilder.toString().trim();
//            ctx.fireChannelRead("filesize");
//            typeAction = TypeAction.GET_FILESIZE;
//            buf.release();
//        }
//        if (typeAction == TypeAction.GET_FILESIZE) {
//            while (buf.readableBytes() >= 4) {
//               int d = (int) buf.readByte();
//            }
//            fileName = stringBuilder.toString().trim();
//            ctx.fireChannelRead("filesize");
//            typeAction = TypeAction.GET_FILESIZE;
//            buf.release();
//        }
//
//    }



}
