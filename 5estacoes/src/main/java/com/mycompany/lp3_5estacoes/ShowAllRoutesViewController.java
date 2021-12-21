/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lp3_5estacoes;

import DAOs.RouteDAO;
import DAOs.TripDAO;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.Client;
import models.Route;
import utils.Alerts;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class ShowAllRoutesViewController implements Initializable {

    @FXML
    private TableView<Route> tableRoutes;
    @FXML
    private TableColumn<Route, String> colStations;
    @FXML
    private TableColumn<Route, String> colPrice;
    @FXML
    private TableColumn<Route, String> colQtt;
    @FXML
    private TableColumn<Route, String> colDuration;
    @FXML
    private TableColumn<Route, String> colChangeOfLine;
    @FXML
    private Button plan;
    @FXML
    private Button exit;

    RouteDAO routeDAO = new RouteDAO();
    TripDAO tripDAO = new TripDAO();

    Client client;
    Route route;

    ObservableList<Route> observableListRoutes;

    /**
     * Fills the table with all the routes in the routes ArrayList
     * @param routes 
     */
    public void getRoutes(ArrayList<Route> routes) {
        //Format the price so that it has 2 decimals max
        DecimalFormat df = new DecimalFormat("#.##");

        colStations.setCellValueFactory(date -> new SimpleStringProperty(
                String.valueOf(date.getValue().toStringRouteStations())));
        colPrice.setCellValueFactory(date -> new SimpleStringProperty(
                String.valueOf(df.format(date.getValue().getPrice()) + " €")));
        colQtt.setCellValueFactory(date -> new SimpleStringProperty(
                String.valueOf(date.getValue().getNumberOfStations())));
        colDuration.setCellValueFactory(date -> new SimpleStringProperty(
                String.valueOf(date.getValue().getDuration())));
        colChangeOfLine.setCellValueFactory(date -> new SimpleStringProperty(
                String.valueOf(date.getValue().getChangesOfLine())));
        
        //sets a fixed witdh to the tables so that they stay regular when user maximizes window
        colStations.prefWidthProperty()
                .bind(tableRoutes.widthProperty().divide(1.18));
        colPrice.prefWidthProperty()
                .bind(tableRoutes.widthProperty().divide(31));
        colQtt.prefWidthProperty()
                .bind(tableRoutes.widthProperty().divide(38));
        colDuration.prefWidthProperty()
                .bind(tableRoutes.widthProperty().divide(20));
        colChangeOfLine.prefWidthProperty()
                .bind(tableRoutes.widthProperty().divide(24));

        observableListRoutes = FXCollections.observableArrayList(routes);
        tableRoutes.setItems(observableListRoutes);
    }

    /**
     * Initializes current client
     * @param client 
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    /**
     * Sets the route of the selected trip
     * @param route 
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableRoutes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> setRoute(newValue));
    }

    /**
     * Inserts the selected route in the database for the current client
     * @param route
     * @throws SQLException 
     */
    public void insertIntoDB(Route route) throws SQLException {
        Alerts alert = new Alerts();
        boolean add = alert.showConfirmation("Rota escolhida", "Deseja adicionar esta rota");

        if (add) {
            tripDAO.insertTripClient(route, client);
        }
    }

    @FXML
    private void btnPlan(ActionEvent event) throws SQLException {
        insertIntoDB(this.route);
    }

    @FXML
    private void btnExit(ActionEvent event) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();

    }

}
