/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.util.Optional;

/**
 *
 * @author nelso
 */
public class User {

    private String name;
    private String userName;
    private int id;
    private int permission;
    private boolean status;
    private String hash;
    private String salt;

    /**
     * Getter name
     *
     * @return String
     */
    public String getName() {
        return name;
    }
    
    /**
     * Getter userName
     * @return String
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter id
     *
     * @return integer
     */
    public int getId() {
        return id;
    }

    /**
     * Getter status
     *
     * @return boolean
     */
    public boolean isStatus() {
        return status;
    }

    /**
     * Getter permission
     *
     * @return integer
     */
    public int getPermission() {
        return permission;
    }
    
    /**
     * Getter hash
     * @return String
     */
    public String getHash() {
        return hash;
    }
    
    /**
     * Getter salt
     * @return String
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Setter name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Setter userName
     * @param userName
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * Setter id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Setter status
     * @param status
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    /**
     * Setter permission
     * @param permission
     */
    public void setPermission(int permission) {
        this.permission = permission;
    }
    
        /**
     * Setter hash
     * @param hash
     */
    public void setHash(String hash) {
        this.hash = hash;
    }
    
    /**
     * Setter salt
     * @param salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
    }

    /**
     * Constructor empty
     */
    public User() {
    }

    /**
     * Constructor complete
     *
     * @param name
     * @param userName
     * @param id
     * @param status
     * @param permission
     * @param hash
     * @param salt
     */
    public User(String name,String userName, boolean status, int permission, String hash, String salt) {
        this.name = name;
        this.userName = userName;
        this.status = status;
        this.permission = permission;
        this.hash = hash;
        this.salt = salt;
    }

    /**
     * Default to string
     *
     * @return String
     */
    @Override
    public String toString() {
        return "User{" + "name=" + name + ", User Name=" + userName + ", id=" + id + ", permission=" + permission + ", status=" + status + '}';
    }

    public void setSalt(Optional<String> salt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
