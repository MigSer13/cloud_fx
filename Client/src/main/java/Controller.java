
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
    private File fileinfo;

    @FXML
    TextField textField;
    @FXML
    ListView<String> listView;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Запуск приложения");
        //updateList();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getClickCount() == 2) {
            String selectedFile = listView.getSelectionModel().getSelectedItem();
            fileinfo = new File(selectedPath + "/" +  selectedFile);
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
        if(!fileinfo.isDirectory()) {
            client.getChannel().write("upload");
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "выберите файл, а нек папку", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public void toClientAction(ActionEvent actionEvent) {
    }
}
