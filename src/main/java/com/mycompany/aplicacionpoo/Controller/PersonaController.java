package com.mycompany.aplicacionpoo.Controller;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


import com.mycompany.aplicacionpoo.Config.ConexionDB;
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
public class PersonaController {
    
    public void guardarPersona(int id, String nombre, String apellido, String correo, String tipo){
            String sql = "INSERT INTO persona (nombre, apellido, correo, tipo, id) values (?, ?, ?, ?, ?)";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, correo);
                stmt.setString(4, tipo);
                stmt.setInt(5, (int) id);

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Agregado correctamente");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al agregar persona " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e.toString());
            }
    }
    
    public void eliminarPersona(int id) {
        
            String sql = "Delete from persona where id = ? ";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, id);
                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Persona eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ninguna Persona con ese ID.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al eliminar " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error inesperado " + e.toString());
            }
        }
    
    
    public void actualizarPersona(int id, String nombre, String apellido, String correo, String tipo){
            String sql = "UPDATE persona SET nombre = ?, apellido = ?, correo = ?, tipo = ? where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, nombre);
                stmt.setString(2, apellido);
                stmt.setString(3, correo);
                stmt.setString(4, tipo);
                stmt.setInt(5, (int) id);
                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Persona actualizada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ninguna persona con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e.toString());
            }
    }
    
    public ArrayList<Persona> mostrarPersona(){
        ArrayList<Persona> listapersona = new ArrayList<>();
        String sql = "Select * from persona";
        try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                Persona p = new Persona();
                p.setId(rs.getInt("id"));
                p.setNombres(rs.getString("nombre"));
                p.setApellidos(rs.getString("apellido"));
                p.setEmail(rs.getString("correo"));
                p.setTipo(rs.getString("tipo"));
                
                listapersona.add(p);
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar personas" +  e.getMessage());
        }
        return listapersona;
    }
    
    public Persona buscarPersona(int id){
        
            String sql = "Select * from persona where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) id);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    Persona p = new Persona();
                    p.setId(rs.getInt("id"));
                    p.setNombres(rs.getString("nombre"));
                    p.setApellidos(rs.getString("apellido"));
                    p.setEmail(rs.getString("correo"));
                    p.setTipo(rs.getString("tipo"));
                    
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
