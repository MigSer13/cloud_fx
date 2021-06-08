import com.workcloud.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    @FXML
    public Button buttonsignIn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void signInAction(ActionEvent actionEvent) {
        //if( authok){
        buttonsignIn.getScene().getWindow().hide();
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
        //stage.getScene();
        stage.showAndWait();
        //}
    }

    public void registrationAction(ActionEvent actionEvent) {
        //зарегистрировать нового пользователя
    }
}
