package com.example.schoolmanagementsystem.controller;

import com.example.schoolmanagementsystem.Main;
import com.example.schoolmanagementsystem.model.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CourseController {

    Connection connection;
    PreparedStatement preparedStatement;

    @FXML
    private Label wrongData;

    @FXML
    private Button backButton;

    @FXML
    private TextField descriptionField, maxStudentsField, subjectField;

    @FXML
    private ComboBox<String> activityComboBox;
    @FXML
    private ComboBox<String> mandatoryComboBox;

    ObservableList<String> options = FXCollections.observableArrayList();
    ObservableList<String> activityOptions = FXCollections.observableArrayList();


    public CourseController() {
        connection = new DatabaseConnection().getConnection();
    }

    @FXML
    public void initialize() {
        options.addAll("OPTIONAL", "OBLIGATORIU");
        mandatoryComboBox.setItems(options);

        activityOptions.addAll("Laborator", "Seminar", "Curs");
        activityComboBox.setItems(activityOptions);
    }

    public void backSuperAdmin(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("SuperAdminView.fxml");
    }

    public void backAdmin(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("AdminView.fxml");
    }

    boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    @FXML
    private void handleAdd() {
        try {
            if (!isNullOrEmpty(descriptionField.getText()) &&
                    !isNullOrEmpty(subjectField.getText()) &&
                    !isNullOrEmpty(maxStudentsField.getText()) &&
                    !isNullOrEmpty(mandatoryComboBox.getSelectionModel().getSelectedItem()) &&
                    !isNullOrEmpty(activityComboBox.getSelectionModel().getSelectedItem())){

                String query = "INSERT INTO cursuri (tip, tipuri_activitati, descriere, nr_maxim_studenti, materie) VALUES ( ?, ?, ?, ?, ?)";
                PreparedStatement stmt = connection.prepareStatement(query);

                stmt.setString(1, mandatoryComboBox.getSelectionModel().getSelectedItem());
                stmt.setString(2, activityComboBox.getSelectionModel().getSelectedItem());
                stmt.setString(3, descriptionField.getText());
                stmt.setInt(4, Integer.parseInt(maxStudentsField.getText()));
                stmt.setString(5, subjectField.getText());

                int rowsAffected = stmt.executeUpdate();

                // Check if the insertion was successful
                if (rowsAffected > 0) {
                    wrongData.setText("Curs adăugat cu succes!");
                } else {
                    wrongData.setText("Eroare la adăugarea cursului!");
                }
            } else {
                // Notify the user if any required field is missing
                wrongData.setText("Toate câmpurile sunt obligatorii!");
            }

        } catch (SQLException e) {
            wrongData.setText("Could not add the course to the database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdate() {
        try {
            String query = "UPDATE cursuri SET tip = ?, tipuri_activitati = ?, descriere = ?, nr_maxim_studenti = ? WHERE materie = ?";

            // Validate inputs
            if (!isNullOrEmpty(descriptionField.getText()) &&
                    !isNullOrEmpty(subjectField.getText()) &&
                    !isNullOrEmpty(maxStudentsField.getText()) &&
                    !isNullOrEmpty(mandatoryComboBox.getSelectionModel().getSelectedItem()) &&
                    !isNullOrEmpty(activityComboBox.getSelectionModel().getSelectedItem())){

                PreparedStatement statement = connection.prepareStatement(query);
                // Set parameters
                statement.setString(1, mandatoryComboBox.getSelectionModel().getSelectedItem());
                statement.setString(2, activityComboBox.getSelectionModel().getSelectedItem());
                statement.setString(3, descriptionField.getText());
                statement.setString(4, maxStudentsField.getText());
                statement.setString(5, subjectField.getText());

                // Execute update and handle result
                if (statement.executeUpdate() > 0) {
                    wrongData.setText("Informatiile cursului au fost actualizate cu succes!");
                } else {
                    wrongData.setText("Nu exista acest curs!");
                }
            }else{
                wrongData.setText("Toate campurile trebuie completate!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            wrongData.setText("Eroare la actualizarea cursului!");
        }
    }

    @FXML
    private void handleDelete() {
        try{
            String query = "DELETE FROM cursuri WHERE materie = ?";

            if(!isNullOrEmpty(subjectField.getText())){
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, subjectField.getText());

                if (preparedStatement.executeUpdate() > 0) {
                    wrongData.setText("Cursul a fost sters cu succes!");
                } else {
                    wrongData.setText("Nu exista acest curs!");
                }

            }else{
                wrongData.setText("Completati numele cursului!");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
    }


}
