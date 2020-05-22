/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ChatServer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerStatement {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Connection getConnection() {
        final String url = "jdbc:mysql://localhost:3306/user_list";
        final String username = "root";
        final String password = "";

        try {
            Class.forName("com.mysql.jdbc.Driver");

            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServerStatement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ServerStatement.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public boolean loginRecord(String username, String password) {
        ServerStatement serverStatement = new ServerStatement();
        
        serverStatement.setUsername(username);
        serverStatement.setPassword(password);
        
        String passwordEncrypt = Base64.getEncoder().encodeToString((serverStatement.getPassword()).getBytes());

        Connection conn = getConnection();

        String sql = "SELECT * FROM `users` WHERE username = ? AND password = ?"; 

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, serverStatement.getUsername());
            preparedStatement.setString(2, passwordEncrypt);

            ResultSet rs = preparedStatement.executeQuery(); 

            return rs.next(); // true hoặc false
        } catch (SQLException ex) {
            Logger.getLogger(ServerStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public int registerRecord(String username, String password) {
        ServerStatement serverStatement = new ServerStatement();
        serverStatement.setUsername(username); 
        serverStatement.setPassword(password);

        String passwordEncrypt = Base64.getEncoder().encodeToString((serverStatement.getPassword()).getBytes());

        Connection conn = getConnection();

        String sql = "INSERT INTO `users`(username, password) VALUES(?, ?)";
        
        if(serverStatement.checkRegister(serverStatement.getUsername())){
            return 0;
        } else {
            try {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, serverStatement.getUsername());
                preparedStatement.setString(2, passwordEncrypt);

                int rs = preparedStatement.executeUpdate();
                return rs;
            } catch (SQLException ex) {
                Logger.getLogger(ServerStatement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 0;
    }
    
    public boolean checkRegister(String username){
        ServerStatement serverStatement = new ServerStatement();
        serverStatement.setUsername(username);
        
        Connection conn = getConnection();

        String sql = "SELECT * FROM `users` WHERE username = ?"; 

        try {
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, serverStatement.getUsername());

            ResultSet rs = preparedStatement.executeQuery(); 

            return rs.next();
        } catch (SQLException ex) {
            Logger.getLogger(ServerStatement.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
