package com.example.schoolmanagementsystem.controller;

import com.example.schoolmanagementsystem.Main;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminController extends SuperAdminController {

    @Override
    public void crudCursuri(ActionEvent event) throws IOException {
        Main m = new Main();
        m.changeScene("CrudCursuriAdminView.fxml");
    }

    @Override
    public void updateUser(ActionEvent event) throws IOException {

        String role = getUserRole();
        if(role == null){
            wrongData.setText("Date incorecte!");
        }
        else if(role.equals("super_administrator")){
            wrongData.setText("Nu se pot modifica informatiile pentru un super administrator!");
        }
        else{
            super.updateUser(event);
        }
    }

    @Override
    public void deleteUser(ActionEvent event) throws IOException {
        String role = getUserRole();
        if(role == null){
            wrongData.setText("Date incorecte!");
        }
        else if(role.equals("super_administrator")){
            wrongData.setText("Nu se poate sterge un super administrator!");
        }
        else{
            super.deleteUser(event);
        }
    }

    public String getUserRole(){
        try {
            String sql = "SELECT tip_utilizator FROM utilizatori WHERE prenume = ? and nume = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName.getText());
            statement.setString(2, lastName.getText());
            if(isNullOrEmpty(firstName.getText()) || isNullOrEmpty(lastName.getText())){
                return null;
            }
            resultSet = statement.executeQuery();
            if(!resultSet.next()){
                return null;
            }else{
                return resultSet.getString("Tip_utilizator");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        options.remove("super_administrator");
    }
}
