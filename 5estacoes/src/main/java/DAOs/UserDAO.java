/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAOs;

import database.Database;
import database.DatabaseFactory;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;

/**
 *
 * @author Rui
 */
public class UserDAO {

    //BD Factory that calls our MSSql Database
    private final Database database = DatabaseFactory.getDatabase("Test");
    private final Connection conn = database.conectar();

    /**
     * Gets the user with the username received
     *
     * @param userName
     * @return user
     */
    public User getUser(String userName) {
        User user = new User();
        String sql = "SELECT * FROM PERSON WHERE UserName='" + userName + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                user.setId(rs.getInt("IDPerson"));
                user.setUserName(rs.getString("UserName"));
                user.setName(rs.getString("Name"));
                user.setPermission(rs.getInt("Permission"));
                user.setStatus(rs.getBoolean("Active"));
                user.setHash(rs.getString("HashPassword"));
                user.setSalt(rs.getString("Salt"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    /**
     * Checks if the user exists
     *
     * @param userName
     * @return boolean
     */
    public boolean userExist(String userName) {
        int result = 0;
        String sql = "SELECT COUNT(IDPerson) as 'total' FROM PERSON WHERE UserName='" + userName + "'";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                result = rs.getInt("total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result > 0;
    }
}
