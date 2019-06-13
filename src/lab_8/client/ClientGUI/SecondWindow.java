package lab_8.client.ClientGUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lab_8.client.core.CommandParser;

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
            CommandParser.getMessageFromJSON(insertText.getText());
        });

    }
}
