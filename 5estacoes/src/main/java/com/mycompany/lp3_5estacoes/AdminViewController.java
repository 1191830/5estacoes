/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lp3_5estacoes;

import DAOs.ClientDAO;
import DAOs.TripDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.xml.parsers.ParserConfigurationException;
import models.Admin;
import models.Client;
import models.Station;
import models.Trip;
import models.User;
import org.xml.sax.SAXException;
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
    @FXML
    private TableView<Client> tableClients;
    @FXML
    private TableColumn<Client, String> colIDClient;
    @FXML
    private TableColumn<Client, String> colNameClient;
    @FXML
    private TableColumn<Client, String> colUserNameClient;
    @FXML
    private TableView<Trip> tableTrips;
    @FXML
    private TableColumn<Trip, String> colIDTrip;
    @FXML
    private TableColumn<Trip, String> colDeparture;
    @FXML
    private TableColumn<Trip, String> colArrival;
    @FXML
    private TableColumn<Trip, String> colData;
    @FXML
    private TextField clienttxt;
    
    private static final boolean USER_ACTIVE = true;
    private static final boolean USER_INACTIVE = false;
    
    ClientDAO  clientDAO = new ClientDAO();
    TripDAO tripDAO = new TripDAO();
    
    ArrayList<Client> clientsList;
    ArrayList<Trip> tripsList;
    ObservableList<Client> observableListClient;
    ObservableList<Trip> observableListTrip;
    ObservableList<Station> observableList;
    
    
    /**
     * Fills the local admin with user passed as parameter
     * @param user 
     */
    public void getUser(User user){

        admin = new Admin();
        
        admin.setId(user.getId());
        admin.setUserName(user.getUserName());
        admin.setName(user.getName());
        admin.setPermission(user.getPermission());
        admin.setStatus(USER_ACTIVE);
        admin.setHash(user.getHash());
        admin.setSalt(user.getSalt());

    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTableClients();
        //if the user click in a client from the tble fills the table trips with all the trips of the selected client
        tableClients.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> loadTableTrips(newValue));
        
        //if the user click in a trip from the table calls the method to show the route
        tableTrips.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
            try {
                loadTableClientCourse(newValue);
            } catch (IOException ex) {
                Logger.getLogger(AdminViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

    }    

    /**
     * Change to anchorClients
     * @param event 
     */
    @FXML
    private void btnClients(ActionEvent event) {
        anchorClients.setVisible(true);
        anchorImportXML.setVisible(false);
        
        loadTableClients();
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
    private void btnImport(ActionEvent event) throws ParserConfigurationException, SAXException, IOException, SQLException {
               
       fileChooseXML();
        
    }
    
    /**
     * Populates the table with id, name and username and filters depending on user input
     */
    public void loadTableClients() {
        
        colIDClient.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getId())));
        colNameClient.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getName())));
        colUserNameClient.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getUserName())));
        
        clientsList = clientDAO.getClients();
        observableListClient = FXCollections.observableArrayList(clientsList);
        tableClients.setItems(observableListClient);
        
        //Initial filtered List
        FilteredList<Client> filteredData = new FilteredList<>(observableListClient, b -> true);
        
        clienttxt.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Client -> {
                
                //If no search value then display all records or whatever records it current have. No changes.
                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }
                
                String searchClient = newValue.toLowerCase();
                
                if(Client.getName().toLowerCase().indexOf(searchClient) > -1){
                    return true;
                }else if(Client.getUserName().toLowerCase().indexOf(searchClient) > -1){
                    return true;
                }else
                return false; // No match found
                
            });
        });
        
        SortedList<Client> sortedData = new SortedList <>(filteredData);
        
        //Bind sorted result with table view
        sortedData.comparatorProperty().bind(tableClients.comparatorProperty());
    
        //Aply filtered and sorted data to the table view
        tableClients.setItems(sortedData);
    
    }
    
    /**
     * Populates the table trips with all trips made by the client
     * @param client 
     */
    public void loadTableTrips(Client client){
        if(client != null){
        colIDTrip.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getId())));
        colDeparture.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getDeparture().getName())));
        colArrival.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getArrival().getName())));
        colData.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getTripDate())));
        tripsList = tripDAO.getTripsClient(client.getId());
        observableListTrip = FXCollections.observableArrayList(tripsList);
        tableTrips.setItems(observableListTrip);
        }        
    }
    
    /**
     * Opens the view for the trip selected course
     * @param trip
     * @throws IOException 
     */
    public void loadTableClientCourse(Trip trip) throws IOException{
        if(trip != null){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/courseClientView.fxml"));
        Parent root = loader.load();
                
        CourseClientViewController controller = loader.getController();
        controller.getTrip(trip);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);      
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Viagem " + trip.getDeparture() + " -> " + trip.getArrival());
        stage.showAndWait();
        }
    }
  
    /**
     * Call the view and the method to fill the table with the stations passed
     * @param stations
     * @throws IOException 
     */
    public void showTable(ArrayList<Station> stations) throws IOException{
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/importedXMLView.fxml"));
        Parent root = loader.load();
                
        ImportedXMLViewController controller = loader.getController();
        controller.getList(stations);
        
        Stage stage = new Stage();
        Scene scene = new Scene(root);      
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("Items importados");
        stage.showAndWait();       
    }
}