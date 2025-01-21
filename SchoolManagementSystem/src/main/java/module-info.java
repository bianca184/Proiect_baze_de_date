module com.example.schoolmanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.schoolmanagementsystem to javafx.fxml;
    exports com.example.schoolmanagementsystem;
    exports com.example.schoolmanagementsystem.model;

    //opens com.example.schoolmanagementsystem.model to javafx.base;
    //exports com.example.schoolmanagementsystem.model;
    exports com.example.schoolmanagementsystem.controller;
    opens com.example.schoolmanagementsystem.controller to javafx.fxml;
    opens com.example.schoolmanagementsystem.model to javafx.base, javafx.fxml;
}