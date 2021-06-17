package com.workcloud.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class InClientHandler extends ChannelInboundHandlerAdapter {
    private boolean authok = false;
    private Window currentWindow = null;
    public Button buttonsignIn;
    private TypeAction typeAction = TypeAction.WAITING_AUTH;
    private Controller controller = null;
    private static String fileName = "";
    private static String fullPathFile = "";
    private static int filesize = 0;

    public static void setFilesize(int filesize) {
        InClientHandler.filesize = filesize;
    }

    public static void setFullPathFile(String fullPathFile) {
        InClientHandler.fullPathFile = fullPathFile;
    }

    public static void setFileName(String fileName) {
        InClientHandler.fileName = fileName;
    }

    public InClientHandler(Button buttonsignIn) {
        this.buttonsignIn = buttonsignIn;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился");
        //this.currentWindow = currentWindow;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBufIn = (ByteBuf) msg;

        if (typeAction == TypeAction.WAITING_AUTH || byteBufIn.readableBytes() == 1) {
            StringBuilder stringBuilder = new StringBuilder();
            while (byteBufIn.isReadable()) {
                stringBuilder.append((char) byteBufIn.readByte());
            }
            String answerServer = stringBuilder.toString();
            if (answerServer.equals("authOK")) {
                typeAction = TypeAction.WAITING;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        openWorkingWindow();
                    }
                });
            } else if (answerServer.equals("authNO")) {
                System.out.println("Пароль неверный");
            } else if (answerServer.equals("needRegister")) {
                System.out.println("Пользователь с таким логином не найден");
            }
        }

        if (typeAction == TypeAction.WAITING) {
            StringBuilder stringBuilder = new StringBuilder();
            while (byteBufIn.readableBytes() > 0) {
                stringBuilder.append((char) byteBufIn.readByte());
            }
            String command = stringBuilder.toString();
            if (command.equals("filename")) {
                OutClientHandler.setTypeAction(TypeAction.SEND_4Bytes);
                ctx.write(fileName);
            } else if (command.equals("filesize")) {
                OutClientHandler.setTypeAction(TypeAction.SEND_FILESIZE);
                ctx.write(filesize);
            } else if (command.equals("upload")) {
                File fileToSend = new File(fullPathFile);
                OutClientHandler.setTypeAction(TypeAction.UPLOAD);
                ctx.write(fileToSend);
            } else if (command.equals("filenamesListServer")) {
                Controller.setListFiles_str(command);
            }
        }
    }

    public void openWorkingWindow() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/client.fxml"));
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("Storage");
        stage.setScene(new Scene(root));
        stage.toFront();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        controller = (Controller) loader.getController();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
