package lab_8.client.ClientGUI;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lab_8.client.core.CommandParser;
import lab_8.client.core.FileParser;
import lab_8.client.core.NetworkConnection;
import lab_8.message.Message;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class SecondWindow {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button deleteButton;

    @FXML
    private TextArea bigTextBox;

    @FXML
    private TextField insertText;

    @FXML
    private Button addButton;

    @FXML
    private Button showButton;

    @FXML
    private Button addMinButton;

    @FXML
    private Button addMaxButton;

    @FXML
    void initialize() {
        addButton.setOnAction(event -> {
            String command = "add " + insertText.getText().trim();
            Message message = CommandParser.getMessageFromJSON(command);
            try {
                Message response = NetworkConnection.command(message);
                bigTextBox.appendText(response.text + "\n");
            } catch (Exception e) {
                bigTextBox.appendText(e.getMessage()+ "\n");
            }
        });

    }
}


