package sample;

import java.io.IOException;
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
        chooseLoginButton.setOnAction(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/sample/editWindow.fxml"));
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
