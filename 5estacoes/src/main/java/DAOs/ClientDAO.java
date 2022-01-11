/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import database.Database;
import database.DatabaseFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Client;
import models.User;

/**
 *
 * @author Rui
 */
public class ClientDAO {

    //BD Factory that calls our MSSql Database
    private final Database database = DatabaseFactory.getDatabase("Test");
    private final Connection conn = database.conectar();
    
    /**
     * Insert client in database
     * @param user
     * @return boolean
     * @throws SQLException 
     */
    public boolean insertClientDB(User user) throws SQLException {
        String sql = "INSERT INTO PERSON(Name, UserName, Permission, "
                + "Active, HashPassword, Salt) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getUserName());
            stmt.setInt(3, User.CLIENT_PERMISSION);
            stmt.setBoolean(4, User.USER_ACTIVE);
            stmt.setString(5, user.getHash());
            stmt.setString(6, user.getSalt());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
    
    /**
     * Get all clients from BD to ArrayList
     * @return 
     */
    public ArrayList<Client> getClients() {
        String sql = "SELECT * FROM PERSON where PERMISSION = 2 AND ACTIVE = 1";
        ArrayList<Client> clients = new ArrayList<>();
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet result = stmt.executeQuery();
            while (result.next()) {
                Client client = new Client();
                client.setId(result.getInt("IDPerson"));
                client.setName(result.getString("Name"));
                client.setUserName(result.getString("UserName"));
                client.setStatus(result.getBoolean("Permission"));
                client.setPermission(result.getInt("Active"));
                clients.add(client);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ClientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return clients;
    }
}
