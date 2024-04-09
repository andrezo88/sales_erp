package com.mycompany.sales_system.factory;

import com.mycompany.sales_system.env.Env;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class ConnectionFactory {


    private Connection connection;
    private static ConnectionFactory instancy;

    private ConnectionFactory() {

        try {
            Class.forName(Env.DRIVER);
            System.out.println("driver ok!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver não encontrado.");
        }

        try {
            this.connection = DriverManager.getConnection(Env.URL, Env.USER, Env.PASSWORD);
            System.out.println("banco connectado");
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public static ConnectionFactory getInstancy() {
        if (instancy == null) {
           instancy = new ConnectionFactory();
        }
        return instancy;
    }

    public Connection getConnection() {
        return this.connection;
    }
    
    public void closeConnection() {
        try {
            if(!this.connection.isClosed()){
                this.connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
