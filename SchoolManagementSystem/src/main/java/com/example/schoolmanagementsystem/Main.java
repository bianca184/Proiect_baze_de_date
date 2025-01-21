package com.example.schoolmanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogInView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        primaryStage = stage;
        stage.setResizable(false);
        //Parent root = FXMLLoader.load(getClass().getResource("LogInView.fxml"));

        stage.setTitle("TUCN management system");
        //stage.setScene(new Scene(root, 1000, 700));
        stage.setScene(scene);
        stage.show();
    }
    public void changeScene(String fxml) throws IOException {
        URL fxmlUrl = getClass().getResource("/com/example/schoolmanagementsystem/" + fxml);
        if (fxmlUrl == null) {
            System.out.println("FXML file not found: " + fxml);  // Debugging message
            throw new IOException("FXML file not found: " + fxml);
        }
        System.out.println("Loading FXML: " + fxmlUrl);  // Debugging message
        Parent pane = FXMLLoader.load(fxmlUrl);
        primaryStage.getScene().setRoot(pane);
    }




    public Stage getStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch();
    }

}