import com.workcloud.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    @FXML
    public Button buttonsignIn;
    @FXML
    public TextField textFieldLogin;
    @FXML
    public PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //new Client(buttonsignIn.getScene().getWindow());
    }

    public void signInAction(ActionEvent actionEvent) {
        String login = textFieldLogin.getText().trim();
        String password = passwordField.getText().trim();
        Client.getChannel().write("auth "+login + " " + password);
    }

    public void registrationAction(ActionEvent actionEvent) {
        //зарегистрировать нового пользователя
    }



}
