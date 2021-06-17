package com.workcloud.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class InClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = Logger.getLogger(InClientHandler.class.getName());

    private boolean authok = false;
    public Button buttonsignIn;
    private static TypeAction typeAction = TypeAction.WAITING_AUTH;
    private Controller controller = null;
    private static String fileName = "";
    private static String fullFileNameDownload = "";
    private static String fullPathFile = "";
    private static int filesizeUpload = 0;
    private int filesizeDownload = 0;


    public static void setFileNameDownload(String fullFileNameDownload) {
        InClientHandler.fullFileNameDownload = fullFileNameDownload;
    }

    public static void setTypeAction(TypeAction typeAction) {
        InClientHandler.typeAction = typeAction;
    }

    public static void setFilesizeUpload(int filesizeUpload) {
        InClientHandler.filesizeUpload = filesizeUpload;
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
        log.info("Клиент подключился");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Клиент отключился");
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
                log.info("удачная авторизация клиента");
                typeAction = TypeAction.WAITING;
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        openWorkingWindow();
                    }
                });
            } else if (answerServer.equals("authNO")) {
                log.info("неверный пароль при авторизации");
            } else if (answerServer.equals("needRegister")) {
                log.info("не найден пользователь с указанным логином");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Пользователь с таким логином не найден", ButtonType.OK);
            } else if (answerServer.equals("userExist")) {
                log.info("сообщение при регистрации - пользователь уже существует");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Пользователь успешно добавлен", ButtonType.OK);
            }else if (answerServer.equals("registrationOK")) {
                log.info("добавлен новый пользователь");
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Пользователь успешно добавлен", ButtonType.OK);
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
                ctx.write(filesizeUpload);
            } else if (command.equals("upload")) {
                File fileToSend = new File(fullPathFile);
                OutClientHandler.setTypeAction(TypeAction.UPLOAD);
                ctx.write(fileToSend);
            } else if (command.equals("filenamesListServer")) {
                Controller.setListFiles_str(command);
            }
        }

        if (typeAction == TypeAction.GET_FILESIZE_DOWNLOAD) {
            filesizeUpload = byteBufIn.readInt();
            OutClientHandler.setTypeAction(TypeAction.WAITING);
            ctx.write("filesizeForDownloadReceived");
            InClientHandler.setTypeAction(TypeAction.DOWNLOAD);
        }
        if (typeAction == TypeAction.DOWNLOAD) {
            byte[] bytes = new byte[filesizeDownload];
            File newFile = new File(fullFileNameDownload);
            FileOutputStream fos = new FileOutputStream(newFile);
            int b = 0;
            while ((b = byteBufIn.readByte()) != -1) {
                fos.write(b);
            }
            InClientHandler.setTypeAction(TypeAction.WAITING);
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
