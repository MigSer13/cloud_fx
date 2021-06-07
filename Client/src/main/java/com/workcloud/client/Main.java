package com.workcloud.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/client.fxml"));
        primaryStage.setTitle("Storage");
        primaryStage.setScene(new Scene(root, 300, 400));
        primaryStage.show();
        System.out.println("Запуск приложения");
        new Client();
    }


    public static void main(String[] args) {
        launch(args);
    }


}
