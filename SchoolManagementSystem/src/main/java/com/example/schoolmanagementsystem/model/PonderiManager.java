
package com.example.schoolmanagementsystem.model;

public class PonderiManager {

    private static float seminarWeight;
    private static float laboratorWeight;
    private static float cursWeight;

    public static void setSeminarWeight(float seminarWeight) {
        if (seminarWeight > 0 && seminarWeight <= 1) {
            PonderiManager.seminarWeight = seminarWeight;
        } else {
            throw new IllegalArgumentException("Ponderea seminarului trebuie să fie între 0 și 1");
        }
    }

    public static void setLaboratorWeight(float laboratorWeight) {
        if (laboratorWeight > 0 && laboratorWeight <= 1) {
            PonderiManager.laboratorWeight = laboratorWeight;
        } else {
            throw new IllegalArgumentException("Ponderea laboratorului trebuie să fie între 0 și 1");
        }
    }

    public static void setCursWeight(float cursWeight) {
        if (cursWeight > 0 && cursWeight <= 1) {
            PonderiManager.cursWeight = cursWeight;
        } else {
            throw new IllegalArgumentException("Ponderea cursului trebuie să fie între 0 și 1");
        }
    }

    public static float getSeminarWeight() {
        return seminarWeight;
    }

    public static float getLaboratorWeight() {
        return laboratorWeight;
    }

    public static float getCursWeight() {
        return cursWeight;
    }
}
