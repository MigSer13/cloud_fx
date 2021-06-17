package com.workcloud.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.io.*;
import java.util.HashMap;

public class Server {
    private static int PORT = 8187;
    //private static String pathlistUsers = "D:/разное/Java/workcloud/Server/src/main/resources/usersData.txt";
    private static String pathlistUsers = "E:/Java/java5_Cloud/cloud_fx/Server/src/main/resources/usersData.txt";
    private static HashMap<String, String> users = null;

    public static HashMap<String, String> getUsers() {
        return users;
    }

    public static void main(String[] args) {
        EventLoopGroup mainGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();
        getUsersData();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(mainGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(
                                    new OutHandlerServer(),
                                    new InHandlerServer()
                            );
                        }
                    });
            ChannelFuture channelFuture = sb.bind(PORT).sync();

            System.out.println("Сервер запущен");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mainGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private static void getUsersData() {
        users = new HashMap<>();
        File file = new File(pathlistUsers);
        try {
            BufferedReader bufreader = new BufferedReader(new FileReader(file));
            String line = bufreader.readLine();
            while (line != null) {
                String[] str = line.split(" ");
                String login = str[0];
                String password = str[1];
                System.out.print(login + " " + password);
                System.out.println();

                users.put(login, password);
                line = bufreader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String addNewUser(String login, String password){
        if(users.containsKey(login)) {
            return "userExist";
        }

        try {
            FileWriter fileWriter = new FileWriter(pathlistUsers, true);
            fileWriter.write("\n" + login + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        users.put(login, password);

        return "registrationOK";
    }


}
