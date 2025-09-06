/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Controller;

import com.mycompany.aplicacionpoo.Config.ConexionDB;
import com.mycompany.aplicacionpoo.Model.Curso;
import com.mycompany.aplicacionpoo.Model.Programa;
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
public class CursoController {
    
    public ArrayList<Curso>  mostrarCurso(){
        ArrayList<Curso> listaCurso = new ArrayList<>();
        
        String sql = """
                     select c.id, c.nombre, c.activo, p.id as id_programa, p.nombre as nombre_programa
                     from curso c
                     inner join programa p on c.programa_id = p.id;
                     """;
            
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                Curso c = new Curso();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setActivo(rs.getBoolean("activo"));
                
                Programa p = new Programa();
                p.setId(rs.getDouble("id_programa"));
                p.setNombre(rs.getString("nombre_programa"));
                
                c.setPrograma(p);
                listaCurso.add(c);
            }
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Curso" +  e.getMessage());
        }
        return listaCurso;
    }
    
    public void agregarCurso(int id, String nombre, boolean estado, double idPrograma) {
        String sql = "INSERT INTO curso (id, nombre, activo, programa_id) VALUES (?, ?, ?, ?)";
                
                try(Connection conn = ConexionDB.conectar();
                        PreparedStatement stmt = conn.prepareStatement(sql)){
                        
                       
                        stmt.setInt(1, id);
                        stmt.setString(2, nombre);
                        stmt.setBoolean(3, estado);
                        stmt.setDouble(4, idPrograma);
                        
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Curso agregado exitosamente.");
                    
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar Curso: " + ex.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
                }
    }
    
    public void eliminarCurso(int id){
        String sql = "delete from curso where id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setInt(1, id);

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Curso eliminado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ningún Curso con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex);
            }  
    }
    
    public Curso buscarCurso(int id){
        String sql = """
                     SELECT c.id, c.nombre, c.activo, p.id as id_programa, p.nombre as nombre_programa
                     FROM curso c
                     INNER JOIN programa p ON p.id = c.programa_id
                     WHERE c.id = ?;
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    Curso c = new Curso();
                    c.setId(rs.getInt("id"));
                    c.setNombre(rs.getString("nombre"));
                    c.setActivo(rs.getBoolean("activo"));
                
                    Programa p = new Programa();
                    p.setId(rs.getDouble("id_programa"));
                    p.setNombre(rs.getString("nombre_programa"));
                
                    c.setPrograma(p);
                    return c;
                }else{
                    JOptionPane.showMessageDialog(null, "No se encontró ninguna Información");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar información " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e);
            }
        return null;
    }
    
    public void actualizarCurso(int id, String nombre, boolean estado, double idPrograma) {
        String sql = "UPDATE curso SET nombre = ?, activo = ?, programa_id = ? where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                
                stmt.setString(1, nombre);
                stmt.setBoolean(2, estado);
                stmt.setDouble(3, idPrograma);
                stmt.setDouble(4, id);
                
                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Curso actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ningún Curso con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e.toString());
            }
        }
}
