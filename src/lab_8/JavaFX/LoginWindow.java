package lab_8.JavaFX;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginWindow {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField newText;

    @FXML
    private Button acceptButton;

    @FXML
    void initialize() {
        acceptButton.setOnAction(event -> {
            try {
                ClientData.login = newText.getText().trim();
                if(ClientData.login.length()>2) {
                    Parent root = FXMLLoader.load(getClass().getResource("/lab_8/JavaFX/startWindow.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
