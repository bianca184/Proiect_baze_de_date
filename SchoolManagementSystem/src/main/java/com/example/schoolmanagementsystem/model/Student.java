package com.example.schoolmanagementsystem.model;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Student {
    private static int idCounter = 1; // Contor pentru ID-uri unice
    private final StringProperty name;
    private final StringProperty prenume;
    private final StringProperty cursName;
    private final IntegerProperty seminarGrade;
    private final IntegerProperty laboratorGrade;
    private final IntegerProperty cursGrade;
    private final FloatProperty weightedAverage;
    private int id;  // ID-ul unic al studentului
    private User user;
    private FloatProperty finalGrade=new SimpleFloatProperty();

    // Constructorul cu ID automat
    public Student(String name, String prenume, String cursName, int seminarGrade, int laboratorGrade, int cursGrade, float weightedAverage) {
        this.cursName = new SimpleStringProperty(cursName);
        this.id = idCounter++; // Setează ID-ul și crește contorul
        this.name = new SimpleStringProperty(name);
        this.prenume = new SimpleStringProperty(prenume);
        this.seminarGrade = new SimpleIntegerProperty(seminarGrade);
        this.laboratorGrade = new SimpleIntegerProperty(laboratorGrade);
        this.cursGrade = new SimpleIntegerProperty(cursGrade);
        this.weightedAverage = new SimpleFloatProperty();
        this.finalGrade = new SimpleFloatProperty(weightedAverage);
        calculateWeightedAverage();
    }

    // Getter pentru ID
    public int getId() {
        return id;
    }

    // Getter pentru nota finală
    public float getFinalGrade() {
        return finalGrade.get();
    }

    // Settere pentru nota finală
    public void setFinalGrade(float finalGrade) {
        this.finalGrade.set(finalGrade);
    }

    // Getter pentru obiectul User
    public User getUser() {
        return user;
    }

    // Settere pentru obiectul User
    public void setUser(User user) {
        this.user = user;
    }

    // Getter pentru nume
    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty prenumeProperty() {
        return prenume;
    }

    public StringProperty numeCursProperty() {
        return cursName;
    }

    // Gettere pentru notele fiecărei activități
    public IntegerProperty seminarGradeProperty() {
        return seminarGrade;
    }

    public IntegerProperty laboratorGradeProperty() {
        return laboratorGrade;
    }

    public IntegerProperty cursGradeProperty() {
        return cursGrade;
    }

    // Getter pentru media ponderată
    public FloatProperty weightedAverageProperty() {
        return weightedAverage;
    }

    // Metodă pentru a calcula media ponderată
    public void calculateWeightedAverage() {
        // Preluăm ponderile salvate în PonderiManager
        float seminarWeight = PonderiManager.getSeminarWeight();
        float laboratorWeight = PonderiManager.getLaboratorWeight();
        float cursWeight = PonderiManager.getCursWeight();

        // Verificăm dacă ponderile însumează 1
        float totalWeight = seminarWeight + laboratorWeight + cursWeight;
        if (Math.abs(totalWeight - 1.0) > 1e-9) {
            throw new IllegalArgumentException("Ponderile trebuie să însumeze 1.0!");
        }

        // Calculăm media ponderată
        float weightedAverageValue = (seminarGrade.get() * seminarWeight) +
                (laboratorGrade.get() * laboratorWeight) +
                (cursGrade.get() * cursWeight);

        // Setăm valoarea calculată în proprietatea weightedAverage
        weightedAverage.set(weightedAverageValue);
    }

    // Gettere pentru notele studentului
    public int getSeminarGrade() {
        return seminarGrade.get();
    }

    public int getLaboratorGrade() {
        return laboratorGrade.get();
    }

    public int getCursGrade() {
        return cursGrade.get();
    }

    // Getter pentru media ponderată
    public float getWeightedAverage() {
        return (float) weightedAverage.get();
    }

    // Settere pentru notele studentului, care vor recalcula media când se schimbă notele
    public void setSeminarGrade(int seminarGrade) {
        this.seminarGrade.set(seminarGrade);
        calculateWeightedAverage();  // Recalculăm media când se schimbă nota
    }

    public void setLaboratorGrade(int laboratorGrade) {
        this.laboratorGrade.set(laboratorGrade);
        calculateWeightedAverage();  // Recalculăm media când se schimbă nota
    }

    public void setCursGrade(int cursGrade) {
        this.cursGrade.set(cursGrade);
        calculateWeightedAverage();  // Recalculăm media când se schimbă nota
    }

    // Getter pentru numele complet
    public String getFullName() {
        return name.get() + " " + prenume.get();
    }

    // Metoda toString pentru a ajuta la afișarea informațiilor studentului
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name.get() + '\'' +
                ", seminarGrade=" + seminarGrade.get() +
                ", laboratorGrade=" + laboratorGrade.get() +
                ", cursGrade=" + cursGrade.get() +
                ", weightedAverage=" + weightedAverage.get() +
                '}';
    }

    public String getNumeCurs() {
        return cursName.get();
    }

    // Metodă pentru a salva studentul în baza de date
    public void saveToDatabase(Connection connection) {
        String sql = "INSERT INTO studenti (id, nume, prenume, curs, seminar_grade, laborator_grade, curs_grade, weighted_average, final_grade) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name.get());
            preparedStatement.setString(3, prenume.get());
            preparedStatement.setString(4, cursName.get());
            preparedStatement.setInt(5, seminarGrade.get());
            preparedStatement.setInt(6, laboratorGrade.get());
            preparedStatement.setInt(7, cursGrade.get());
            preparedStatement.setFloat(8, weightedAverage.get());
            preparedStatement.setFloat(9, finalGrade.get());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Student " + name.get() + " " + prenume.get() + " saved successfully to database.");
            } else {
                System.out.println("Failed to save student " + name.get() + " " + prenume.get());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
