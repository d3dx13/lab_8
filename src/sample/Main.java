package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("startWindow.fxml"));
        primaryStage.setTitle("Авторизация");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

    }



    public static void main(String[] args) {
        launch(args);
    }
}
