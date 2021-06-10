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
        Client client = new Client(buttonsignIn.getScene().getWindow());
        try {
            Thread.sleep(5000);
            String login = textFieldLogin.getText().trim();
            String password = passwordField.getText().trim();
            client.getChannel().writeAndFlush("auth " + login + " " + password);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void registrationAction(ActionEvent actionEvent) {
        //зарегистрировать нового пользователя
        Client.getChannel().writeAndFlush("hi");
    }


}
