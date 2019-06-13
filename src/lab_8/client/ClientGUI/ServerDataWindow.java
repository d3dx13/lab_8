package lab_8.client.ClientGUI;

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

public class ServerDataWindow {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField addressText;

    @FXML
    private Button acceptButton;

    @FXML
    private TextField portText;

    @FXML
    void initialize() {
        acceptButton.setOnAction(event -> {
            try {
                ClientData.serverAddress = addressText.getText().trim();
                if(ClientData.serverAddress.length()>2) {
                    Parent root = FXMLLoader.load(getClass().getResource("/lab_8/client/ClientGUI/startWindow.fxml"));
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
