/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BLL;

import DAOs.MachineChangeDAO;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.control.Alert;

/**
 *
 * @author Rui
 */
public class PaymentBLL {

    static MachineChangeDAO machineChangeDAO = new MachineChangeDAO();
    static ArrayList<Integer> machine = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
    static ArrayList<Integer> coins;
    Alert a = new Alert(Alert.AlertType.NONE);

    public static ArrayList<Integer> getCoins() {
        return coins;
    }

    public static ArrayList<Integer> getMachine() {
        return machine;
    }

    public static boolean countChange(double changeDue) {

        int change = (int) (Math.round(changeDue * 100));
        int doisEuros = Math.round((int) change / 200);
        change = change % 200;
        int umEuro = Math.round((int) change / 100);
        change = change % 100;
        int cinquentaCentimos = Math.round((int) change / 50);
        change = change % 50;
        int vinteCentimos = Math.round((int) change / 20);
        change = change % 20;
        int dezCentimos = Math.round((int) change / 10);
        change = change % 10;
        int cincoCentimos = Math.round((int) change / 5);
        change = change % 5;
        coins = new ArrayList<>(Arrays.asList(doisEuros, umEuro, cinquentaCentimos, vinteCentimos, dezCentimos, cincoCentimos));
        if (change > 0 || !checkMachineMoney(coins)) {
            return false;
        }
        String result = changeReturn(coins, changeDue);
        return true;
    }

    public static boolean checkMachineMoney(ArrayList<Integer> coins) {
        machine = machineChangeDAO.getMachineMoney();

        for (int i = 0; i < machine.size(); i++) {
            machine.set(i, (machine.get(i) - coins.get(i)));
            if (machine.get(i) < 0) {
                return false;
            }

        }

        return true;
    }

    public static String changeReturn(ArrayList<Integer> coins, double changeDue) {
        int index = 0;
        StringBuilder result = new StringBuilder();
        ArrayList<String> names = new ArrayList<>(Arrays.asList("2€", "1€", "0.50 €", "0.20 €", "0.10 €", "0.05 €"));
        result.append("O troco é: " + changeDue + " €\n");
        for (int i = 0; i < coins.size(); i++) {
            if (coins.get(i) > 0) {
                result.append(names.get(i) + " = " + coins.get(index) + "\n");
            }
            index++;

        }

        return result.toString();
    }

}
