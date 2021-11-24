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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;

/**
 *
 * @author Rui
 */
public class ClientDAO {
    
    //BD Factory that calls our MSSql Database
    private final Database database = DatabaseFactory.getDatabase("SQLServer");
    private final Connection conn = database.conectar();
    
    
      public boolean insertClientDB(User u) throws SQLException {

        String sql = "INSERT INTO PERSON(Name, UserName, Permission, "
                + "Active, HashPassword, Salt) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, u.getName());
            stmt.setString(2, u.getUserName());
            stmt.setInt(3, 2);
            stmt.setBoolean(4, true);
            stmt.setString(5, u.getHash());
            stmt.setString(6, u.getSalt());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
