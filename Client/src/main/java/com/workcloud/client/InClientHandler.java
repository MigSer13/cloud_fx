package com.workcloud.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class InClientHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;
    private boolean authok;
    private Window currentWindow = null;

    public InClientHandler(Window currentWindow) {
        this.currentWindow = currentWindow;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент подключился");
        this.currentWindow = currentWindow;
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Клиент отключился");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        openWorkingWindow();

        if ( !authok ) {
            ByteBuf buf = (ByteBuf) msg;
            StringBuilder stringBuilder = new StringBuilder();
            while (buf.readableBytes() > 0) {
                stringBuilder.append((char) buf.readByte());
            }
            String answerServer = stringBuilder.toString();
            if (answerServer.equals("authok")) {
                openWorkingWindow();
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void openWorkingWindow(){
        //if( authok){
        currentWindow.hide();
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
        stage.showAndWait();
        //}
    }
}
