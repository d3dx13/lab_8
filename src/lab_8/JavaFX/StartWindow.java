package lab_8.JavaFX;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StartWindow {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signUpButton;

    @FXML
    private Button signInButton;

    @FXML
    private Button chooseLoginButton;

    @FXML
    private Button helpButton;

    @FXML
    private Button chooseServerButton;

    @FXML
    private Label loginText;

    @FXML
    private Label addressText;

    @FXML
    private Label portText;

    @FXML
    private Button exitButton;

    @FXML
    void initialize() {
        loginText.setText("Текущий логин: "+ClientData.login);
        chooseLoginButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/lab_8/JavaFX/loginWindow.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        addressText.setText("Адрес текущего сервера: \n"+ClientData.serverAddress);
        portText.setText("Порт текущего сервера: \n");
        chooseServerButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/lab_8/JavaFX/loginWindow.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
