/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Controller;

import com.mycompany.aplicacionpoo.Config.ConexionDB;
import com.mycompany.aplicacionpoo.Model.Facultad;
import com.mycompany.aplicacionpoo.Model.Programa;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 *
 * @author nicol
 */
public class ProgramaController {
    public ArrayList<Programa> mostrarPrograma(){
        ArrayList<Programa> listaPrograma = new ArrayList<>();
        String sql = """
                     select p.id, p.nombre, p.duracion, p.registro, f.id as id_facultad, f.nombre as nombre_facultad
                     from programa p
                     inner join facultad f on p.facultad_id = f.id
                     """;
             
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                Programa p = new Programa();
                p.setId(rs.getInt("id"));
                p.setNombre(rs.getString("nombre"));
                p.setDuracion(rs.getInt("duracion"));
                p.setRegistro(rs.getDate("registro"));
                
                Facultad f = new Facultad();
                f.setId(rs.getInt("id_facultad"));
                f.setNombre(rs.getString("nombre_facultad"));
                
                p.setFacultad(f);
                
                listaPrograma.add(p);
            }
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Programa" +  e.getMessage());
        }
        return listaPrograma;
    }
    
    public void guardarPrograma(double id, String nombrePrograma, double duracion, Date registro, double idFacultad) {
        
                String sql = "INSERT INTO programa (id, nombre, duracion, registro, facultad_id) VALUES (?, ?, ?, ?, ?)";
                
                try(Connection conn = ConexionDB.conectar();
                        PreparedStatement stmt = conn.prepareStatement(sql)){
                        
                       
                        stmt.setDouble(1, id);
                        stmt.setString(2, nombrePrograma);
                        stmt.setDouble(3, duracion);
                        stmt.setDate(4, registro);
                        stmt.setDouble(5, idFacultad);
                        
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Programa agregado exitosamente.");
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar Programa: " + ex.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
                }
        }
   
    public void actualizarPrograma(double id, String nombrePrograma, double duracion, Date registro, double idFacultad){
        String sql = "update Programa set nombre = ?, duracion = ?, registro = ?, facultad_id = ? where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, nombrePrograma);
                stmt.setDouble(2, duracion);
                stmt.setDate(3, registro);
                stmt.setDouble(4, idFacultad);
                stmt.setDouble(5, id);

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Programa actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe Programa con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error " + ex);
            }
    }
    
    public void eliminarPrograma(double id){
        String sql = "delete from programa where id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setDouble(1, id);

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Programa eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ningún Programa con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex);
            }  
    }
    
    public Programa buscarPrograma(double id){
        String sql = """
                     SELECT p.id, p.nombre, p.duracion, p.registro, f.nombre as nombre_facultad, f.id as id_facultad
                     FROM programa p
                     INNER JOIN facultad f ON p.facultad_id = f.id
                     WHERE p.id = ?;
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setDouble(1, id);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    Programa p = new Programa();
                    p.setId(rs.getDouble("id"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDuracion(rs.getDouble("duracion"));
                    p.setRegistro(rs.getDate("registro"));
                    
                    Facultad f = new Facultad();
                    f.setId(rs.getDouble("id_facultad"));
                    f.setNombre(rs.getString("nombre_facultad"));
                    
                    p.setFacultad(f);
                    
                    return p;
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar información " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e);
            }
        return null;
        }
}
