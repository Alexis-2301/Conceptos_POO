/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Controller;

import com.mycompany.aplicacionpoo.Config.ConexionDB;
import com.mycompany.aplicacionpoo.Model.Profesor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author nicol
 */
public class ProfesorController {
    
     public void agregarProfesor(int id, String tipoContrato) {
        
            String validarSql = "SELECT tipo FROM persona WHERE id = ?";
            String sql = "INSERT INTO profesor (id, tipo_contrato) VALUES (?, ?)";

            try (Connection conn = ConexionDB.conectar()) {
                try (PreparedStatement stmtValidar = conn.prepareStatement(validarSql)) {
                    stmtValidar.setInt(1, id);
                    try (ResultSet rs = stmtValidar.executeQuery()) {
                        if (rs.next()) {
                            String tipo = rs.getString("tipo");

                            if (!"profesor".equalsIgnoreCase(tipo)) {
                                JOptionPane.showMessageDialog(null, 
                                    "El ID corresponde a una persona tipo " + tipo + 
                                    ", no puede agregarse como profesor.");
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, 
                                "No existe ninguna persona con ese ID.");
                            return;
                        }
                    }
                }


                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, id);
                    stmt.setString(2, tipoContrato);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Profesor agregado exitosamente.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al agregar profesor: " + ex.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
            }
        }

    
    public void actualizarProfesor(int id, String tipoContrato){
            String sql = "update profesor set tipo_contrato = ? where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, tipoContrato);
                stmt.setInt(2, id);


                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Profesor actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ningún profesor con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error " + ex);
            }
    }
    
    public ArrayList<Profesor> mostrarProfesor(){
        ArrayList<Profesor> listaProfesor = new ArrayList<>();
        
        String sql = """
                     select p.id, p.nombre, p.apellido, p.correo, pr.tipo_contrato
                     from persona p
                     inner join profesor pr on p.id = pr.id;
                     """;
           
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                Profesor p = new Profesor();
                p.setId(rs.getInt("id"));
                p.setNombres(rs.getString("nombre"));
                p.setApellidos(rs.getString("apellido"));
                p.setEmail(rs.getString("correo"));
                p.setTipoContrato(rs.getString("tipo_contrato"));
                
                listaProfesor.add(p);     
            }
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar profesor" +  e.getMessage());
        }
        return listaProfesor;
    }
    
    public void eliminarProfesor(int id){
        String sql = "delete from profesor where id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) id);

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Profesor eliminado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ningún profesor con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex);
            }
        
    }
    
    public Profesor buscarProfesor(int id){
        String sql = """
                     SELECT p.id, p.nombre, p.apellido, p.correo, p.tipo, pr.tipo_contrato
                     FROM persona p
                     INNER JOIN profesor pr ON p.id = pr.id
                     WHERE p.id = ?;
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    Profesor p = new Profesor();
                    
                    p.setId(rs.getInt("id"));
                    p.setNombres(rs.getString("nombre"));
                    p.setApellidos(rs.getString("apellido"));
                    p.setEmail(rs.getString("correo"));
                    p.setTipoContrato(rs.getString("tipo_contrato"));
                    
                    return p;
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar Persona " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e);
            }
        return null;
    }
}
