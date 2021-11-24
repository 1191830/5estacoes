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
import javax.xml.parsers.ParserConfigurationException;
import models.Admin;
import models.User;
import org.xml.sax.SAXException;
import utils.ImportXML;
import static utils.ImportXML.fileChooseXML;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class AdminViewController implements Initializable {
    private Admin admin;
    @FXML
    private Button clients;
    @FXML
    private Button importXML;
    @FXML
    private Button configuration;
    @FXML
    private Button perfil;
    @FXML
    private Button logOut;
    
    @FXML
    private AnchorPane anchorClients;
    @FXML
    private Label labelTrips;
    @FXML
    private Label labelClients;
    @FXML
    private AnchorPane anchorImportXML;
    @FXML
    private AnchorPane anchorConfiguration;
    @FXML
    private AnchorPane anchorPerfil;
    @FXML
    private Button ImportFile;



    public void getUser(User user){

        admin = new Admin();
        admin.setId(user.getId());
        admin.setUserName(user.getUserName());
        admin.setName(user.getName());
        admin.setPermission(user.getPermission());
        admin.setStatus(true);
        admin.setHash(user.getHash());
        admin.setSalt(user.getSalt());

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    /**
     * Change to anchorClients
     * @param event 
     */
    @FXML
    private void btnClients(ActionEvent event) {
        anchorClients.setVisible(true);
        anchorImportXML.setVisible(false);
    }

    /**
     * Change to anchorImportXML
     * @param event 
     */
    @FXML
    private void btnImportXML(ActionEvent event) {
        anchorImportXML.setVisible(true);
        anchorClients.setVisible(false);
    }

    /**
     * Change to anchorConfiguration
     * @param event 
     */
    @FXML
    private void btnConfiguration(ActionEvent event) {
       // anchorConfiguration.setVisible(true);
    }

    /**
     * Change to anchorPerfil
     * @param event 
     */
    @FXML
    private void btnPerfil(ActionEvent event) {
       // anchorPerfil.setVisible(true);
    }

    /**
     * Log out to aplication
     * @param event
     * @throws IOException 
     */
    @FXML
    private void btnLogOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
        
        
    }
    
    @FXML
    private void btnImport(ActionEvent event) throws ParserConfigurationException, SAXException, IOException {
        
        
        
       fileChooseXML();
        
    }
    
}