package com.example.schoolmanagementsystem.controller;
import com.example.schoolmanagementsystem.Main;
import com.example.schoolmanagementsystem.model.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.event.ActionEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class LogIn implements Initializable {

    Connection connection;

    public LogIn() {
        connection = new DatabaseConnection().getConnection();
    }

    @FXML
    private Button logInButton;
    @FXML
    private Label wrongLogIn;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    PreparedStatement preparedStatement;
    ResultSet resultSet;
    public void userLogIn(ActionEvent event) throws IOException {
        Main m = new Main();
        String name = username.getText().toString();
        String pass = password.getText().toString();
        String sql = "SELECT * FROM utilizatori WHERE username= ? AND password= ?";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, pass);

            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                wrongLogIn.setText("Wrong Username or Password");
            } else {
                wrongLogIn.setText("Successfully logged in");

                // Obține rolul și alte informații necesare
                String role = resultSet.getString("Tip_utilizator");

                switch (role) {
                    case "administrator":
                        // Încărcarea FXML-ului pentru profesori
                        FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/com/example/schoolmanagementsystem/AdminView.fxml"));
                        Parent root3 = loader3.load();

                        // Transmiterea numelui și prenumelui către controller
                        AdminController controller3 = loader3.getController();
                        controller3.setUserName(name);

                        // Crearea scenei cu root-ul și schimbarea acesteia
                        Scene scene3 = new Scene(root3);
                        m.getStage().setScene(scene3);
                        break;
                    case "super_administrator":
                        // Încărcarea FXML-ului pentru profesori
                        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/com/example/schoolmanagementsystem/SuperAdminView.fxml"));
                        Parent root1 = loader1.load();

                        // Transmiterea numelui și prenumelui către controller
                        SuperAdminController controller1 = loader1.getController();
                        controller1.setUserName(name);

                        // Crearea scenei cu root-ul și schimbarea acesteia
                        Scene scene1 = new Scene(root1);
                        m.getStage().setScene(scene1);
                        break;

                    case "studenti":
                        // Încărcarea FXML-ului pentru profesori
                        FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/com/example/schoolmanagementsystem/StudentView.fxml"));
                        Parent root2 = loader2.load();

                        // Transmiterea numelui și prenumelui către controller
                        StudentController controller2 = loader2.getController();
                        controller2.setUserName(name);

                        // Crearea scenei cu root-ul și schimbarea acesteia
                        Scene scene2 = new Scene(root2);
                        m.getStage().setScene(scene2);
                        break;
                    case "profesori":
                        // Încărcarea FXML-ului pentru profesori
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/schoolmanagementsystem/ProfesoriView.fxml"));
                        Parent root = loader.load();

                        // Transmiterea numelui și prenumelui către controller
                        ProfesorController controller = loader.getController();
                        controller.setUserName(name);

                        // Crearea scenei cu root-ul și schimbarea acesteia
                        Scene scene = new Scene(root);
                        m.getStage().setScene(scene);
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
