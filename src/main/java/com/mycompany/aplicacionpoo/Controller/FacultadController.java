/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Controller;

import com.mycompany.aplicacionpoo.Config.ConexionDB;
import com.mycompany.aplicacionpoo.Model.Facultad;
import com.mycompany.aplicacionpoo.Model.Persona;
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
public class FacultadController {
    
    public ArrayList<Facultad> mostrarFacultad(){
        ArrayList<Facultad> listaFacultad = new ArrayList<>();
        
        String sql = """
                     select f.id, f.nombre, p.id as id_decano, p.nombre as nombre_decano
                     from persona p
                     inner join Facultad f on p.id = f.decano_id;
                     """;
            
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
               Facultad f = new Facultad();
                f.setId(rs.getInt("id"));
                f.setNombre(rs.getString("nombre"));
                
                Persona decano = new Persona();
                decano.setId(rs.getInt("id_decano"));
                decano.setNombres(rs.getString("nombre_decano"));
                
                f.setDecano(decano);
                
                listaFacultad.add(f);
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Facultad" +  e.getMessage());
        }
        return listaFacultad;
    }
    
    public void agregarFacultad(int id, String nombreFacultad, int idDecano) {
            String validarSql = "SELECT tipo FROM persona WHERE id = ?";
            String sql = "INSERT INTO facultad (id, nombre, decano_id) VALUES (?, ?, ?)";

            try (Connection conn = ConexionDB.conectar()) {
                try (PreparedStatement stmtValidar = conn.prepareStatement(validarSql)) {
                    stmtValidar.setInt(1, idDecano);
                    try (ResultSet rs = stmtValidar.executeQuery()) {
                        if (rs.next()) {
                            String tipo = rs.getString("tipo");

                            if (!"profesor".equalsIgnoreCase(tipo)) {
                                JOptionPane.showMessageDialog(null, 
                                    "El ID corresponde a una persona tipo " + tipo + 
                                    ", no puede agregarse como decano.");
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, 
                                "No existe ninguna Persona con ese ID.");
                            return;
                        }
                    }
                }


                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, (int) id);
                    stmt.setString(2, nombreFacultad);
                    stmt.setInt(3, (int) idDecano);
                    
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Facultad agregada exitosamente.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al agregar Facultad: " + ex.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
                System.out.println(e);
            }
    }
    
    public void actualizarFacultad(int id, String nombreFacultad, int idDecano){
        String sql = "update Facultad set decano_id = ?, nombre = ? where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) idDecano);
                stmt.setString(2, nombreFacultad);
                stmt.setInt(3, (int) id);


                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Facultad actualizada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe Facultad con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error " + ex);
            }
    }
    
    public void eliminarFacultad(int id){
        String sql = "delete from facultad where id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setInt(1, id);

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Facultad eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ninguna Facultad con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex);
            } 
    }
    
    public Facultad buscarFacultad(int id){
        String sql = """
                     SELECT p.id, p.nombre, p.apellido, p.correo, f.nombre as nombre_facultad, f.id as id_facultad
                     FROM persona p
                     INNER JOIN facultad f ON p.id = f.decano_id
                     WHERE f.id = ?;
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    Facultad f = new Facultad();
                    
                    f.setId(rs.getInt("id_facultad"));
                    f.setNombre(rs.getString("nombre_facultad"));

                    Persona decano = new Persona();
                    decano.setId(rs.getInt("id"));
                    decano.setNombres(rs.getString("nombre"));
                    decano.setApellidos(rs.getString("apellido"));
                    decano.setEmail(rs.getString("correo"));
                    
                    f.setDecano(decano);
                    
                    return f;
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar información " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e);
            }
        return null;
    }
}
