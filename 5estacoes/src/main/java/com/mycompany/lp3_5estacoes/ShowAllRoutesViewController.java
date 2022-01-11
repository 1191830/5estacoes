/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.lp3_5estacoes;

import BLL.PaymentBLL;
import DAOs.MachineChangeDAO;
import DAOs.RouteDAO;
import DAOs.TripDAO;
import java.math.BigDecimal;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Client;
import models.Route;
import utils.Alerts;
import utils.Parser;

/**
 * FXML Controller class
 *
 * @author Rui
 */
public class ShowAllRoutesViewController implements Initializable {

    @FXML
    public AnchorPane anchorRoutes;
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

    @FXML
    private Label lbEfetueoPagamento;
    @FXML
    private Label lbTotal;
    @FXML
    public AnchorPane anchorPayment;
    @FXML
    private Label lbPreco;
    @FXML
    private AnchorPane anchorChange;

    RouteDAO routeDAO = new RouteDAO();
    TripDAO tripDAO = new TripDAO();

    Client client;
    Route route;
    Alert a = new Alert(Alert.AlertType.NONE);

    ObservableList<Route> observableListRoutes;
    static ArrayList<Integer> changeCoins = new ArrayList<>();
    @FXML
    private Button confirm;
    @FXML
    private Label lblChange;
    @FXML
    private Label lblTwoEuroChange;
    @FXML
    private Label lblOneEuroChange;
    @FXML
    private Label lblFiftyCentChange;
    @FXML
    private Label lblTwentyCentChange;
    @FXML
    private Label lblTenCentChange;
    @FXML
    private Label lblFiveCentChange;
    @FXML
    private Button btntwoEuro;
    @FXML
    private Button btntenEuro;
    @FXML
    private Button btnfiveEuro;
    @FXML
    private Button tbnoneEuro;
    @FXML
    private Button btnfifitCent;
    @FXML
    private Button btntwentyCent;
    @FXML
    private Button btntenCent;
    @FXML
    private Button btnfiveCent;
    @FXML
    private Label lblPriceTtl;
    @FXML
    private Label lblPrice;

    private BigDecimal totalBtn = new BigDecimal(0);
    private String totalDue;
    
    static MachineChangeDAO machineChangeDAO = new MachineChangeDAO();
    @FXML
    private Button clean;
    
    private final double TENEURO = 10;
    private final double FIVEEURO = 5;
    private final double TWOERURO = 2;
    private final double ONEEURO = 1;
    private final double FIFTYCENT = 0.5;
    private final double TWENTYCENT = 0.2;
    private final double TENCENT = 0.1;
    private final double FIVECENT = 0.05;
    @FXML
    private Button back;
    

    /**
     * Fills the table with all the routes in the routes ArrayList
     *
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
        anchorPayment.setVisible(false);
        anchorChange.setVisible(false);
    }

    /**
     * Initializes current client
     *
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Sets the route of the selected trip
     *
     * @param route
     */
    public void setRoute(Route route) {
        this.route = route;
    }

    /**
     * Initializes the controller class.
     *
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
     *
     * @param route
     * @throws SQLException
     */
    public boolean insertIntoDB(Route route) throws SQLException {
        Alerts alert = new Alerts();
        boolean add = alert.showConfirmation("Rota escolhida", "Deseja adicionar esta rota");

        if (add) {
            tripDAO.insertTripClient(route, client);
            return true;
        }
        return false;
    }

    @FXML
    private void btnPlan(ActionEvent event) throws SQLException {
        //insertIntoDB(this.route);
        anchorPayment.setVisible(true);
        anchorChange.setVisible(false);
        anchorRoutes.setVisible(false);
        
        totalDue = Parser.format(route.getPrice());
        route.setPrice(Parser.round(route.getPrice(), 2));
        lblPrice.setText(totalDue);
    }

    @FXML
    private void btnConfirm(ActionEvent event) throws SQLException {

        if (PaymentBLL.countChange(totalBtn.doubleValue() - route.getPrice())) {
            boolean add = insertIntoDB(route);
            if (add) {
                changeCoins = PaymentBLL.getCoins();
                anchorChange.setVisible(true);
                anchorPayment.setVisible(false);
                fillChangeLabels();
                machineMoney();
            }

        } else {
            Alerts alert = new Alerts();
            alert.showInformation("Não foi possivel satisfazer o troco");
        }

    }
    
