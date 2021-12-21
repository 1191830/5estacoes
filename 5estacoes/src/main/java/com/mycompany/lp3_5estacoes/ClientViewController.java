/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lp3_5estacoes;

import DAOs.StationDAO;
import DAOs.TripDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import models.Client;
import models.Route;
import models.Station;
import models.Trip;
import models.User;
import utils.Alerts;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class ClientViewController implements Initializable {
    @FXML
    private Label name;
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
    private ComboBox<Station> selectDeparture;
    @FXML
    private ComboBox<Station> selectArrival;
    @FXML
    private TableView<Trip> tableTrips;
    @FXML
    private TableColumn<Trip, String> colIDTrip;
    @FXML
    private TableColumn<Trip, String> colDeparture;
    @FXML
    private TableColumn<Trip, String> colArrival;
    @FXML
    private TableColumn<Trip, String> colDate;
    @FXML
    private Button btnSeeRoutes;
 
    private Alerts alert =  new Alerts();
    private Client client;
    private Route route = new Route();
    private Trip trip = new Trip();
    
    private TripDAO tripDAO = new TripDAO();
    private StationDAO stationDAO;

    ObservableList<Trip> observableListTrip;
    ObservableList<Station> observableListStations;
    ArrayList<Station> stations;
    ArrayList<Trip> tripsList;
    ArrayList<Route> allRoutes;  

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stationDAO = new StationDAO();
        stations = stationDAO.getStations();
        fillSelectArrivalAndDeparture();
        
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
     * Validates the station selection and show table with the routes between them 
     * @param event
     * @throws SQLException
     * @throws IOException 
     */
    @FXML
    private void btnSeeRoutes(ActionEvent event) throws SQLException, IOException {
        if (validationSelectStations()) {
            allRoutes = route.populateMetroAndFindRoute(selectDeparture.getSelectionModel().getSelectedItem().getName(),selectArrival.getSelectionModel().getSelectedItem().getName());
            showAllRoutes();
        } else {
            this.alert.showError("Escolha uma viagem válida");
        }
    }
    
    /**
     * Check the trips made by a client
     * @param event 
     */
    @FXML
    private void checkTrips(ActionEvent event) {
       anchorCheckMyTrips.setVisible(true);
        anchorSelectTrip.setVisible(false);
        loadTableTrips();
    }
    
    /**
     * LogOut from a session as a client
     * @param event
     * @throws IOException 
     */
    @FXML
    private void LogOut(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.centerOnScreen();
        stage.show();
    }
    
    /**
     * Shows the window to plan trips and hides others
     * @param event 
     */
    @FXML
    private void selectTrip(ActionEvent event) {
        anchorCheckMyTrips.setVisible(false);
        anchorSelectTrip.setVisible(true);        
    }
    
    /**
     * Receives a combobox an filters the content by user input
     * @param combo 
     */
    public void autoCompleteComboBox(ComboBox<Station> combo){
        // Create a FilteredList wrapping the ObservableList.
        FilteredList<Station> filteredItems = new FilteredList<>(observableListStations, p -> true);

        // Add a listener to the textProperty of the combobox editor. The
        // listener will simply filter the list every time the input is changed
        combo.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            final TextField editor = combo.getEditor();
            final Station selected = combo.getSelectionModel().getSelectedItem();

            // This needs run on the GUI thread to avoid the error described
            // here: https://bugs.openjdk.java.net/browse/JDK-8081700.
            Platform.runLater(() -> {
                // If no item in the list is selected or the selected item
                // isn't equal to the current input, we refilter the list.
                if (selected == null || !selected.getName().equals(editor.getText())) {
                    filteredItems.setPredicate(item -> {
                        // We return true for any items that starts with the
                        // same letters as the input. We use toUpperCase to
                        // avoid case sensitivity.
                        if (item.getName().toUpperCase().startsWith(newValue.toUpperCase())) {
                            //if the combobox is closed we open it showing the filtered results
                            combo.show();
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
        });

        //fills the combobox with the filtered list
        combo.setItems(filteredItems);
        
        //we need to convert the results to Stations because they come out as strings
        combo.setConverter(new StringConverter<>() {

            @Override
            public String toString(Station station) {
                return station != null ? station.getName() : "";
            }

            @Override
            public Station fromString(String string) {
                return combo.getItems().stream().filter(station ->
                        station.getName().equals(string)).findFirst().orElse(null);
            }
        });
    }
    
    /**
     * Fills the combo box with current stations and filters them by user input
     */
    private void fillSelectArrivalAndDeparture() {
        observableListStations = FXCollections.observableArrayList(stations);
        selectDeparture.getItems().addAll(observableListStations);
        selectArrival.getItems().addAll(observableListStations);
        selectArrival.setEditable(true);
        selectDeparture.setEditable(true);
        
        autoCompleteComboBox(selectArrival);
        autoCompleteComboBox(selectDeparture);  
    }
    
    /**
     * Initializes current user
     * @param user 
     */
    public void getUser(User user) {
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
        
        loadTableTrips();      
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
     * Populates the table with the id,departure and arrival of all the trips from this client
     */
    public void loadTableTrips(){
        
        colIDTrip.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getId() )));
        colDeparture.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getDeparture().getName())));
        colArrival.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getArrival().getName())));
        colDate.setCellValueFactory(date -> new SimpleStringProperty(String.valueOf(date.getValue().getTripDate())));
        tripsList = tripDAO.getTripsClient(client.getId());
        observableListTrip = FXCollections.observableArrayList(tripsList);
        tableTrips.setItems(observableListTrip);
        
    }
    
    /**
     * Calls the view that show all the routes for the trip selected
     * @throws IOException 
     */
    public void showAllRoutes() throws IOException{
        if(allRoutes != null){
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/showAllRoutesView.fxml"));
            Parent root = loader.load();

            ShowAllRoutesViewController controller = loader.getController();
            //sets the routes and the client
            controller.getRoutes(allRoutes);
            controller.setClient(client);

            Stage stage = new Stage();
            Scene scene = new Scene(root);      
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("Rotas");
            stage.showAndWait();
            allRoutes.clear();
        }
    }
    
    /**
     * Validation fields
     * @return boolean
     */
    private boolean validationSelectStations() {
        if ((selectArrival.getSelectionModel().getSelectedItem() != null
                && selectDeparture.getSelectionModel().getSelectedItem() != null) 
                && selectDeparture.getSelectionModel().getSelectedItem() != 
                selectArrival.getSelectionModel().getSelectedItem()) {
            return true;
        }
        return false;
    }
}
