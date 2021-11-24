/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.ArrayList;

/**
 *
 * @author pcoelho
 */
public class StationLine {
    
    private String nameOfStation;
    private ArrayList<Character> lines;

    
    /**
     * Constructor complete
     * @param nameOfStation
     * @param lines
     */
    public StationLine(String nameOfStation, ArrayList<Character> lines) {
        this.nameOfStation = nameOfStation;
        this.lines = lines;
    }
    
    
     /**
     * Constructor empty
     */
    public StationLine() {
    }
    
    
    
    /**
     * Getter nameOfStation
     *
     * @return String
     */
    public String getNameOfStation() {
        return nameOfStation;
    }
/**
     * Getter lines
     *
     * @return ArrayList_Character
     */
    

    public ArrayList<Character> getLines() {
        return lines;
    }

    /**
     * Setter nameOfStation
     *
     * @param nameOfStation
     */
    public void setNameOfStation(String nameOfStation) {
        this.nameOfStation = nameOfStation;
    }
      
    
    /**
     * Setter lines
     *
     * @param lines
     */
    public void setLines(ArrayList<Character> lines) {
        this.lines = lines;
    }

    
    /**
     * Default to string
     *
     * @return String
     */
    @Override
    public String toString() {
        return "StationLine{" + "nameOfStation=" + nameOfStation + ", lines=" + lines + '}';
    }
    
    
    
    
}
