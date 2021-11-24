/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.lp3_5estacoes;

import DAOs.ClientDAO;
import DAOs.UserDAO;
import auth.Auth;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Client;
import models.User;
import utils.Alerts;

/**
 * FXML Controller class
 *
 * @author bruno
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField nameRegister;
    @FXML
    private TextField pswRegister;
    @FXML
    private TextField usernameRegister;
    @FXML
    private Button register;
    @FXML
    private Button cancel;
    @FXML
    private Label labelPSW;
    @FXML
    private Label labelPSW2;
    @FXML
    private Label labelPSW3;

    String salt = Auth.createSalt().get();
    ClientDAO clientDao = new ClientDAO();
    User user = new User();
    Alerts a = new Alerts();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * If BtnRegister is clicked execute the method register()
     *
     * @param event
     * @throws SQLException
     */
    @FXML
    private void BtnRegister(ActionEvent event) throws SQLException {
        register();
    }

    /**
     * If the cancel button is clicked closes the scene
     *
     * @param event
     * @throws IOException
     */
    @FXML
    private void BtnCancel(ActionEvent event) throws IOException {
        closeRegister();
    }

    /**
     * Get the inserted texts, verify if they are empty and insert in database
     */
    private void register() throws SQLException {
        if (nameRegister.getText().isEmpty()
                || usernameRegister.getText().isEmpty()
                || pswRegister.getText().isEmpty()) {
            a.showError("Preencha os campos");
        } else {
            user.setName(nameRegister.getText());
            user.setUserName(usernameRegister.getText());
            String psw = pswRegister.getText();
            String key = Auth.hashPassword(psw, salt).get();
            user.setSalt(salt);
            user.setHash(key);
            if (verificationKey(psw) == true) {
                if (clientDao.insertClientDB(user)) {
                    a.showInformation("Registo efetudo com sucesso!");
                    closeRegister();
                } else {
                    a.showError("Não foi possivel efetuar o registo");
                }
            } else {
                a.showError("Password não corresponde aos requesitos minimos");
                labelPSW.setVisible(true);
                labelPSW2.setVisible(true);
                labelPSW3.setVisible(true);
            }
        }
    }

    /**
     * Close scene Register
     */
    public void closeRegister() {
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    /**
     * Check if the password entered is in accordance with the parameters
     *
     * @param psw
     * @return
     */
    public boolean verificationKey(String psw) {
        //Regex com os requesitos da password 1 numero, 1 letra maiuscula, 1 minuscula e minimo de 8 caracteres
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=\\S+$).{8,}$";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(psw);
        return matcher.matches();
    }
}
