package com.example.schoolmanagementsystem.controller;
import com.example.schoolmanagementsystem.Main;
import com.example.schoolmanagementsystem.model.Course;
import com.example.schoolmanagementsystem.model.DatabaseConnection;
import com.example.schoolmanagementsystem.model.User;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;


public class SuperAdminController implements Initializable {
    private String userName;
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    public SuperAdminController() {
        connection = new DatabaseConnection().getConnection();
    }

    @FXML
    private Label cnpLabel;
    @FXML
    private Label numeLabel;
    @FXML
    private Label prenumeLabel;
    @FXML
    private Label adresaLabel;
    @FXML
    private Label telefonLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label ibanLabel;
    @FXML
    private Label numarContractLabel;
    @FXML
    protected Button logOutButton;
    @FXML
    protected Button deleteUsers;
    @FXML
    protected Button updateUsers;
    @FXML
    protected Button insertUsers;
    @FXML
    protected Button searchCourse;
    @FXML
    protected Button displayStudents;
    @FXML
    protected Button crudCourseButton;

    @FXML
    protected TableView<User> tableView;  //search user table
    @FXML
    protected TableColumn<User, Integer> userIdColumn;
    @FXML
    protected TableColumn<User, String> lastNameColumn;
    @FXML
    protected TableColumn<User, String> firstNameColumn;
    @FXML
    protected TableColumn<User, String> cnpColumn;
    @FXML
    protected TableColumn<User, String> adressColumn;
    @FXML
    protected TableColumn<User, String> phoneNumberColumn;
    @FXML
    protected TableColumn<User, String> emailColumn;
    @FXML
    protected TableColumn<User, String> ibanColumn;
    @FXML
    protected TableColumn<User, String> contractNumberColumn;
    @FXML
    protected TableColumn<User, String> userRoleColumn;
    @FXML
    protected TableColumn<User, String> usernameColumn;
    @FXML
    protected TableColumn<User, String> passwordColumn;

    @FXML
    protected TextField userSearchField;

    @FXML
    protected TextField firstName;
    @FXML
    protected TextField lastName;
    @FXML
    protected TextField cnp;
    @FXML
    protected TextField adress;
    @FXML
    protected TextField phoneNumber;
    @FXML
    protected TextField email;
    @FXML
    protected TextField iban;
    @FXML
    protected TextField contractNumber;

    @FXML
    protected TextField username;
    @FXML
    protected TextField password;



    @FXML
    protected TableView studentsListTableView;  // students from course table
    @FXML
    protected TextField courseNameField;
    @FXML
    protected TableColumn<User, String> lastNameColumnCourse;
    @FXML
    protected TableColumn<User, String> firstNameColumnCourse;

    @FXML
    protected TableView<Course> courseTableView;  // course table
    @FXML
    protected TableView<User> teachersTableView;
    @FXML
    protected TableColumn<Course, String> courseDescriptionColumn;
    @FXML
    protected TableColumn<Course, String> courseTypeColumn;
    @FXML
    protected TableColumn<Course, Integer> courseMaximumStudentsColumn;
    @FXML
    protected TableColumn<User, String> courseTeachersColumn;
    @FXML
    protected TableColumn<Course, String> courseActivityTypeColumn;

    @FXML
    protected Label wrongData;

    @FXML
    private ComboBox<String> userRoleComboBox;

    ObservableList<String> options = FXCollections.observableArrayList();

    ObservableList<User> searchUserObservableList = FXCollections.observableArrayList();

    ObservableList<Course> courseList = FXCollections.observableArrayList();

    ObservableList<User> teachersList = FXCollections.observableArrayList();
    @FXML
    private Button personalDataButton;


    public void setUserName(String username) {
        this.userName = username;
    }

    public void userLogOut(ActionEvent event) throws IOException {
        Main m = new Main();;
        m.changeScene("LogInView.fxml");
    }

    public void crudCursuri(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("CrudCursuriView.fxml");
    }


