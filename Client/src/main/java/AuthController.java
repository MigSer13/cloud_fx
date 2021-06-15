import com.workcloud.client.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {

    private Client client;
    @FXML
    public Button buttonsignIn;
    @FXML
    public TextField textFieldLogin;
    @FXML
    public PasswordField passwordField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new Client(buttonsignIn);
    }

    public void signInAction(ActionEvent actionEvent) throws Exception {
        //Thread.sleep(5000);
        String login = textFieldLogin.getText().trim();
        String password = passwordField.getText().trim();
        Client.getChannel().writeAndFlush("auth " + login + " " + password);
        //client.getChannel().writeAndFlush(login);
    }

    public void registrationAction(ActionEvent actionEvent) {
        //зарегистрировать нового пользователя
        Client.getChannel().writeAndFlush("hi");
    }


}