    public static void machineMoney(){
        ArrayList<Integer> machine = new ArrayList<>();
        machine = machineChangeDAO.getMachineMoney();
        
        for (int i = 0; i < changeCoins.size(); i++) {
            machine.set(i, (machine.get(i) - changeCoins.get(i)));
        }
        machineChangeDAO.chargeMachine(machine);
    }

    @FXML
    private void btnExit(ActionEvent event) {
        exit();

    }

    public void fillChangeLabels() {

        lblTwoEuroChange.setText("" + changeCoins.get(0));
        lblOneEuroChange.setText("" + changeCoins.get(1));
        lblFiftyCentChange.setText("" + changeCoins.get(2));
        lblTwentyCentChange.setText("" + changeCoins.get(3));
        lblTenCentChange.setText("" + changeCoins.get(4));
        lblFiveCentChange.setText("" + changeCoins.get(5));

        calculateChangeTotal();
    }

    public void calculateChangeTotal() {
        //this array will get all the values to be sum
        Double[] total = new Double[6];
        double totalMachineValue = 0;

        //total of coins * their value
        total[0] = Double.valueOf(Integer.parseInt(lblTwoEuroChange.getText()) * 2);
        total[1] = Double.valueOf(Integer.parseInt(lblOneEuroChange.getText()));
        total[2] = Integer.parseInt(lblFiftyCentChange.getText()) * 0.5;
        total[3] = Integer.parseInt(lblTwentyCentChange.getText()) * 0.2;
        total[4] = Integer.parseInt(lblTenCentChange.getText()) * 0.1;
        total[5] = Integer.parseInt(lblFiveCentChange.getText()) * 0.05;

        //runs the array to sum all the values
        for (int i = 0; i < total.length; i++) {
            totalMachineValue += total[i];
        }

        //this will format the value to only have 2 decimal places
        String totalValue = Parser.format(totalMachineValue);
        lblChange.setText(totalValue + "€");
    }

    @FXML
    private void btnBackChange(ActionEvent event) {
        exit();
    }

    public void exit() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleBtn(ActionEvent event) {
        if (event.getSource() == btntenEuro) {
            totalBtn = totalBtn.add(BigDecimal.valueOf(TENEURO));
        } else if (event.getSource() == btnfiveEuro) {
            totalBtn = totalBtn.add(BigDecimal.valueOf(FIVEEURO));
        } else if (event.getSource() == btntwoEuro) {
            totalBtn = totalBtn.add(BigDecimal.valueOf(TWOERURO));
        } else if (event.getSource() == tbnoneEuro) {
            totalBtn = totalBtn.add(BigDecimal.valueOf(ONEEURO));
        } else if (event.getSource() == btnfifitCent) {
            totalBtn = totalBtn.add(BigDecimal.valueOf(FIFTYCENT));
        } else if (event.getSource() == btntwentyCent) {
            totalBtn = totalBtn.add(BigDecimal.valueOf(TWENTYCENT));
        } else if (event.getSource() == btntenCent) {
            totalBtn = totalBtn.add(BigDecimal.valueOf(TENCENT));
        } else if (event.getSource() == btnfiveCent) {
            totalBtn = totalBtn.add(BigDecimal.valueOf(FIVECENT));
        }
        lblPriceTtl.setText(totalBtn.toString());
        calculo();
    }

    public void calculo() {
        if (totalBtn.doubleValue() >= route.getPrice()) {
            confirm.setDisable(false);
            disableCoins(true);
        }
    }

    @FXML
    private void btnClean(ActionEvent event) {
        clean();
    }
    
    public void disableCoins(boolean state){
        btnfiveCent.setDisable(state);
        btntenCent.setDisable(state);
        btntwentyCent.setDisable(state);
        btnfifitCent.setDisable(state);
        tbnoneEuro.setDisable(state);
        btntwoEuro.setDisable(state);
        btnfiveEuro.setDisable(state);
        btntenEuro.setDisable(state);
    }

    @FXML
    private void btnBack(ActionEvent event) {
        anchorPayment.setVisible(false);
        anchorRoutes.setVisible(true);
        clean();
    }
    
    public void clean(){
        confirm.setDisable(true);
        totalBtn = new BigDecimal(0);
        lblPriceTtl.setText("" + totalBtn);
        disableCoins(false);
    }
}
