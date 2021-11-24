/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

/**
 *
 * @author nelso
 */
public class Station {

    private int id;
    private int position;
    private String name;
    private char keyOfLine;   
   
    /**
     * Getter id
     *
     * @return Integer
     */
    public int getId() {
        return id;
    }

    /**
     * Getter position
     *
     * @return Integer
     */
    public int getPosition() {
        return position;
    }

    /**
     * Getter name
     *
     * @return String
     */
    public String getName() {
        return name;
    }
    
    
    /**
     * Getter keyOfLine
     *
     * @return char
     */
    public char getKeyOfLine() {
        return keyOfLine;
    }

    /**
     * Setter id
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter position
     *
     * @param position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * Setter name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Setter keyOfLine
     *
     * @param keyOfLine
     */
    public void setKeyOfLine(char keyOfLine) {
        this.keyOfLine = keyOfLine;
    }

    /**
     * Constructor empty
     */
    public Station() {
    }

    /**
     * Constructor complete
     *
     * @param id
     * @param position
     * @param name
     * @param keyOfLine
     */
    public Station(int id, int position, String name, char keyOfLine) {
        this.id = id;
        this.position = position;
        this.name = name;
        this.keyOfLine = keyOfLine;
    }
    
    
    /**
     * Default to string
     *
     * @return String
     */
    
    @Override
    public String toString() {
        return "Station{" + "id=" + id + ", position=" + position + ", name=" + name + ", keyOfLine=" + keyOfLine + '}';
    }


    

}
