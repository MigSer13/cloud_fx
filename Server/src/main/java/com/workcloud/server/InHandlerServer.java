package com.workcloud.server;

import com.workcloud.TypeAction;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class InHandlerServer extends ChannelInboundHandlerAdapter {
    private final String pathStorageFiles = "Files";
    private String userLogin = "";
    private String dirUser = "";
    private TypeAction typeAction = TypeAction.WAITING;
    private String fileNameUpload = "";
    private File fileDownload;
    private String userPassword = "";
    private int fileSizeUpload = 0;
    private ArrayList<String> filesList;

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
                        userLogin = login;
                        userPassword = password;
                        dirUser = pathStorageFiles + "_" + userLogin;
                        ctx.write("authOK");
                        typeAction = TypeAction.WAITING;
                        updateFilesList();
                    } else {
                        ctx.write("authNO");
                    }
                } else {
                    ctx.write("authRegisterNO");
                }
            }
        }

        if (typeAction == TypeAction.SEND_FILESIZE_DOWNLOAD || byteBufIn.readableBytes() > 1) {
            ByteBuf buf = (ByteBuf) msg;
            StringBuilder stringBuilder = new StringBuilder();
            while (buf.isReadable()) {
                stringBuilder.append((char) buf.readByte());
            }
            String command = stringBuilder.toString();
            if (command.equals("filesizeForDownloadReceived")) {
                byte[] bytes = new byte[(int) fileDownload.length()];
                FileInputStream fis = new FileInputStream(fileDownload);
                int b = 0;
                while ((b = fis.read()) != -1) {
                    ctx.write(fis.read());
                }
                typeAction = TypeAction.WAITING;
            }
        }

        if (typeAction == TypeAction.WAITING || byteBufIn.readableBytes() > 1) {
            ByteBuf buf = (ByteBuf) msg;
            StringBuilder stringBuilder = new StringBuilder();
            while (buf.isReadable()) {
                stringBuilder.append((char) buf.readByte());
            }
            String command = stringBuilder.toString();
            if (command.equals("upload")) {
                ctx.write("filename");
                typeAction = TypeAction.GET_FILENAME_UPLOAD;
            } else if (command.startsWith("download")) {
                String[] filesParameters = command.split(" ");
                String fileNameDownload = filesParameters[1];
                //отправить файл клиенту
                fileDownload = new File(dirUser + "/" + fileNameDownload);
                long fileSizeDownload = fileDownload.length();
                ctx.write(fileSizeDownload);
                typeAction = TypeAction.SEND_FILESIZE_DOWNLOAD;
            }
        }

        if (typeAction == TypeAction.GET_FILENAME_UPLOAD || byteBufIn.readableBytes() > 4) {
            ByteBuf buf = (ByteBuf) msg;
            StringBuilder stringBuilder = new StringBuilder();
            while (buf.isReadable()) {
                stringBuilder.append((char) buf.readByte());
            }
            fileNameUpload = stringBuilder.toString();
            ctx.write("filesize");
            typeAction = TypeAction.GET_FILESIZE_UPLOAD;
        }

        if (typeAction == TypeAction.GET_FILESIZE_UPLOAD || byteBufIn.readableBytes() > 4) {
            //получить размер файла в int
            Integer fileSizeUploadInteger = (Integer) msg;
            fileSizeUpload = fileSizeUploadInteger.intValue();
            ctx.write("upload");
            typeAction = TypeAction.UPLOAD;
        }

        if (typeAction == TypeAction.UPLOAD || byteBufIn.readableBytes() > fileSizeUpload) {
            ByteBuf buf = (ByteBuf) msg;
            //получить файл
            byte[] bytes = new byte[(int) fileDownload.length()];
            FileInputStream fis = new FileInputStream(fileDownload);
            File newFile = new File(pathStorageFiles + "/" + fileNameUpload);
            FileOutputStream fos = new FileOutputStream(newFile);
            int b = 0;
            while ((b = fis.read()) != -1) {
                fos.write(b);
            }
            //обновляем список и отправляем его клиенту
            updateFilesList();
            StringBuilder filenamesListServer = new StringBuilder();
            for (String s : filesList) {
                filenamesListServer.append(s + " ");
            }
            ctx.write("filenamesListServer " + filenamesListServer);
            typeAction = TypeAction.WAITING;
        }

//        ByteBuf inBuf = (ByteBuf) msg;
//        int size = (int)inBuf.readInt();
//        System.out.println(size);

//        while (inBuf.isReadable()){
//            System.out.print((char)inBuf.readByte());
//            System.out.flush();
//        }

    }

    private void updateFilesList() {
        filesList = new ArrayList<>();
        File dir = new File(dirUser);
        String[] list = dir.list();
        for (String fileName : list) {
            filesList.add(fileName);
        }
    }
}
