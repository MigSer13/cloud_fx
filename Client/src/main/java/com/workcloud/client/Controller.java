package com.workcloud.client;

import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private Client client;

    @FXML
    TextField fileName;
    @FXML
    private Button upload;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        upload = null;
        upload.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                System.out.println(event.getX());
                System.out.println(event.getY());
            }
        });
    }
}

