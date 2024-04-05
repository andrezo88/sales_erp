package com.mycompany.sales_system.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {
    
    final static private String DRIVER = "org.firebirdsql.jdbc.FBDriver";
    final static String URL = "jdbc:firebirdsql://localhost//home/aabreu/Documents/projects/pessoal/database/sales.fdb";
    final static String USER = "andre";
    final static String PASSWORD = "Aa134625@@";
    public static Connection connection = null;
     
    
    public static Connection CONNECT_DATABASE() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("banco connectado");
        } catch (ClassNotFoundException ex) {
            System.out.println("Classe não encontrada, adicione o driver na biblioteca");
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            System.out.println(ex);
        }     
        return connection;
    }
}
