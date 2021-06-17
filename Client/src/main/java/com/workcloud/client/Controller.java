package com.workcloud.client;

import com.workcloud.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    private Client client;
    private String selectedPath = "D:/";
    private String selectedFile = "";
    private String fullPathSelectedFile = "";
    private File fileinfo;
    private int sizeSelectedFile = 0;
    private static String listFilesFromServer = "";

    @FXML
    TextField textField;
    @FXML
    ListView<String> listView;
    @FXML
    ListView<String> listViewServer;

    public static void setListFiles_str(String listFilesFromServer) {
        Controller.listFilesFromServer = listFilesFromServer;
    }

    public void updateList() {
        try {
            listView.getItems().clear();
            fileinfo = new File(selectedPath);
            listView.getItems().addAll(fileinfo.list());
            textField.setText(selectedPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateListServer() {
            listViewServer.getItems().clear();
            String[] str = listFilesFromServer.split(" ");
            for (int i = 1; i < str.length; i++) {
                listViewServer.getItems().add(str[i]);
            }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Запуск приложения");
        //updateList();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 1) {
            selectedFile = listView.getSelectionModel().getSelectedItem();
            fullPathSelectedFile = selectedPath + "/" +  selectedFile;
            fileinfo = new File(fullPathSelectedFile);
            sizeSelectedFile = (int)fileinfo.length();
            if(fileinfo.isDirectory()){
                selectedPath = selectedPath + "/" +  selectedFile;
                updateList();
            }
        }
    }

    public void backButtonClick(ActionEvent actionEvent) {
        if(fileinfo.getParent() == null){
            return;
        }
        selectedPath = fileinfo.getParent();
        updateList();
    }

    public void toServerAction(ActionEvent actionEvent) {
        if( !fileinfo.isDirectory()) {
            InClientHandler.setFileName(selectedFile);
            InClientHandler.setFullPathFile(fullPathSelectedFile);
            InClientHandler.setFilesize(sizeSelectedFile);
            client.getChannel().write("upload");
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "выберите файл, а не папку", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void toClientAction(ActionEvent actionEvent) {
        String selectedServerFile = listViewServer.getSelectionModel().getSelectedItem();
        OutClientHandler.setTypeAction(TypeAction.SEND_4Bytes);
        client.getChannel().write("download" + " " + selectedServerFile);

    }
}
