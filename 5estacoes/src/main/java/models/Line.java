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
public class Line {

    private int id;
    private String name;
    private String color;

    /**
     * Getter id
     *
     * @return integer
     */
    public int getId() {
        return id;
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
     * Getter color
     *
     * @return String
     */
    public String getColor() {
        return color;
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
     * Setter name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter color
     *
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Constructor empty
     */
    public Line() {
    }

    /**
     * Constructor complete
     *
     * @param id
     * @param name
     * @param color
     */
    public Line(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    /**
     * Default to string
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Line{" + "id=" + id + ", name=" + name + ", color=" + color + '}';
    }

}
