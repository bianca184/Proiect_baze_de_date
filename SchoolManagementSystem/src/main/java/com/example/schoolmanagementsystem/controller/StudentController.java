package com.example.schoolmanagementsystem.controller;

import com.example.schoolmanagementsystem.Main;
import com.example.schoolmanagementsystem.model.DatabaseConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.*;

import com.example.schoolmanagementsystem.model.DatabaseConnection;

public class StudentController {
    Connection conn;
    public StudentController() {
        conn = new DatabaseConnection().getConnection();
    }
    @FXML
    private TextField numeCursField;

    @FXML
    private TextField numeGrupField;

    @FXML
    private TextField numeField;

    @FXML
    private TextField prenumeField;

    @FXML
    private TextField descriereGrupField;

    @FXML
    private TextField newGroupNameField;


    @FXML
    private TextField mesajField;
    private String username;
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
    private Button personalDataButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button createGroupButton;
    @FXML
    private Button updateGroupButton;
    @FXML
    private Button deleteGroupButton;


    public void setUserName(String username) {
        this.username = username;
    }

    @FXML
    public void createGroup(ActionEvent event) {

        String cursName = numeCursField.getText();
        String grupName = numeGrupField.getText();
        String description = descriereGrupField.getText();
        String insertQuery = "INSERT INTO grup (id_curs, nume, descriere) VALUES (?, ?, ?)";

        if(cursName.isEmpty() || grupName.isEmpty() || description.isEmpty()) {
            showAlert(AlertType.INFORMATION, "Creare grup", "Introduceti numele cursului, numele grupului si descrierea!");
        }else try{
            int courseID = getCourseIdByName(cursName);

            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);

            preparedStatement.setInt(1, courseID);
            preparedStatement.setString(2, grupName);
            preparedStatement.setString(3, description);

           int rowsInserted = preparedStatement.executeUpdate();
           if(!(rowsInserted>0)){
               showAlert(AlertType.INFORMATION, "Căutare Curs", "Nu s-a găsit niciun curs cu acest nume.");
           }else{
               showAlert(AlertType.INFORMATION, "Grup", "Grup creat cu succes!");
           }

        }catch(SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private int getCourseIdByName(String courseName) throws Exception {
        String query = "SELECT id_curs FROM cursuri WHERE materie = ?";
        try {
             PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, courseName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id_curs");
            } else {
                showAlert(AlertType.INFORMATION, "Căutare Curs", "Nu s-a găsit niciun curs cu acest nume.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new Exception();
    }

    @FXML
    public void updateGroup(ActionEvent event) {
        String newName = newGroupNameField.getText();
        String grupName = numeGrupField.getText();
        String description = descriereGrupField.getText();
        String updateQuery = "UPDATE grup SET nume = ?, descriere = ? WHERE nume = ?";

        if(newName.isEmpty() || grupName.isEmpty() || description.isEmpty()){
            showAlert(AlertType.INFORMATION, "Modificare grup", "Introduceti numele grupului, noua descriere si noul nume!");
        }else try{

            PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);

            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, grupName);

            int rowsUpdated = preparedStatement.executeUpdate();
            if(!(rowsUpdated>0)){
                showAlert(AlertType.INFORMATION, "Modificare grup", "Nu s-a găsit niciun grup cu acest nume.");
            }else{
                showAlert(AlertType.INFORMATION, "Grup", "Grup modificat cu succes!");
            }

        }catch(SQLException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    public void deleteGroup(ActionEvent event) {

        String groupName = numeGrupField.getText();

        String selectQuery = "SELECT id_grup FROM grup WHERE nume = ?";
        String procedureCall = "{CALL DeleteGroup(?)}";

        try{
             Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            //Get the group ID based on the group name
            preparedStatement.setString(1, groupName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int groupId = resultSet.getInt("id_grup");

                //Call the stored procedure to delete the group
                try (CallableStatement callableStatement = connection.prepareCall(procedureCall)) {
                    callableStatement.setInt(1, groupId);
                    callableStatement.execute();
                    showAlert(AlertType.INFORMATION, "Stergere grup", "Grup sters cu succes!");
                }
            } else {
                showAlert(AlertType.INFORMATION, "Stergere grup", "Nu s-a găsit niciun grup cu acest nume.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Căutare curs
    @FXML
    public void cautareCurs(ActionEvent actionEvent) {
        String numeCurs = numeCursField.getText();

        if (numeCurs.isEmpty()) {
            showAlert(AlertType.ERROR, "Căutare Curs", "Introduceți numele cursului!");
            return;
        }

        String query = "SELECT * FROM cursuri WHERE materie LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(AlertType.ERROR, "Eroare conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, "%" + numeCurs + "%");
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    showAlert(AlertType.INFORMATION, "Căutare Curs", "Curs găsit: " + rs.getString("materie") +
                            "\nTip: " + rs.getString("tip") +
                            "\nDescriere: " + rs.getString("Descriere"));
                } else {
                    showAlert(AlertType.INFORMATION, "Căutare Curs", "Nu s-a găsit niciun curs cu acest nume.");
                }
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Căutare Curs", "Eroare SQL: " + e.getMessage());
        }
    }

    // Înscriere la curs
    @FXML
    public void inscriereCurs(ActionEvent actionEvent) {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        String numeCurs = numeCursField.getText();

        if (nume.isEmpty() || prenume.isEmpty() || numeCurs.isEmpty()) {
            showAlert(AlertType.ERROR, "Înscriere Curs", "Introduceți toate câmpurile necesare!");
            return;
        }

        String getStudentIdQuery = "SELECT id FROM utilizatori WHERE nume = ? AND prenume = ?";
        String getCursIdQuery = "SELECT id_curs FROM cursuri WHERE materie = ?";
        String inscriereQuery = "INSERT INTO fisa_participare (utilizator_id, id_curs) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(AlertType.ERROR, "Eroare conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdQuery);
                 PreparedStatement getCursIdStmt = conn.prepareStatement(getCursIdQuery);
                 PreparedStatement inscriereStmt = conn.prepareStatement(inscriereQuery)) {

                // Căutăm ID-ul studentului
                getStudentIdStmt.setString(1, nume);
                getStudentIdStmt.setString(2, prenume);
                ResultSet studentRs = getStudentIdStmt.executeQuery();

                if (!studentRs.next()) {
                    showAlert(AlertType.ERROR, "Înscriere Curs", "Studentul specificat nu există!");
                    return;
                }
                int studentId = studentRs.getInt("id");

                // Căutăm ID-ul cursului
                getCursIdStmt.setString(1, numeCurs);
                ResultSet cursRs = getCursIdStmt.executeQuery();

                if (!cursRs.next()) {
                    showAlert(AlertType.ERROR, "Înscriere Curs", "Cursul specificat nu există!");
                    return;
                }
                int cursId = cursRs.getInt("id_curs");

                // Inserăm în tabelul fisa_participare
                inscriereStmt.setInt(1, studentId);
                inscriereStmt.setInt(2, cursId);
                inscriereStmt.executeUpdate();

                showAlert(AlertType.INFORMATION, "Înscriere Curs", "Studentul a fost înscris cu succes la cursul: " + numeCurs);
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Înscriere Curs", "Eroare SQL: " + e.getMessage());
        }
    }

    @FXML
    public void vizualizareNote(ActionEvent actionEvent) {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();

        if (nume.isEmpty() || prenume.isEmpty()) {
            showAlert(AlertType.ERROR, "Vizualizare Note", "Introduceți numele și prenumele studentului!");
            return;
        }

        String getStudentIdQuery = "SELECT id FROM utilizatori WHERE nume = ? AND prenume = ?";
        String getNotesQuery = """
        SELECT c.materie, n.seminar, n.laborator, n.curs, n.media 
        FROM note n 
        INNER JOIN cursuri c ON n.id_curs = c.id_curs 
        WHERE n.utilizator_id = ?
    """;

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(AlertType.ERROR, "Eroare conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdQuery);
                 PreparedStatement getNotesStmt = conn.prepareStatement(getNotesQuery)) {

                // Căutăm ID-ul studentului
                getStudentIdStmt.setString(1, nume);
                getStudentIdStmt.setString(2, prenume);
                ResultSet studentRs = getStudentIdStmt.executeQuery();

                if (!studentRs.next()) {
                    showAlert(AlertType.ERROR, "Vizualizare Note", "Studentul specificat nu există!");
                    return;
                }
                int studentId = studentRs.getInt("id");

                // Căutăm notele studentului
                getNotesStmt.setInt(1, studentId);
                ResultSet notesRs = getNotesStmt.executeQuery();

                StringBuilder result = new StringBuilder("Note:\n");
                while (notesRs.next()) {
                    String materie = notesRs.getString("materie");
                    int seminar = notesRs.getInt("seminar");
                    int laborator = notesRs.getInt("laborator");
                    int curs = notesRs.getInt("curs");
                    float media = notesRs.getFloat("media");

                    result.append("Materie: ").append(materie)
                            .append("\n  Seminar: ").append(seminar)
                            .append("\n  Laborator: ").append(laborator)
                            .append("\n  Curs: ").append(curs)
                            .append("\n  Media: ").append(media)
                            .append("\n\n");
                }

                if (result.toString().equals("Note:\n")) {
                    showAlert(AlertType.INFORMATION, "Vizualizare Note", "Studentul nu are note disponibile.");
                } else {
                    showAlert(AlertType.INFORMATION, "Vizualizare Note", result.toString());
                }
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Vizualizare Note", "Eroare SQL: " + e.getMessage());
        }
    }


    // Vizualizare grupuri
    @FXML
    public void vizualizareGrupuri(ActionEvent actionEvent) {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();

        if (nume.isEmpty() || prenume.isEmpty()) {
            showAlert(AlertType.ERROR, "Vizualizare Grupuri", "Introduceți numele și prenumele studentului!");
            return;
        }

        String getStudentIdQuery = "SELECT id FROM utilizatori WHERE nume = ? AND prenume = ?";
        String query = "SELECT nume, descriere FROM grup g INNER JOIN membru m ON g.id_grup = m.id_grup WHERE m.utilizator_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(AlertType.ERROR, "Eroare conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdQuery);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // Căutăm ID-ul studentului
                getStudentIdStmt.setString(1, nume);
                getStudentIdStmt.setString(2, prenume);
                ResultSet studentRs = getStudentIdStmt.executeQuery();

                if (!studentRs.next()) {
                    showAlert(AlertType.ERROR, "Vizualizare Grupuri", "Studentul specificat nu există!");
                    return;
                }
                int studentId = studentRs.getInt("id");

                // Vizualizăm grupurile
                stmt.setInt(1, studentId);
                ResultSet rs = stmt.executeQuery();

                StringBuilder result = new StringBuilder("Grupuri:\n");

                while (rs.next()) {
                    result.append("Nume: ").append(rs.getString("nume"))
                            .append(", Descriere: ").append(rs.getString("descriere"))
                            .append("\n");
                }

                showAlert(AlertType.INFORMATION, "Vizualizare Grupuri", result.toString());
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Vizualizare Grupuri", "Eroare SQL: " + e.getMessage());
        }
    }

