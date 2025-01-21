package com.example.schoolmanagementsystem.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    // Atributele utilizatorului
    private Integer userId;
    private String lastName, firstName, CNP, adress, phone, email, IBAN, contractNumber;
    private String userRole;
    private String username, password;
    private FloatProperty finalGrade=new SimpleFloatProperty(0.0f);

    // Constructor cu toți parametrii
    public User(Integer userId, String lastName, String firstName, String CNP, String adress, String phone,
                String email, String IBAN, String contractNumber, String userRole, String username,
                String password) {
        this.userId = userId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.CNP = CNP;
        this.adress = adress;
        this.phone = phone;
        this.email = email;
        this.IBAN = IBAN;
        this.contractNumber = contractNumber;
        this.userRole = userRole;
        this.username = username;
        this.password = password;
    }

    // Constructor simplificat pentru finalGrade
    public User(String lastName, String firstName,Float finalGrade) {
        this.lastName = lastName;
        this.firstName = firstName;
        setFinalGrade(finalGrade);
        
    }
    // Set the final grade to either the provided value or 0.0f if it's null
    public void setFinalGrade(Float finalGrade) {
        this.finalGrade.set((finalGrade != null) ? finalGrade : 0.0f);
    }


    // Constructor simplificat pentru nume și prenume
    public User(String lastName, String firstName) {
        this.lastName = lastName;
        this.firstName = firstName;
    }

    // Gettere și settere pentru atribute
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getFinalGrade() {
        return finalGrade.get();
    }

    public FloatProperty FinalGradeProperty () {
        return finalGrade;
    }



    // Metoda de salvare a utilizatorului în baza de date
    public void saveToDatabase(Connection connection) {
        String sql = "INSERT INTO utilizatori (Nume, Prenume, CNP, Adresa, Telefon, Email, IBAN, Numar_contract, Tip_utilizator, Username, Password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, lastName);
            preparedStatement.setString(2, firstName);
            preparedStatement.setString(3, CNP);
            preparedStatement.setString(4, adress);
            preparedStatement.setString(5, phone);
            preparedStatement.setString(6, email);
            preparedStatement.setString(7, IBAN);
            preparedStatement.setString(8, contractNumber);
            preparedStatement.setString(9, userRole);
            preparedStatement.setString(10, username);
            preparedStatement.setString(11, password);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User " + username + " inserted successfully.");
            } else {
                System.out.println("Failed to insert user " + username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metoda de căutare a unui utilizator în baza de date pe baza numelui complet
    public static void searchUserByFullName(Connection connection, String firstName, String lastName) {
        String sql = "SELECT * FROM utilizatori WHERE Nume LIKE ? AND Prenume LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

            ResultSet resultSet = preparedStatement.executeQuery();

            // Procesarea rezultatelor
            while (resultSet.next()) {
                System.out.println(
                        "ID: " + resultSet.getInt("id") +
                                ", Name: " + resultSet.getString("Nume") +
                                ", Surname: " + resultSet.getString("Prenume") +
                                ", Email: " + resultSet.getString("Email") +
                                ", Role: " + resultSet.getString("Tip_utilizator")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metoda de ștergere a unui utilizator pe baza numelui
    public static void deleteUserByName(Connection connection, String firstName, String lastName) {
        String sql = "DELETE FROM utilizatori WHERE Nume = ? AND Prenume = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User " + firstName + " " + lastName + " deleted successfully.");
            } else {
                System.out.println("User " + firstName + " " + lastName + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
