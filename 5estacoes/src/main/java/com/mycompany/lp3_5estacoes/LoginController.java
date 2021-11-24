/* 
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.lp3_5estacoes;

import DAOs.UserDAO;
import auth.Auth;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;

/**
 * FXML Controller class
 *
 * @author bruno
 */
public class LoginController implements Initializable {

    @FXML
    private Hyperlink registar;
    @FXML
    private TextField userName;
    @FXML
    private Button login;
    @FXML
    private PasswordField passwordHide;
    @FXML
    private CheckBox MostrarPSW;
    @FXML
    private TextField passwordShow;
    
    Alert a = new Alert(AlertType.NONE);
    String loginName;
    String key;
    String passShow;
    String passHide;
    User user;
    UserDAO userDAO = new UserDAO();
    
    private final static int ADMINROLE = 1;

    /**
     * @param event Show and hide the password field
     */
    @FXML
    private void CheckPSW(ActionEvent event) {

        if (MostrarPSW.isSelected()) {
            passwordShow.setText(passwordHide.getText());
            passwordShow.setVisible(true);
            passwordHide.setVisible(false);
            passwordShow.setFocusTraversable(true);
            passwordHide.setFocusTraversable(false);
            return;
        }

        passwordHide.setText(passwordShow.getText());
        passwordHide.setVisible(true);
        passwordShow.setVisible(false);
        passwordShow.setFocusTraversable(false);
        passwordHide.setFocusTraversable(true);
    }
    
    /**
     * @param event
     * @throws IOException Get username and password and validates if the fields
     * are correctly filled in, if true redirects to session
     */
    @FXML
    private void LoginButton(ActionEvent event) throws IOException {
        loginName = userName.getText();
        //Checks if user inserted any UserName
        if("".equals(loginName)){
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Insira Username");
            a.show();
        //Chekx if UserName inserted exist in DB
        }else if(userDAO.userExist(loginName)){
            user = userDAO.getUser(loginName);
            //We need to get passwords from both the hidden and show field
            passShow = passwordShow.getText();
            passHide = passwordHide.getText();
            //We check if the user checked the show password and compare the password with the hash and salt in the DB
            if((MostrarPSW.isSelected() && Auth.verifyPassword(passShow, user.getHash(), user.getSalt()))
                    || (!MostrarPSW.isSelected() && Auth.verifyPassword(passHide, user.getHash(), user.getSalt()))){
                if(user.getPermission() == ADMINROLE){
                    toLogin(event, "adminView");
                }else{
                    toLogin(event, "clientView");
                }
                
            }else{
                a.setAlertType(AlertType.ERROR);
                a.setContentText("Password Incorreta");
                a.show(); 
            }
        } else{
            a.setAlertType(AlertType.ERROR);
            a.setContentText("Username não existe");
            a.show();
        }
       
    }
    
    public void toLogin(ActionEvent event, String fxml) throws IOException{
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/" + fxml + ".FXML"));
        Parent root = loader.load();
        
        if(fxml.equals("adminView")){        
            AdminViewController adminController = loader.getController();
            adminController.getUser(user);
        }else{
            ClientViewController clientController = loader.getController();
            clientController.getUser(user);
        }
                
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);      
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * @param event Opens a window to register
     */
    @FXML
    private void RegistarLink(ActionEvent event) {
 
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("fxml/register.fxml"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Scene scene = new Scene(root);
            Stage window = new Stage();
            window.setTitle("Registar");
            window.setResizable(false);
            window.setScene(scene);
            window.showAndWait();
    }

    @FXML
    private void TextPasswordHide(ActionEvent event) {
    }

    @FXML
    private void TextPasswordShow(ActionEvent event) {
    }
    
    /**
     * @param event Event for TextField userName
     */
    @FXML
    private void TextUsername(ActionEvent event) {
    }


}
