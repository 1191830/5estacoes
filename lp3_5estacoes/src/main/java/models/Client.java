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
public class Client extends User {

    /**
     * Constructor empty
     */
    public Client() {
    }

    /**
     * Constructor complete
     *
     * @param name
     * @param userName
     * @param status
     * @param permission
     * @param hash
     * @param salt
     */
    public Client(String name, String userName, boolean status, int permission, String hash, String salt) {
        super(name, userName, status, permission, hash, salt);
    }

    /**
     * Default to string
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Client" + super.toString();
    }

}
