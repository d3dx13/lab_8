package lab_8.client.ClientGUI;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lab_8.client.core.CommandParser;
import lab_8.client.core.NetworkConnection;
import lab_8.client.userInterface.ConsoleGUI;

import static lab_8.Settings.loginMaximalLength;
import static lab_8.Settings.loginMinimalLength;

public class StartWindow {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button signInButton;

    @FXML
    private TextField loginText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private TextArea bigBox;

    @FXML
    private Button helpButton;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField emailText;

    @FXML
    void initialize() {
        signInButton.setOnAction(event -> {
            try {
                String login = loginText.getText().trim();
                if (login.length() < loginMinimalLength || login.length() > loginMaximalLength){
                    bigBox.appendText(String.format("!!! Логин должен быть от %d до %d символов !!!\n", loginMinimalLength, loginMaximalLength));
                    return;
                }
                NetworkConnection.objectCryption.setUserLogin(login);
                CommandParser.setUserLogin(login);
                String password = passwordText.getText().trim();
                if (password.equals("")) {
                    bigBox.appendText("!!! Введите ваш пароль !!!\n");
                    return;
                }
                try {
                    String resp = NetworkConnection.signIn(password);
                    System.out.print(resp);
                    bigBox.appendText(resp);
                    if (resp.substring(resp.length() - 8, resp.length()-1).equals("success"))
                        ConsoleGUI.main();
                } catch (Exception ex){
                    bigBox.appendText(ex.getMessage());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        signUpButton.setOnAction(event -> {
            try {
                String login = loginText.getText().trim();
                if (login.length() < loginMinimalLength || login.length() > loginMaximalLength){
                    bigBox.appendText(String.format("!!! Логин должен быть от %d до %d символов !!!\n", loginMinimalLength, loginMaximalLength));
                    return;
                }
                NetworkConnection.objectCryption.setUserLogin(login);
                CommandParser.setUserLogin(login);
                String email = emailText.getText().trim();
                if (email.equals("")) {
                    bigBox.appendText("!!! Введите ваш email !!!\n");
                    return;
                }
                try {
                    String resp = NetworkConnection.signUp(email);
                    System.out.print(resp);
                    bigBox.appendText(resp);
                    if (resp.substring(resp.length() - 8, resp.length()-1).equals("success"))

                    } catch (Exception ex){
                    bigBox.appendText(ex.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

/*
                    Parent root = FXMLLoader.load(getClass().getResource("/lab_8/client/ClientGUI/startWindow.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                    ((Node) (event.getSource())).getScene().getWindow().hide();
                */