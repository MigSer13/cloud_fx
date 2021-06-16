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
import java.io.IOException;

public class InClientHandler extends ChannelInboundHandlerAdapter {
    private boolean authok = false;
    private Window currentWindow = null;
    public Button buttonsignIn;
    private TypeAction typeAction = TypeAction.WAITING_AUTH;
    private Controller controller = null;

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
            } else if(answerServer.equals("authNO")){
                System.out.println("Пароль неверный");
            } else if(answerServer.equals("needRegister")){
                System.out.println("Пользователь с таким логином не найден");
            }
        }

        if (typeAction == TypeAction.WAITING || byteBufIn.readableBytes() == 1){
            StringBuilder stringBuilder = new StringBuilder();
            while (byteBufIn.readableBytes() > 0) {
                stringBuilder.append((char) byteBufIn.readByte());
            }
            String command = stringBuilder.toString();
            if (command.equals("filename")) {

            }
        }

//        if ( !authok ) {
//            ByteBuf buf = (ByteBuf) msg;
//            StringBuilder stringBuilder = new StringBuilder();
//            while (buf.readableBytes() > 0) {
//                stringBuilder.append((char) buf.readByte());
//            }
//            String answerServer = stringBuilder.toString();
////            if (answerServer.equals("authok")) {
////                openWorkingWindow();
////            }
//            ctx.write(answerServer.getBytes(StandardCharsets.UTF_8));
//        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void openWorkingWindow() {
        //if( authok){
        //currentWindow.hide();
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
        controller = (Controller)loader.getController();
        //}
    }
}