    public void insertUser(ActionEvent event) throws IOException {
        try {
            // Validate input fields using isNullOrEmpty
            if (!isNullOrEmpty(lastName.getText()) &&
                    !isNullOrEmpty(firstName.getText()) &&
                    !isNullOrEmpty(cnp.getText()) &&
                    !isNullOrEmpty(adress.getText()) &&
                    !isNullOrEmpty(phoneNumber.getText()) &&
                    !isNullOrEmpty(email.getText()) &&
                    !isNullOrEmpty(iban.getText()) &&
                    !isNullOrEmpty(contractNumber.getText()) &&
                    !isNullOrEmpty(userRoleComboBox.getSelectionModel().getSelectedItem()) &&
                    !isNullOrEmpty(username.getText()) &&
                    !isNullOrEmpty(password.getText())) {

                // SQL query for inserting a new user
                String query = "INSERT INTO utilizatori (nume, prenume, cnp, adresa, telefon, email, iban, numar_contract, Tip_utilizator, username, password) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                // Set values for the placeholders
                preparedStatement.setString(1, lastName.getText().trim());
                preparedStatement.setString(2, firstName.getText().trim());
                preparedStatement.setString(3, cnp.getText().trim());
                preparedStatement.setString(4, adress.getText().trim());
                preparedStatement.setString(5, phoneNumber.getText().trim());
                preparedStatement.setString(6, email.getText().trim());
                preparedStatement.setString(7, iban.getText().trim());
                preparedStatement.setString(8, contractNumber.getText().trim());
                preparedStatement.setString(9, userRoleComboBox.getSelectionModel().getSelectedItem());
                preparedStatement.setString(10, username.getText().trim());
                preparedStatement.setString(11, password.getText().trim());

                // Execute the insert query
                int rowsAffected = preparedStatement.executeUpdate();

                // Check if the insertion was successful
                if (rowsAffected > 0) {
                    wrongData.setText("Utilizator adăugat cu succes!");
                } else {
                    wrongData.setText("Eroare la adăugarea utilizatorului!");
                }
            } else {
                // Notify the user if any required field is missing
                wrongData.setText("Toate câmpurile sunt obligatorii!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            wrongData.setText("Eroare la conectarea la baza de date!");
        }
    }

    public void updateUser(ActionEvent event) throws IOException {
        try {
            String query = "UPDATE utilizatori SET cnp=?, adresa=?, telefon=?, email=?, iban=?, numar_contract=?, Tip_utilizator=?, username=?, password=? WHERE nume=? and prenume=?";

            // Validate inputs
            if (isNullOrEmpty(lastName.getText()) || isNullOrEmpty(firstName.getText()) ||
                    isNullOrEmpty(cnp.getText()) || isNullOrEmpty(adress.getText()) ||
                    isNullOrEmpty(phoneNumber.getText()) || isNullOrEmpty(email.getText()) ||
                    isNullOrEmpty(iban.getText()) || isNullOrEmpty(contractNumber.getText()) ||
                    isNullOrEmpty(userRoleComboBox.getSelectionModel().getSelectedItem()) || isNullOrEmpty(username.getText()) ||
                    isNullOrEmpty(password.getText())) {
                wrongData.setText("Toate campurile trebuie completate!");
                return;
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Set parameters
                statement.setString(1, cnp.getText());
                statement.setString(2, adress.getText());
                statement.setString(3, phoneNumber.getText());
                statement.setString(4, email.getText());
                statement.setString(5, iban.getText());
                statement.setString(6, contractNumber.getText());
                statement.setString(7, userRoleComboBox.getSelectionModel().getSelectedItem());
                statement.setString(8, username.getText());
                statement.setString(9, password.getText());
                statement.setString(10, lastName.getText().trim());
                statement.setString(11, firstName.getText().trim());

                System.out.println("Last Name: " + lastName.getText());
                System.out.println("First Name: " + firstName.getText());

                // Execute update and handle result
                if (statement.executeUpdate() > 0) {
                    wrongData.setText("Informatiile utilizatorului au fost actualizate cu succes!");
                } else {
                    wrongData.setText("Nu exista acest utilizator!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            wrongData.setText("Eroare la actualizarea utilizatorului!");
        }
    }

    boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }


    public void deleteUser(ActionEvent event) throws IOException {
        try {
            // Step 1: Check if the user exists
            String query = "SELECT id FROM utilizatori WHERE prenume = ? AND nume = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, firstName.getText().trim());
            preparedStatement.setString(2, lastName.getText().trim());
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                wrongData.setText("Nu exista acest utilizator!");
                return;
            }

            // Step 2: Get the user ID
            int userId = resultSet.getInt("id");

            // Step 3: Call the stored procedure to delete the user
            query = "CALL delete_user(?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

            // Step 4: Execute the procedure
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                wrongData.setText("Utilizator șters cu succes!");
            } else {
                wrongData.setText("Eroare la ștergerea utilizatorului!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            wrongData.setText("Eroare la conectarea la baza de date!");
        } finally {
            // Ensure resources are closed
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void searchForCourse(ActionEvent event) throws IOException {

        String courseName = courseNameField.getText().trim();
        if (courseName.isEmpty()) {
            wrongData.setText("Introduceți un nume de curs valid!");
            return;
        }

        // COURSE DATA
        String queryCourse = "SELECT tip, Tipuri_activitati, descriere, Nr_maxim_studenti FROM cursuri WHERE materie = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryCourse)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean courseExists = false;
            while(resultSet.next()) {
                courseExists = true;

                Course course = new Course(
                        resultSet.getString("tip"),
                        resultSet.getString("Tipuri_activitati"),
                        resultSet.getString("descriere"),
                        resultSet.getInt("Nr_maxim_studenti")
                );
                courseList.add(course);
            }
            if(!courseExists) {
                wrongData.setText("Cursul cu numele specificat nu există!");
                return;
            }

            courseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
            courseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
            courseMaximumStudentsColumn.setCellValueFactory(new PropertyValueFactory<>("maxStudents"));
            courseActivityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("activityType"));

            courseTableView.setItems(courseList);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        // TEACHER FOR COURSE
        String query = "SELECT nume, prenume from utilizatori where id = (SELECT utilizator_id FROM profesori WHERE cursuri_predate = ?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)){

            String course = courseNameField.getText().trim();
            if (course.matches(".*\\d$")) { // Regex: checks if the string ends with a digit
                course = course.replaceAll("\\d+$", ""); // Removes one or more digits at the end
            }

            preparedStatement.setString(1, course);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                User user = new User(resultSet.getString("nume"), resultSet.getString("prenume"));
                teachersList.add(user);
            }

            courseTeachersColumn.setCellValueFactory(cellData -> {
                User user = cellData.getValue();
                String fullName = user.getLastName() + " " + user.getFirstName(); // Combine nume and prenume
                return new SimpleStringProperty(fullName); // Return as a StringProperty
            });

            teachersTableView.setItems(teachersList);

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void selectCourse(ActionEvent event) {
        System.out.println("Butonul 'Alege curs' a fost apăsat!");
        String courseName = courseNameField.getText().trim();

        if (courseName.isEmpty()) {
            wrongData.setText("Introduceți un nume de curs valid!");
            return;
        }

        int courseId = -1;

        // Găsește id_curs din tabelul cursuri
        String queryCourseId = "SELECT id_curs FROM cursuri WHERE materie = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryCourseId)) {
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                courseId = resultSet.getInt("id_curs");
            } else {
                wrongData.setText("Cursul cu numele specificat nu există!");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Găsește utilizator_id pentru cursul specificat
        ObservableList<User> studentsForCourse = FXCollections.observableArrayList();
        String queryUserIds = "SELECT utilizator_id FROM fisa_participare WHERE id_curs = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryUserIds)) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int userId = resultSet.getInt("utilizator_id");

                // Pentru fiecare utilizator_id, extrage detaliile
                String queryUserDetails = "SELECT Nume, Prenume FROM utilizatori WHERE id = ?";
                try (PreparedStatement userDetailsStmt = connection.prepareStatement(queryUserDetails)) {
                    userDetailsStmt.setInt(1, userId);
                    ResultSet userResultSet = userDetailsStmt.executeQuery();

                    if (userResultSet.next()) {
                        String nume = userResultSet.getString("Nume");
                        String prenume = userResultSet.getString("Prenume");

                        // Creează un obiect User și adaugă-l la listă
                        studentsForCourse.add(new User(nume, prenume));
                    }
                }
            }

            // Actualizează tabelul cu lista de studenți

            lastNameColumnCourse.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            firstNameColumnCourse.setCellValueFactory(new PropertyValueFactory<>("firstName"));

            studentsListTableView.setItems(studentsForCourse);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void initialize(URL url, ResourceBundle rb) {

        //SEARCH BAR AND TABLE VIEW FILTER RESULT METHOD
        // SQL QUERY - EXECUTE IN THE BACKEND DB
        String sqlQuery = "SELECT id, nume, prenume, cnp, adresa, telefon, email, IBAN, numar_contract , tip_utilizator, username, password FROM utilizatori";

        try{
            preparedStatement = connection.prepareStatement(sqlQuery);
            resultSet = preparedStatement.executeQuery(sqlQuery);

            while (resultSet.next()){
                Integer queryId = resultSet.getInt("id");
                String queryLastName = resultSet.getString("nume");
                String queryFirstName = resultSet.getString("prenume");
                String queryCnp = resultSet.getString("cnp");
                String queryAdress = resultSet.getString("adresa");
                String queryPhoneNumber = resultSet.getString("telefon");
                String queryEmail = resultSet.getString("email");
                String queryIban = resultSet.getString("iban");
                String queryContractNumber = resultSet.getString("numar_contract");
                String queryUserRole = resultSet.getString("tip_utilizator");
                String queryUsername = resultSet.getString("username");
                String queryPassword = resultSet.getString("password");

                //POPULATE OBSERVABLE LIST
                searchUserObservableList.add(new User(queryId, queryLastName, queryFirstName, queryCnp, queryAdress, queryPhoneNumber, queryEmail,
                        queryIban, queryContractNumber, queryUserRole, queryUsername, queryPassword));
            }

            //PropertyValueFactory corresponds to new AdminService fields
            userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
            lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            cnpColumn.setCellValueFactory(new PropertyValueFactory<>("CNP"));
            adressColumn.setCellValueFactory(new PropertyValueFactory<>("adress"));
            phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
            emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
            ibanColumn.setCellValueFactory(new PropertyValueFactory<>("IBAN"));
            contractNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contractNumber"));
            userRoleColumn.setCellValueFactory(new PropertyValueFactory<>("userRole"));
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

            tableView.setItems(searchUserObservableList);

            //Initial filtered list
            FilteredList<User> filteredData = new FilteredList<>(searchUserObservableList, b -> true);

            userSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(user -> {
                    if (newValue.isEmpty() || newValue.isBlank()) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if(user.getLastName().toLowerCase().indexOf(searchKeyword) > -1){
                        return true; // we found a match in LastName
                    }else if(user.getFirstName().toLowerCase().indexOf(searchKeyword) > -1){
                        return true; // we found a match in FirstName
                    }else if(user.getCNP().toLowerCase().indexOf(searchKeyword) > -1){
                        return true;
                    }else if(user.getAdress().toLowerCase().indexOf(searchKeyword) > -1){
                        return true;
                    }else if(user.getPhone().toLowerCase().indexOf(searchKeyword) > -1){
                        return true;
                    }else if(user.getEmail().toLowerCase().indexOf(searchKeyword) > -1){
                        return true;
                    }else if(user.getIBAN().toLowerCase().indexOf(searchKeyword) > -1){
                        return true;
                    }else if(user.getContractNumber().toLowerCase().indexOf(searchKeyword) > -1){
                        return true;
                    }else if(user.getUserRole().toLowerCase().indexOf(searchKeyword) > -1){
                        return true;
                    }else if(user.getUsername().toLowerCase().indexOf(searchKeyword) > -1){
                        return true;
                    }else if(user.getPassword().toLowerCase().indexOf(searchKeyword) > -1){
                        return true;
                    }else return false; // no match found
                });
            });

            SortedList<User> sortedData = new SortedList<>(filteredData);

            //Bind sorted result with TableView
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());

            //Apply filltered and sorted data to the TableView
            tableView.setItems(sortedData);

        } catch (SQLException e){
            Logger.getLogger(SuperAdminController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }

        options.addAll("super_administrator", "administrator", "profesori", "studenti");
        userRoleComboBox.setItems(options);

    }

    @FXML
    private void showPersonalData() {
        String sql = "SELECT * FROM utilizatori WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, userName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Preia toate datele înainte de a închide ResultSet
                    String CNP = resultSet.getString("CNP");
                    String nume = resultSet.getString("Nume");
                    String prenume = resultSet.getString("Prenume");
                    String adresa = resultSet.getString("Adresa");
                    String telefon = resultSet.getString("Telefon");
                    String email = resultSet.getString("Email");
                    String iban = resultSet.getString("IBAN");
                    String numarContract = resultSet.getString("Numar_contract");

                    // Actualizează UI-ul pe thread-ul principal
                    Platform.runLater(() -> {
                        cnpLabel.setText("CNP: "+ CNP);
                        numeLabel.setText("Nume: " + nume);
                        prenumeLabel.setText("Prenume: " + prenume);
                        adresaLabel.setText("Adresa: " + adresa);
                        telefonLabel.setText("Telefon: " + telefon);
                        emailLabel.setText("Email: " + email);
                        ibanLabel.setText("IBAN: " + iban);
                        numarContractLabel.setText("Număr Contract: " + numarContract);
                    });
                } else {
                    Platform.runLater(() -> {
                        numeLabel.setText("No personal data found.");
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
