package com.mycompany.sales_system.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConnectionFactory {

    final  private String DRIVER = "org.firebirdsql.jdbc.FBDriver";
    final static String URL = "jdbc:firebirdsql://localhost//home/aabreu/Documents/projects/pessoal/database/sales.fdb";
    final static String USER = "andre";
    final static String PASSWORD = "Aa134625@@";
    private Connection connection = null;
    private static ConnectionFactory instancy;

    private ConnectionFactory() {

        try {
            Class.forName(DRIVER);
            System.out.println("driver ok!");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver não encontrado.");
        }

        try {
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("banco connectado");
        } catch(SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    public static ConnectionFactory getInstancy() {
        instancy = null;
        if (instancy == null) {
           instancy = new ConnectionFactory();
        }
        return instancy;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
