package com.example.schoolmanagementsystem.controller;

import com.example.schoolmanagementsystem.Main;
import com.example.schoolmanagementsystem.model.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleFloatProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ProfesorController{
    Connection connection;
    float average;
    private String username;

    public ProfesorController() {
        connection = new DatabaseConnection().getConnection();
    }

    PreparedStatement preparedStatement;
    ResultSet resultSet;
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
    private TableView<Student> studentsTableView;
    @FXML
    private TableColumn<Student, String> studentNameColumn;
    @FXML
    private TableColumn<Student, String> studentPrenumeColumn;
    @FXML
    private TableColumn<Student, String> cursNameColumn;
    @FXML
    private TableColumn<Student, Integer> seminarGradeColumn;
    @FXML
    private TableColumn<Student, Integer> laboratorGradeColumn;
    @FXML
    private TableColumn<Student, Integer> cursGradeColumn;
    @FXML
    private TableColumn<Student, Float> averageGradeColumn;
    @FXML
    private TableColumn<User, Float> finalGradeColumn;


    @FXML
    private TableView<Activity> activitiesTableView;
    @FXML
    private TableColumn<Activity, String> activityTypeColumn;
    @FXML
    private TableColumn<Activity, String> activityDateColumn;
    @FXML
    private TableColumn<Activity, String> activityDescriptionColumn;

    @FXML
    private TextField studentNameField;
    @FXML
    private TextField studentPrenumeField;
    @FXML
    private TextField cursNameField;
    @FXML
    private TextField seminarGradeField;
    @FXML
    private TextField laboratorGradeField;
    @FXML
    private TextField cursGradeField;
    @FXML
    private TextField seminarWeightField;
    @FXML
    private TextField laboratorWeightField;
    @FXML
    private TextField cursWeightField;


    @FXML
    private ComboBox<String> activityTypeComboBox;
    @FXML
    private DatePicker activityDatePicker;
    @FXML
    private TextField activityDescriptionField;

    @FXML
    private Label resultLabel;
    @FXML
    private List<User> usersList;
    @FXML
    private Button logoutButton;

    @FXML
    private TextField courseNameField;

    @FXML
    private Button selectCourseButton;

    @FXML
    private TableView<User> studentsListTableView;

    @FXML
    private TableColumn<Student, String> studentLastNameColumn;

    @FXML
    private TableColumn<Student, String> studentFirstNameColumn;
    @FXML
    private Button downloadCatalogButton;
    @FXML
    private Button personalDataButton;


    public void setUserName(String username) {
        this.username = username;
    }

    // Setter for prenume (optional, if needed)
    public void setUserPrenume(String prenume) {
        // Set the prenume if necessary
    }
    ObservableList<Student> studentsList = FXCollections.observableArrayList();
    ObservableList<User> studentsForCourse = FXCollections.observableArrayList();

    private final ObservableList<Activity> activitiesList = FXCollections.observableArrayList();

    public void logout(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("LogInView.fxml");
    }

    @FXML
    private ComboBox<String> groupActivityComboBox;

    ObservableList<String> options = FXCollections.observableArrayList();

    @FXML
    private Label wrongData;

    @FXML
    public void addProffesorToGroupActivity(ActionEvent event) throws IOException {

        String query = "SELECT id FROM utilizatori WHERE username = ?";
        String procedure = "CALL AdaugaProfesorLaActivitateGrup(?, ?)";
        String activityQuery = "SELECT id_activitate FROM activitate_grup where descriere = ?";

        try{

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            int user_id = 0;
            if(resultSet.next()){
                user_id = resultSet.getInt("id");
            }


            System.out.println(groupActivityComboBox.getSelectionModel().getSelectedItem());


            preparedStatement = connection.prepareStatement(activityQuery);
            preparedStatement.setString(1, groupActivityComboBox.getSelectionModel().getSelectedItem().trim());
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.next()){
                wrongData.setText("Nu exista activitate cu aceasta descriere.");
            }else {
                int activity_id = resultSet.getInt("id_activitate");

                preparedStatement = connection.prepareStatement(procedure);
                preparedStatement.setInt(1, activity_id);
                preparedStatement.setInt(2, user_id);
                resultSet = preparedStatement.executeQuery();

                wrongData.setText("Ati fos inscris cu succes la activitate!");

            }

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @FXML
    private void showPersonalData() {
        String sql = "SELECT * FROM utilizatori WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, username);

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

    @FXML
    public void initialize() {
        // Setează fiecare coloană pentru Student
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        studentPrenumeColumn.setCellValueFactory(cellData -> cellData.getValue().prenumeProperty());
        cursNameColumn.setCellValueFactory(cellData -> cellData.getValue().numeCursProperty());
        seminarGradeColumn.setCellValueFactory(cellData -> cellData.getValue().seminarGradeProperty().asObject());
        laboratorGradeColumn.setCellValueFactory(cellData -> cellData.getValue().laboratorGradeProperty().asObject());
        cursGradeColumn.setCellValueFactory(cellData -> cellData.getValue().cursGradeProperty().asObject());
        averageGradeColumn.setCellValueFactory(cellData -> cellData.getValue().weightedAverageProperty().asObject());
        activityTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        activityDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        activityDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        studentLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        studentFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        finalGradeColumn.setCellValueFactory(new PropertyValueFactory<>("finalGrade"));
        // Inițializează lista activităților
        activitiesTableView.setItems(activitiesList);
        // Leagă lista de studenți la TableView
        studentsTableView.setItems(studentsList);


        addActivitiesToComboBox();
    }


    public void addActivitiesToComboBox() {

        String query = "SELECT descriere FROM activitate_grup ";
        try{
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                options.add(resultSet.getString("descriere"));
            }
            groupActivityComboBox.setItems(options);

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    @FXML
    public void addActivity(ActionEvent event) {
        try {
            String type = activityTypeComboBox.getValue();
            String date = activityDatePicker.getValue().toString();
            String description = activityDescriptionField.getText();

            // Creează o nouă activitate
            Activity newActivity = new Activity(type, date, description);

            // Adaugă activitatea în listă
            activitiesList.add(newActivity);

            // Actualizează tabelul
            activitiesTableView.setItems(activitiesList);

            // Afișează un mesaj de succes (opțional)
            resultLabel.setText("Activitate adăugată cu succes!");
        } catch (Exception e) {
            resultLabel.setText("Eroare la adăugarea activității!");
            e.printStackTrace();
        }
    }


    @FXML
    public void addStudent(ActionEvent event) {
        String studentName = studentNameField.getText();
        String studentPrenume = studentPrenumeField.getText();
        String cursName = cursNameField.getText();
        int seminarGrade = Integer.parseInt(seminarGradeField.getText());
        int laboratorGrade = Integer.parseInt(laboratorGradeField.getText());
        int cursGrade = Integer.parseInt(cursGradeField.getText());
        System.out.println(studentName + " " + seminarGrade + " " + laboratorGrade + " " + cursGrade);
        // Calculează media ponderată
        float average = (float) calculateWeightedAverage(seminarGrade, laboratorGrade, cursGrade);

        // Creează un obiect Student și adaugă-l în listă
        Student student = new Student(studentName, studentPrenume, cursName, seminarGrade, laboratorGrade, cursGrade, (float) average);
        studentsList.add(student);  // Adaugă studentul la lista observabilă
        studentsTableView.setItems(studentsList);


    }

    @FXML
    public void saveGrades(ActionEvent event) throws IOException, SQLException {
        try {
            float seminarWeight = Float.parseFloat(seminarWeightField.getText());
            float laboratorWeight = Float.parseFloat(laboratorWeightField.getText());
            float cursWeight = Float.parseFloat(cursWeightField.getText());

            // Verifică dacă suma ponderilor este 1
            if (seminarWeight + laboratorWeight + cursWeight != 1.0) {
                System.out.println("Eroare: suma ponderilor trebuie să fie 1.0!");
                return;
            }

            // Setează ponderile
            PonderiManager.setSeminarWeight((float) seminarWeight);
            PonderiManager.setLaboratorWeight((float) laboratorWeight);
            PonderiManager.setCursWeight((float) cursWeight);

            // Poți acum să salvezi sau să calculezi media
            System.out.println("Ponderile au fost setate corect!");
        } catch (NumberFormatException e) {
            System.out.println("Eroare: Vă rugăm să introduceți valori numerice valide pentru ponderi.");
        }
        addStudent(event);
        insertStudentToDatabase(event);
    }

    private void clearGradeFields() {
        studentNameField.clear();
        seminarGradeField.clear();
        laboratorGradeField.clear();
        cursGradeField.clear();
    }

    private float calculateWeightedAverage(int seminarGrade, int laboratorGrade, int cursGrade) {
        float seminarWeight = PonderiManager.getSeminarWeight();
        float laboratorWeight = PonderiManager.getLaboratorWeight();
        float cursWeight = PonderiManager.getCursWeight();

        // Verificăm dacă suma ponderilor este 1 (sau 100%).
        float totalWeight = seminarWeight + laboratorWeight + cursWeight;
        System.out.println("Suma ponderilor: " + totalWeight);
        if (totalWeight != 1.0) {
            throw new IllegalStateException("Suma ponderilor trebuie să fie 1.");
        }

        // Calculul mediei ponderate
        float average = seminarGrade * seminarWeight + laboratorGrade * laboratorWeight + cursGrade * cursWeight;
        return average;
    }


    @FXML
    public void saveWeights(ActionEvent actionEvent) {
        try {
            // Preluăm valorile introduse de utilizator
            String name = studentNameField.getText();
            String prenume = studentPrenumeField.getText();
            String numeCurs = cursNameField.getText();
            int seminarGrade = Integer.parseInt(seminarGradeField.getText()); // Nota la seminar
            int laboratorGrade = Integer.parseInt(laboratorGradeField.getText()); // Nota la laborator
            int cursGrade = Integer.parseInt(cursGradeField.getText()); // Nota la curs

            // Calculăm media ponderată
            float weightedAverage = (float) calculateWeightedAverage(seminarGrade, laboratorGrade, cursGrade);

            // Creăm un nou obiect Student
            Student newStudent = new Student(name, prenume, numeCurs, seminarGrade, laboratorGrade, cursGrade, weightedAverage);

            // Adăugăm studentul în lista de studenți
            studentsList.add(newStudent);

            // Afișăm mesajul de succes
            resultLabel.setText("Nota a fost salvată cu succes!");

            // Curățăm câmpurile de introducere pentru a permite adăugarea unui alt student
            clearGradeFields();
        } catch (NumberFormatException e) {
            // Dacă utilizatorul nu introduce un număr valid pentru note
            resultLabel.setText("Toate câmpurile de note trebuie să conțină numere valide!");
        }
    }


    private void insertStudentToDatabase(ActionEvent event) throws IOException, SQLException {
        int idStudent = 0, idCurs = 0;

        // Query 1: Găsește ID-ul studentului
        String query1 = "SELECT id FROM utilizatori WHERE Nume = ? AND Prenume = ?";
        try {
            preparedStatement = connection.prepareStatement(query1);
            preparedStatement.setString(1, studentNameField.getText());
            preparedStatement.setString(2, studentPrenumeField.getText());
            System.out.println(studentNameField.getText() + " " + studentPrenumeField.getText());
            resultSet = preparedStatement.executeQuery();


            if (!resultSet.next()) {
                System.out.println("Error: Studentul nu a fost găsit.");
                return;
            }
            idStudent = resultSet.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Query 2: Găsește ID-ul cursului
        String query2 = "SELECT id_curs FROM cursuri WHERE materie = ?";
        try {
            preparedStatement = connection.prepareStatement(query2);
            preparedStatement.setString(1, cursNameField.getText().trim());
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                System.out.println("Error: Cursul nu a fost găsit.");
                return;
            }
            idCurs = resultSet.getInt("id_curs");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        float seminarWeight = PonderiManager.getSeminarWeight();
        float laboratorWeight = PonderiManager.getLaboratorWeight();
        float cursWeight = PonderiManager.getCursWeight();

        float average = calculateWeightedAverage(
                Integer.parseInt(seminarGradeField.getText()),
                Integer.parseInt(laboratorGradeField.getText()),
                Integer.parseInt(cursGradeField.getText())
        );
        // Query 3: Introdu datele în tabelul "note"
        String sql = "INSERT INTO note (utilizator_id, id_curs, seminar, laborator, curs, media) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, idStudent); // ID-ul studentului
            preparedStatement.setInt(2, idCurs); // ID-ul cursului
            preparedStatement.setInt(3, Integer.parseInt(seminarGradeField.getText())); // Nota la seminar
            preparedStatement.setInt(4, Integer.parseInt(laboratorGradeField.getText())); // Nota la laborator
            preparedStatement.setInt(5, Integer.parseInt(cursGradeField.getText())); // Nota la curs
            preparedStatement.setFloat(6, average); // Media ponderată

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Nota a fost inserată cu succes în baza de date.");
            } else {
                System.out.println("Eroare la inserarea notei în baza de date.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Eroare SQL: " + e.getMessage());
        }
    }
    @FXML
    public void selectCourse(ActionEvent event) {
        System.out.println("Butonul 'Alege curs' a fost apăsat!");
        String courseName = courseNameField.getText().trim();

        if (courseName.isEmpty()) {
            System.out.println("Introduceți un nume de curs valid!");
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
                System.out.println("Cursul cu numele specificat nu există!");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Găsește detaliile utilizatorilor (nume, prenume, media) pentru cursul specificat
        ObservableList<User> studentsForCourse = FXCollections.observableArrayList();
        String queryUserDetails = """
    SELECT u.Nume, u.Prenume, n.media
    FROM utilizatori u
    JOIN fisa_participare fp ON u.id = fp.utilizator_id
    LEFT JOIN note n ON u.id = n.utilizator_id AND fp.id_curs = n.id_curs
    WHERE fp.id_curs = ?
    """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryUserDetails)) {
            preparedStatement.setInt(1, courseId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String nume = resultSet.getString("Nume");
                String prenume = resultSet.getString("Prenume");
                Float media = resultSet.getFloat("media");

                // Verifică dacă media este NULL (caz în care studentul nu are notă)
                if (resultSet.wasNull()) {
                    media = null; // Setăm nota ca null dacă lipsește
                }

                // Creează un obiect User și adaugă-l la listă
                studentsForCourse.add(new User(nume, prenume, media));
            }

            // Actualizează tabelul cu lista de studenți
            studentsListTableView.setItems(studentsForCourse);
            this.studentsForCourse = studentsForCourse; // Actualizează variabila globală
            System.out.println("Lista de utilizatori pentru curs:");
            for (User user : studentsForCourse) {
                System.out.println(user.getLastName() + " " + user.getFirstName() +
                        " - Nota finală: " +(user.getFinalGrade() == 0.0f ? "0" : String.valueOf(user.getFinalGrade())));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void downloadCatalog(ActionEvent event) {
        // Verifică dacă lista de studenți conține date
        System.out.println("Lista de studenți pentru curs:");
        for (User user : studentsForCourse) {
            System.out.println(user.getLastName() + " " + user.getFirstName() +
                    " - Nota finală: " + (user.getFinalGrade() == 0.0f ? "0" : String.valueOf(user.getFinalGrade())));
        }

        // Verifică dacă lista este goală
        if (studentsForCourse.isEmpty()) {
            System.out.println("Nu există studenți pentru acest curs!");
            return;
        }

        // Obține calea către desktop
        String userHome = System.getProperty("user.home");
        String desktopPath = userHome + File.separator + "Desktop" + File.separator + "catalog.txt";

        // Crează fișierul la locația specificată (pe desktop)
        File file = new File(desktopPath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Scrie capul de tabel în fișier
            writer.write("Nume | Prenume | Nota Finală\n");
            writer.write("---------------------------------\n");

            // Iterează prin lista de studenți și scrie fiecare student în fișier
            for (User user : studentsForCourse) {
                String finalGrade = (user.getFinalGrade() == 0.0f ? "0" : String.valueOf(user.getFinalGrade()));
                writer.write(user.getLastName() + " | " + user.getFirstName() + " | " + finalGrade + "\n");
            }

            System.out.println("Catalogul a fost descărcat cu succes pe desktop.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("A apărut o eroare la descărcarea catalogului.");
        }
    }


    public void downloadActivities(ActionEvent actionEvent) {
    }


}