    // Trimitere mesaj
    @FXML
    public void trimiteMesaj(ActionEvent actionEvent) {
        String mesaj = mesajField.getText();
        String nume = numeField.getText();
        String prenume = prenumeField.getText();

        if (mesaj.isEmpty() || nume.isEmpty() || prenume.isEmpty()) {
            showAlert(AlertType.ERROR, "Trimitere Mesaj", "Introduceți toate câmpurile necesare!");
            return;
        }

        String getStudentIdQuery = "SELECT id FROM utilizatori WHERE nume = ? AND prenume = ?";
        String query = "INSERT INTO mesaj (id_grup, utilizator_id, continut) VALUES (1, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(AlertType.ERROR, "Eroare conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdQuery);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // Căutăm ID-ul studentului
                getStudentIdStmt.setString(1, nume);
                getStudentIdStmt.setString(2, prenume);
                ResultSet studentRs = getStudentIdStmt.executeQuery();

                if (!studentRs.next()) {
                    showAlert(AlertType.ERROR, "Trimitere Mesaj", "Studentul specificat nu există!");
                    return;
                }
                int studentId = studentRs.getInt("id");

                // Inserăm mesajul
                stmt.setInt(1, studentId);
                stmt.setString(2, mesaj);
                stmt.executeUpdate();

