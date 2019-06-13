package lab_8.client.ClientGUI;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lab_8.client.core.CommandParser;
import lab_8.client.core.FileParser;
import lab_8.client.core.NetworkConnection;
import lab_8.message.Message;
import lab_8.world.creation.Dancer;

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
            showTable();
        });
        addMaxButton.setOnAction(event -> {
            String command = "add_if_max " + insertText.getText().trim();
            Message message = CommandParser.getMessageFromJSON(command);
            try {
                Message response = NetworkConnection.command(message);
                bigTextBox.appendText(response.text + "\n");
            } catch (Exception e) {
                bigTextBox.appendText(e.getMessage()+ "\n");
            }
            showTable();
        });
        addMinButton.setOnAction(event -> {
            String command = "add_if_min " + insertText.getText().trim();
            Message message = CommandParser.getMessageFromJSON(command);
            try {
                Message response = NetworkConnection.command(message);
                bigTextBox.appendText(response.text + "\n");
            } catch (Exception e) {
                bigTextBox.appendText(e.getMessage()+ "\n");
            }
            showTable();
        });
        deleteButton.setOnAction(event -> {
            String command = "remove " + insertText.getText().trim();
            Message message = CommandParser.getMessageFromJSON(command);
            try {
                Message response = NetworkConnection.command(message);
                bigTextBox.appendText(response.text + "\n");
            } catch (Exception e) {
                bigTextBox.appendText(e.getMessage()+ "\n");
            }
            showTable();
        });
    }
    void showTable(){
        try {
            Message response = NetworkConnection.command(CommandParser.getMessageFromJSON("show"));
            bigTextBox.clear();
            printTable(response);
        } catch (Exception e) {
            bigTextBox.clear();
            bigTextBox.appendText(e.getMessage() + "\n");
        }
    }
    /**
     * Красивый вывод команды show в виде форматированной таблички.
     * @param message Сообщение, поле values которого будет выведено.
     */
    void printTable(Message message){
        if (message.values == null)
            return;
        if (message.values.size() == 0){
            bigTextBox.appendText("\n--- Collection is Empty ---\n");
            return;
        }
        String [] tableHeader  = new String [] {
                "name",
                "birthday",
                "dance points",
                "dynamics",
                "feel",
                "think",
                "position",
                "owner"
        };
        int[] tableMaxLength = new int[8];
        for (int i = 0; i < 8; i++)
            tableMaxLength[i] = tableHeader[i].length();
        for (Object iter : message.values){
            Dancer dancer = (Dancer)iter;
            if (dancer.name != null && dancer.name.length() > tableMaxLength[0])
                tableMaxLength[0] = dancer.name.length();
            if (dancer.birthday != null && dancer.birthday.toString().length() > tableMaxLength[1])
                tableMaxLength[1] = dancer.birthday.toString().length();
            if (String.valueOf(dancer.getDanceQuality()).length() > tableMaxLength[2])
                tableMaxLength[2] = String.valueOf(dancer.getDanceQuality()).length();
            if (dancer.dynamicsStateState != null && dancer.dynamicsStateState.toString().length() > tableMaxLength[3])
                tableMaxLength[3] = dancer.dynamicsStateState.toString().length();
            if (dancer.feelState != null && dancer.feelState.toString().length() > tableMaxLength[4])
                tableMaxLength[4] = dancer.feelState.toString().length();
            if (dancer.thinkState != null && dancer.thinkState.toString().length() > tableMaxLength[5])
                tableMaxLength[5] = dancer.thinkState.toString().length();
            if (dancer.positionState != null && dancer.positionState.toString().length() > tableMaxLength[6])
                tableMaxLength[6] = dancer.positionState.toString().length();
            if (dancer.owner != null && dancer.owner.length() > tableMaxLength[7])
                tableMaxLength[7] = dancer.owner.length();
        }
        StringBuffer formatBuffer = new StringBuffer();
        for (int i = 0; i < 8; i++){
            formatBuffer.append("|%-");
            formatBuffer.append(tableMaxLength[i]);
            formatBuffer.append("s");
        }
        formatBuffer.append("|\n");
        String header = String.format(formatBuffer.toString(),
                tableHeader[0],
                tableHeader[1],
                tableHeader[2],
                tableHeader[3],
                tableHeader[4],
                tableHeader[5],
                tableHeader[6],
                tableHeader[7]);
        printLine('-', header.length() - 1);
        bigTextBox.appendText(header);
        printLine('-', header.length() - 1);
        StringBuffer stringBuffer = new StringBuffer();
        for (Object iter : message.values){
            Dancer dancer = (Dancer)iter;
            stringBuffer.append(String.format(formatBuffer.toString(),
                    dancer.name,
                    dancer.birthday,
                    dancer.getDanceQuality(),
                    dancer.dynamicsStateState,
                    dancer.feelState,
                    dancer.thinkState,
                    dancer.positionState,
                    dancer.owner));
        }
        bigTextBox.appendText(stringBuffer.toString());
        printLine('-', header.length() - 1);
    }
    /**
     * Вывести полосу из символов symbol длиной len.
     * @param symbol Символ
     * @param len Длина полосы
     */
    private void printLine(char symbol, int len){
        for (int i = 0; i < len; i++)
            bigTextBox.appendText(symbol+"");
        bigTextBox.appendText('\n'+"");
    }
}


