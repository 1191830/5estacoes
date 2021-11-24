/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lp3_5estacoes;

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Client;
import models.User;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class ClientViewController implements Initializable {

    @FXML
    private Label name;
    
    private Client client;
    @FXML
    private Button btnSelectTrip;
    @FXML
    private Button btnLogOut;
    @FXML
    private Button btnProfile;
    @FXML
    private Button btnCheckTrip;
    @FXML
    private Label userName;
    @FXML
    private AnchorPane anchorSelectTrip;
    @FXML
    private AnchorPane anchorCheckMyTrips;
    @FXML
    private AnchorPane anchorProfile;

    public void getUser(User user){
        client = new Client();
        client.setId(user.getId());
        client.setUserName(user.getUserName());
        client.setName(user.getName());
        client.setPermission(user.getPermission());
        client.setStatus(true);
        client.setHash(user.getHash());
        client.setSalt(user.getSalt());
        
        name.setText(client.getName());
        userName.setText(client.getUserName());
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void LogOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }
    
}