                showAlert(AlertType.INFORMATION, "Trimitere Mesaj", "Mesaj trimis cu succes!");
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Trimitere Mesaj", "Eroare SQL: " + e.getMessage());
        }
    }

    // Vizualizare activități
    @FXML
    public void vizualizareActivitati(ActionEvent actionEvent) {
        String query = "SELECT * FROM activitate";
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(AlertType.ERROR, "Eroare conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                StringBuilder result = new StringBuilder("Activități:\n");

                while (rs.next()) {
                    result.append("Activitate: ").append(rs.getString("Tip"))
                            .append(", Data: ").append(rs.getDate("Data_inceperii"))
                            .append(" - ").append(rs.getDate("Data_incheierii"))
                            .append("\n");
                }

                showAlert(AlertType.INFORMATION, "Vizualizare Activități", result.toString());
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Vizualizare Activități", "Eroare SQL: " + e.getMessage());
        }
    }

    // Afișare mesaje de alertă
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void logout(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("LogInView.fxml");
    }
    @FXML
    private void showPersonalData() {
        String sql = "SELECT * FROM utilizatori WHERE username = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
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

    // Înscriere într-un grup de studiu
    @FXML
    public void inscriereGrup(ActionEvent actionEvent) {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        String grupNume = numeGrupField.getText();

        if (nume.isEmpty() || prenume.isEmpty() || grupNume.isEmpty()) {
            showAlert(AlertType.ERROR, "Înscriere Grup", "Introduceți toate câmpurile necesare!");
            return;
        }

        String getStudentIdQuery = "SELECT id FROM utilizatori WHERE nume = ? AND prenume = ?";
        String getGrupIdQuery = "SELECT id_grup FROM grup WHERE nume = ?";
        String inscriereQuery = "INSERT INTO membru (id_grup, utilizator_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(AlertType.ERROR, "Eroare conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdQuery);
                 PreparedStatement getGrupIdStmt = conn.prepareStatement(getGrupIdQuery);
                 PreparedStatement inscriereStmt = conn.prepareStatement(inscriereQuery)) {

                // Căutăm ID-ul studentului
                getStudentIdStmt.setString(1, nume);
                getStudentIdStmt.setString(2, prenume);
                ResultSet studentRs = getStudentIdStmt.executeQuery();

                if (!studentRs.next()) {
                    showAlert(AlertType.ERROR, "Înscriere Grup", "Studentul specificat nu există!");
                    return;
                }
                int studentId = studentRs.getInt("id");

                // Căutăm ID-ul grupului
                getGrupIdStmt.setString(1, grupNume);
                ResultSet grupRs = getGrupIdStmt.executeQuery();

                if (!grupRs.next()) {
                    showAlert(AlertType.ERROR, "Înscriere Grup", "Grupul specificat nu există!");
                    return;
                }
                int grupId = grupRs.getInt("id_grup");

                // Inserăm în tabelul membru
                inscriereStmt.setInt(1, grupId);
                inscriereStmt.setInt(2, studentId);
                inscriereStmt.executeUpdate();

                showAlert(AlertType.INFORMATION, "Înscriere Grup", "Studentul a fost înscris cu succes în grupul: " + grupNume);
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Înscriere Grup", "Eroare SQL: " + e.getMessage());
        }
    }

    @FXML
    public void parasireGrup(ActionEvent actionEvent) {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        String grupNume = numeGrupField.getText();

        if (nume.isEmpty() || prenume.isEmpty() || grupNume.isEmpty()) {
            showAlert(AlertType.ERROR, "Părăsire Grup", "Introduceți toate câmpurile necesare!");
            return;
        }

        String getStudentIdQuery = "SELECT id FROM utilizatori WHERE nume = ? AND prenume = ?";
        String getGrupIdQuery = "SELECT id_grup FROM grup WHERE nume = ?";
        String stergereQuery = "DELETE FROM membru WHERE id_grup = ? AND utilizator_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(AlertType.ERROR, "Eroare conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdQuery);
                 PreparedStatement getGrupIdStmt = conn.prepareStatement(getGrupIdQuery);
                 PreparedStatement stergereStmt = conn.prepareStatement(stergereQuery)) {

                // Căutăm ID-ul studentului
                getStudentIdStmt.setString(1, nume);
                getStudentIdStmt.setString(2, prenume);
                ResultSet studentRs = getStudentIdStmt.executeQuery();

                if (!studentRs.next()) {
                    showAlert(AlertType.ERROR, "Părăsire Grup", "Studentul specificat nu există!");
                    return;
                }
                int studentId = studentRs.getInt("id");

                // Căutăm ID-ul grupului
                getGrupIdStmt.setString(1, grupNume);
                ResultSet grupRs = getGrupIdStmt.executeQuery();

                if (!grupRs.next()) {
                    showAlert(AlertType.ERROR, "Părăsire Grup", "Grupul specificat nu există!");
                    return;
                }
                int grupId = grupRs.getInt("id_grup");

                // Ștergem înregistrarea din tabelul membru
                stergereStmt.setInt(1, grupId);
                stergereStmt.setInt(2, studentId);
                int rowsAffected = stergereStmt.executeUpdate();

                if (rowsAffected > 0) {
                    showAlert(AlertType.INFORMATION, "Părăsire Grup", "Studentul a părăsit cu succes grupul: " + grupNume);
                } else {
                    showAlert(AlertType.WARNING, "Părăsire Grup", "Studentul nu este membru al grupului specificat.");
                }
            }

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Părăsire Grup", "Eroare SQL: " + e.getMessage());
        }
    }




    @FXML
    public void initialize() {
        // Asigurare că toate câmpurile sunt editabile
        numeField.setEditable(true);
        prenumeField.setEditable(true);
        numeCursField.setEditable(true);
        mesajField.setEditable(true);

        // Setare text implicit pentru testare
        numeField.setPromptText("Introduceți numele");
        prenumeField.setPromptText("Introduceți prenumele");
        numeCursField.setPromptText("Introduceți numele cursului");
        mesajField.setPromptText("Introduceți mesajul");

        // Eventual, focus pe primul câmp
        numeField.requestFocus();

        // Debugging: Test clicuri pe câmpuri
        numeField.setOnMouseClicked(event -> System.out.println("Clicked on Nume Field"));
        prenumeField.setOnMouseClicked(event -> System.out.println("Clicked on Prenume Field"));

        // Debugging: Butonul "Afișează Datele Personale"
        personalDataButton.setOnAction(event -> {
            System.out.println("Afișează datele personale apăsat");
            showPersonalData();
        });
    }
    @FXML
    public void inscriereLaActivitatee(ActionEvent actionEvent) {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        String numeCurs = numeCursField.getText();
        String tipActivitate = "Laborator"; // Exemplu pentru tipul activității

        if (nume.isEmpty() || prenume.isEmpty() || numeCurs.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Introduceți toate câmpurile necesare!");
            return;
        }

        String getStudentIdQuery = "SELECT id FROM utilizatori WHERE nume = ? AND prenume = ?";
        String getCursIdQuery = "SELECT id_curs FROM cursuri WHERE materie = ?";
        String getActivitateQuery = """
        SELECT id_activitate, Nr_maxim_participanti
        FROM activitate
        WHERE id_curs = ? AND Tip = ?
        AND CURRENT_DATE() BETWEEN Data_inceperii AND Data_incheierii
    """;
        String checkCollisionQuery = """
        SELECT COUNT(*) 
        FROM activitate a
        INNER JOIN participant_curs p ON a.id_activitate = p.id_activitate
        WHERE p.utilizator_id = ? 
        AND times_collide(a.Data_inceperii, a.Durata, ?, ?) = TRUE
    """;
        String inscriereQuery = "INSERT INTO participant_curs (id_activitate, id_fisa_participare, utilizator_id) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(Alert.AlertType.ERROR, "Eroare conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdQuery);
                 PreparedStatement getCursIdStmt = conn.prepareStatement(getCursIdQuery);
                 PreparedStatement getActivitateStmt = conn.prepareStatement(getActivitateQuery);
                 PreparedStatement checkCollisionStmt = conn.prepareStatement(checkCollisionQuery);
                 PreparedStatement inscriereStmt = conn.prepareStatement(inscriereQuery)) {

                // Obține ID-ul studentului
                getStudentIdStmt.setString(1, nume);
                getStudentIdStmt.setString(2, prenume);
                ResultSet studentRs = getStudentIdStmt.executeQuery();
                if (!studentRs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Studentul specificat nu există!");
                    return;
                }
                int studentId = studentRs.getInt("id");

                // Obține ID-ul cursului
                getCursIdStmt.setString(1, numeCurs);
                ResultSet cursRs = getCursIdStmt.executeQuery();
                if (!cursRs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Cursul specificat nu există!");
                    return;
                }
                int cursId = cursRs.getInt("id_curs");

                // Obține activitatea corespunzătoare cursului
                getActivitateStmt.setInt(1, cursId);
                getActivitateStmt.setString(2, tipActivitate);
                ResultSet activitateRs = getActivitateStmt.executeQuery();
                if (!activitateRs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Nu există activități disponibile pentru acest curs!");
                    return;
                }
                int activitateId = activitateRs.getInt("id_activitate");
                int maxParticipanti = activitateRs.getInt("Nr_maxim_participanti");

                // Verifică dacă mai sunt locuri disponibile
                String checkLocuriQuery = "SELECT COUNT(*) AS inscrisi FROM participant_curs WHERE id_activitate = ?";
                try (PreparedStatement checkLocuriStmt = conn.prepareStatement(checkLocuriQuery)) {
                    checkLocuriStmt.setInt(1, activitateId);
                    ResultSet locuriRs = checkLocuriStmt.executeQuery();
                    if (locuriRs.next()) {
                        int inscrisi = locuriRs.getInt("inscrisi");
                        if (inscrisi >= maxParticipanti) {
                            showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Nu mai sunt locuri disponibile!");
                            return;
                        }
                    }
                }

                // Verifică suprapunerea activităților
                checkCollisionStmt.setInt(1, studentId);
                checkCollisionStmt.setTimestamp(2, Timestamp.valueOf("2025-01-20 10:00:00")); // Exemplu de dată
                checkCollisionStmt.setTime(3, Time.valueOf("02:00:00")); // Exemplu de durată
                ResultSet collisionRs = checkCollisionStmt.executeQuery();
                if (collisionRs.next() && collisionRs.getInt(1) > 0) {
                    showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Activitatea se suprapune cu alta!");
                    return;
                }

                // Realizează înscrierea
                inscriereStmt.setInt(1, activitateId);
                inscriereStmt.setInt(2, 1); // Exemplu pentru id_fisa_participare; trebuie determinat din baza de date
                inscriereStmt.setInt(3, studentId);
                inscriereStmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Înscriere Activitate", "Studentul a fost înscris cu succes!");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Eroare SQL: " + e.getMessage());
        }
    }


    public void renuntareLaCurs(ActionEvent actionEvent) {
    }

    @FXML
    public void inscriereLaActivitate(ActionEvent event) {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        String numeCurs = numeCursField.getText();
        String tipActivitate = "Laborator"; // Exemplu pentru tipul activității

        if (nume.isEmpty() || prenume.isEmpty() || numeCurs.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Introduceți toate câmpurile necesare!");
            return;
        }

        String getStudentIdQuery = "SELECT id FROM utilizatori WHERE nume = ? AND prenume = ?";
        String getCursIdQuery = "SELECT id_curs FROM cursuri WHERE materie = ?";
        String getActivitateQuery = """
        SELECT id_activitate, Nr_maxim_participanti, Data_inceperii, Durata
        FROM activitate
        WHERE id_curs = ? AND Tip = ?
        AND CURRENT_DATE() BETWEEN Data_inceperii AND Data_incheierii
        """;
        String checkCollisionQuery = """
        SELECT COUNT(*) 
        FROM activitate a
        INNER JOIN participant_curs p ON a.id_activitate = p.id_activitate
        WHERE p.utilizator_id = ? 
        AND times_collide(a.Data_inceperii, a.Durata, ?, ?) = TRUE
        """;
        String inscriereQuery = "INSERT INTO participant_curs (id_activitate, utilizator_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(Alert.AlertType.ERROR, "Eroare conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdQuery);
                 PreparedStatement getCursIdStmt = conn.prepareStatement(getCursIdQuery);
                 PreparedStatement getActivitateStmt = conn.prepareStatement(getActivitateQuery);
                 PreparedStatement checkCollisionStmt = conn.prepareStatement(checkCollisionQuery);
                 PreparedStatement inscriereStmt = conn.prepareStatement(inscriereQuery)) {

                // Obține ID-ul studentului
                getStudentIdStmt.setString(1, nume);
                getStudentIdStmt.setString(2, prenume);
                ResultSet studentRs = getStudentIdStmt.executeQuery();
                if (!studentRs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Studentul specificat nu există!");
                    return;
                }
                int studentId = studentRs.getInt("id");

                // Obține ID-ul cursului
                getCursIdStmt.setString(1, numeCurs);
                ResultSet cursRs = getCursIdStmt.executeQuery();
                if (!cursRs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Cursul specificat nu există!");
                    return;
                }
                int cursId = cursRs.getInt("id_curs");

                // Obține activitatea corespunzătoare cursului
                getActivitateStmt.setInt(1, cursId);
                getActivitateStmt.setString(2, tipActivitate);
                ResultSet activitateRs = getActivitateStmt.executeQuery();
                if (!activitateRs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Nu există activități disponibile pentru acest curs!");
                    return;
                }
                int activitateId = activitateRs.getInt("id_activitate");
                int maxParticipanti = activitateRs.getInt("Nr_maxim_participanti");
                Timestamp dataInceperii = activitateRs.getTimestamp("Data_inceperii");
                Time durata = activitateRs.getTime("Durata");

                // Verifică dacă mai sunt locuri disponibile
                String checkLocuriQuery = "SELECT COUNT(*) AS inscrisi FROM participant_curs WHERE id_activitate = ?";
                try (PreparedStatement checkLocuriStmt = conn.prepareStatement(checkLocuriQuery)) {
                    checkLocuriStmt.setInt(1, activitateId);
                    ResultSet locuriRs = checkLocuriStmt.executeQuery();
                    if (locuriRs.next()) {
                        int inscrisi = locuriRs.getInt("inscrisi");
                        if (inscrisi >= maxParticipanti) {
                            showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Nu mai sunt locuri disponibile!");
                            return;
                        }
                    }
                }

                // Verifică suprapunerea activităților
                checkCollisionStmt.setInt(1, studentId);
                checkCollisionStmt.setTimestamp(2, dataInceperii);
                checkCollisionStmt.setTime(3, durata);
                ResultSet collisionRs = checkCollisionStmt.executeQuery();
                if (collisionRs.next() && collisionRs.getInt(1) > 0) {
                    showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Activitatea se suprapune cu alta!");
                    return;
                }

                // Realizează înscrierea
                inscriereStmt.setInt(1, activitateId);
                inscriereStmt.setInt(2, studentId);
                inscriereStmt.executeUpdate();

                showAlert(Alert.AlertType.INFORMATION, "Înscriere Activitate", "Studentul a fost înscris cu succes!");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Înscriere Activitate", "Eroare SQL: " + e.getMessage());
        }
    }


    public void handleInscriereActivitate(ActionEvent event) {
        // Preia informațiile necesare (id activitate, utilizator etc.)
        int idActivitate = 1; // Exemplu - poți prelua dintr-un TextField sau ComboBox
        int idFisaParticipare = 1; // Exemplu
        int utilizatorId = 1; // Exemplu

        // Conexiunea la baza de date
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/proiect", "root", "parola")) {
            // Apelare procedură stocată
            CallableStatement statement = connection.prepareCall("{CALL inscriereActivitate(?, ?, ?)}");
            statement.setInt(1, idActivitate);
            statement.setInt(2, idFisaParticipare);
            statement.setInt(3, utilizatorId);

            // Execută procedura
            statement.execute();

            // Notificare pentru succes
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succes");
            alert.setHeaderText(null);
            alert.setContentText("Înscrierea la activitate a fost realizată cu succes!");
            alert.showAndWait();

        } catch (SQLIntegrityConstraintViolationException e) {
            // Dacă apare o eroare de constrângere, arată un mesaj corespunzător
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText(null);
            alert.setContentText("Nu mai sunt locuri disponibile pentru această activitate!");
            alert.showAndWait();
        } catch (SQLException e) {
            // Gestionarea altor erori SQL
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Eroare");
            alert.setHeaderText(null);
            alert.setContentText("A apărut o problemă la înscrierea în activitate.");
            alert.showAndWait();
        }
    }
    @FXML
    public void renuntareCurs(ActionEvent actionEvent) {
        String nume = numeField.getText();
        String prenume = prenumeField.getText();
        String numeCurs = numeCursField.getText();

        if (nume.isEmpty() || prenume.isEmpty() || numeCurs.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Renunțare Curs", "Introduceți toate câmpurile necesare!");
            return;
        }

        String getStudentIdQuery = "SELECT id FROM utilizatori WHERE nume = ? AND prenume = ?";
        String getCursIdQuery = "SELECT id_curs FROM cursuri WHERE materie = ?";
        String deleteQuery = "DELETE FROM fisa_participare WHERE utilizator_id = ? AND id_curs = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                showAlert(Alert.AlertType.ERROR, "Eroare Conexiune", "Conexiunea la baza de date a eșuat!");
                return;
            }

            try (PreparedStatement getStudentIdStmt = conn.prepareStatement(getStudentIdQuery);
                 PreparedStatement getCursIdStmt = conn.prepareStatement(getCursIdQuery);
                 PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {

                // Obține ID-ul studentului
                getStudentIdStmt.setString(1, nume);
                getStudentIdStmt.setString(2, prenume);
                ResultSet studentRs = getStudentIdStmt.executeQuery();
                if (!studentRs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Renunțare Curs", "Studentul specificat nu există!");
                    return;
                }
                int studentId = studentRs.getInt("id");

                // Obține ID-ul cursului
                getCursIdStmt.setString(1, numeCurs);
                ResultSet cursRs = getCursIdStmt.executeQuery();
                if (!cursRs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Renunțare Curs", "Cursul specificat nu există!");
                    return;
                }
                int cursId = cursRs.getInt("id_curs");

                // Șterge înregistrarea din tabelul fisa_participare
                deleteStmt.setInt(1, studentId);
                deleteStmt.setInt(2, cursId);
                int rowsAffected = deleteStmt.executeUpdate();

                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Renunțare Curs", "Ai renunțat cu succes la cursul: " + numeCurs);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Renunțare Curs", "Nu ești înscris la acest curs!");
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Renunțare Curs", "Eroare SQL: " + e.getMessage());
        }
    }

}