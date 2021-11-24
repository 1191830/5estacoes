package utils;

import javafx.scene.control.Alert;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rui
 */
public class Alerts {

    Alert a = new Alert(Alert.AlertType.NONE);

    public void showError(String message) {
        a.setAlertType(Alert.AlertType.ERROR);
        a.setHeaderText("Erro");
        a.setContentText(message);
        a.show();
    }

    public void showInformation(String message) {
        a.setAlertType(Alert.AlertType.INFORMATION);
        a.setHeaderText("Informação");
        a.setContentText(message);
        a.show();
    }

}
