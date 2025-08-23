/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author nicol
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    
    public static Connection conectar() {
        
        String url = "jdbc:h2:./data/testdb;AUTO_SERVER=TRUE";  
        String user = "sa";
        String password = "";
        
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa");
            return conn;
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo conectar a la base de datos: " + e.getMessage(), e);
        }
    }
}


