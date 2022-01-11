/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lp3_5estacoes;

import DAOs.ClientDAO;
import DAOs.MachineChangeDAO;
import DAOs.TripDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
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
import javafx.scene.input.MouseEvent;
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
import utils.Parser;

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
    MachineChangeDAO machineChangeDAO = new MachineChangeDAO();
    
    ArrayList<Client> clientsList;
    ArrayList<Trip> tripsList;
    ObservableList<Client> observableListClient;
    ObservableList<Trip> observableListTrip;
    ObservableList<Station> observableList;
    @FXML
    private AnchorPane anchorMachine;
    @FXML
    private Button twoEuro;
    @FXML
    private Button fiveCent;
    @FXML
    private Button tenCent;
    @FXML
    private Button twentyCent;
    @FXML
    private Button fiftyCent;
    @FXML
    private Button oneEuro;
    @FXML
    private Label lblTwoEuro;
    @FXML
    private Label lblTenCent;
    @FXML
    private Label lblTwentyCent;
    @FXML
    private Label lblFiftyCent;
    @FXML
    private Label lblOneEuro;
    @FXML
    private Label lblFiveCent;
    @FXML
    private Button add;
    @FXML
    private Label lblTotal;
    
    ArrayList<Integer> coins = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
    ArrayList<Integer> currentCoins = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
    ArrayList<Double> currentMoney = new ArrayList<>(Arrays.asList((double)0, (double)0));
 
    static final double TWOEURO = 2;
    static final double ONEEURO = 1;
    static final double FIFTYCENT = 0.5;
    static final double TWENTYCENT = 0.2;
    static final double TENCENT = 0.1;
    static final double FIVECENT = 0.05;

    
    
    
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
        anchorMachine.setVisible(false);
        
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
        anchorMachine.setVisible(false);
    }
    
    @FXML
    private void btnMachine(ActionEvent event) {
        anchorImportXML.setVisible(false);
        anchorClients.setVisible(false);
        anchorMachine.setVisible(true);
        
        fillMachineLabels();
    }
    
    /**
     * Fills the labels and the total with coins in the database
     */
    public void fillMachineLabels(){
        //gets the coins in the machine
        currentCoins = machineChangeDAO.getMachineMoney();
        
        lblTwoEuro.setText("" + currentCoins.get(0));
        lblOneEuro.setText("" + currentCoins.get(1));
        lblFiftyCent.setText("" + currentCoins.get(2));
        lblTwentyCent.setText("" + currentCoins.get(3));
        lblTenCent.setText("" + currentCoins.get(4));
        lblFiveCent.setText("" + currentCoins.get(5));
        
        //calculate the total
        totalMachine();
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

    @FXML
    private void onClick(MouseEvent event) {
        //actual value of coins to insert
        int actual = 0;
        if(event.getSource() == twoEuro){
            //gets the actual coins of this value to insert
            actual = Integer.parseInt(lblTwoEuro.getText());
            //adds one to the value of coins inserted
            lblTwoEuro.setText("" + (actual + 1));
            //sets the array of coins to be inserted in DB
            coins.set(0, (coins.get(0) + 1));
        }else if(event.getSource() == oneEuro){
            actual = Integer.parseInt(lblOneEuro.getText());
            lblOneEuro.setText("" + (actual + 1));
            coins.set(1, (coins.get(1) + 1));
        }else if(event.getSource() == fiftyCent){
            actual = Integer.parseInt(lblFiftyCent.getText());
            lblFiftyCent.setText("" + (actual + 1));
            coins.set(2, (coins.get(2) + 1));
        }else if(event.getSource() == twentyCent){
            actual = Integer.parseInt(lblTwentyCent.getText());
            lblTwentyCent.setText("" + (actual + 1));
            coins.set(3, (coins.get(3) + 1));
        }else if(event.getSource() == tenCent){
            actual = Integer.parseInt(lblTenCent.getText());
            lblTenCent.setText("" + (actual + 1));
            coins.set(4, (coins.get(4) + 1));
        }else if(event.getSource() == fiveCent){
            actual = Integer.parseInt(lblFiveCent.getText());
            lblFiveCent.setText("" + (actual + 1));
            coins.set(5, (coins.get(5) + 1));
        }
        totalMachine();
    }
    
    /**
     * Fills the coins Array to be updated in the DB with the current DB coins
     * and the coins to be inserted
     */
    public void fillCoins(){
        currentCoins = machineChangeDAO.getMachineMoney();
        for (int i = 0; i < coins.size(); i++) {
            coins.set(i, (coins.get(i) + currentCoins.get(i)));
        }
        
    }

    @FXML
    private void btnAdd(ActionEvent event) {
        
        fillCoins();
        machineChangeDAO.chargeMachine(coins);        
    }

    @FXML
    private void btnConfiguration(ActionEvent event) {
    }
    
    /**
     * Calculates the total money to be inserted
     */
    public void totalMachine(){
        //this array will get all the values to be sum
        Double[] total = new Double[6];
        double totalMachineValue = 0;
        
        //total of coins * their value
        total[0]= Double.valueOf(Integer.parseInt(lblTwoEuro.getText()) * 2);
        total[1]= Double.valueOf(Integer.parseInt(lblOneEuro.getText()));
        total[2]= Integer.parseInt(lblFiftyCent.getText()) * 0.5;
        total[3]= Integer.parseInt(lblTwentyCent.getText()) * 0.2;
        total[4]= Integer.parseInt(lblTenCent.getText()) * 0.1;
        total[5]= Integer.parseInt(lblFiveCent.getText()) * 0.05;
        
        //runs the array to sum all the values
        for (int i = 0; i < total.length; i++) {
            totalMachineValue += total[i];
        }
        
        //this will format the value to only have 2 decimal places
        String totalValue = Parser.format(totalMachineValue);
        lblTotal.setText(totalValue + "€");
    }
}