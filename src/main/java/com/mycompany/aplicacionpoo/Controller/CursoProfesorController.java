/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Controller;

import com.mycompany.aplicacionpoo.Config.ConexionDB;
import com.mycompany.aplicacionpoo.Model.Curso;
import com.mycompany.aplicacionpoo.Model.CursoProfesor;
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
public class CursoProfesorController {
    public ArrayList<CursoProfesor> mostrarCursoProfesor(){
        ArrayList<CursoProfesor> listaCursoProfesor = new ArrayList<>();
        
        String sql = """
                     select cp.anio, cp.semestre, 
                            p.id as id_profesor, pe.nombre as nombre_profesor, 
                            c.id as id_curso, c.nombre as nombre_curso
                         from cursoprofesor cp
                         inner join profesor p on cp.profesor_id = p.id
                         inner join persona pe on p.id = pe.id
                         inner join curso c on cp.curso_id = c.id
                     """;
               
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                CursoProfesor cp = new CursoProfesor();
                cp.setAño(rs.getInt("anio"));
                cp.setSemestre(rs.getInt("semestre"));
                
                Profesor p = new Profesor();
                p.setId(rs.getDouble("id"));
                p.setNombres(rs.getString("nombre"));
                
                Curso c = new Curso();
                c.setId(rs.getInt("id_curso"));
                c.setNombre(rs.getString("nombre_curso"));
                
                cp.setProfesor(p);
                cp.setCurso(c);
                
                listaCursoProfesor.add(cp);
            }
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Inscripción" +  e.getMessage());
        }
        return listaCursoProfesor;
    }
    
    public void agregarCursoProfesor(int año, int semestre, double idProfesor, int idCurso) {
        String sql = "INSERT INTO cursoprofesor (anio, semestre, profesor_id, curso_id) VALUES (?, ?, ?, ?)";
                
                try(Connection conn = ConexionDB.conectar();
                        PreparedStatement stmt = conn.prepareStatement(sql)){
                        
                       
                        stmt.setInt(1, año);
                        stmt.setInt(2, semestre);
                        stmt.setDouble(3, idProfesor);
                        stmt.setInt(4, idCurso);
                        
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Inforomación Agregada exitosamente.");
                    
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar Información: " + ex.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
                }
    }
    
    public void actualizarCursoProfesor(int año, int semestre, double idProfesor, int idCurso){
       String sql = "update cursoprofesor set anio = ?, semestre = ? where profesor_id = ? and curso_id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, año);
                stmt.setInt(2, semestre);
                stmt.setDouble(3, idProfesor);
                stmt.setInt(4, idCurso);

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Información actualizada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe cursos o profesores con esos ID's.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error " + ex);
            }
    }
    
    public void eliminarCursoProfesor(double idProfesor, int idCurso){
        String sql = "delete from cursoprofesor where profesor_id = ? and Curso_id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setDouble(1, idProfesor);
                stmt.setInt(2, idCurso);

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Información eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe Información para eliminiar.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex);
            }  
    }
    
    public CursoProfesor buscarCursoProfesor(double idProfesor, int idCurso){
        String sql = """
                     select i.anio, i.semestre, 
                                             pe.id as id_profesor, pe.nombre as nombre_profesor, 
                                             c.id as id_curso, c.nombre as nombre_curso
                                      from cursoprofesor i
                                      inner join profesor p on i.profesor_id = p.id
                                      inner join persona pe on p.id = pe.id
                                      inner join curso c on i.curso_id = c.id
                                      where i.profesor_id = ? and i.curso_id = ?
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setDouble(1, idProfesor);
                stmt.setInt(2, idCurso);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    CursoProfesor cp = new CursoProfesor();
                    cp.setAño(rs.getInt("anio"));
                    cp.setSemestre(rs.getInt("semestre"));

                    Profesor p = new Profesor();
                    p.setId(rs.getDouble("id"));
                    p.setNombres(rs.getString("nombre_profesor"));

                    Curso c = new Curso();
                    c.setId(rs.getInt("id_curso"));
                    c.setNombre(rs.getString("nombre_curso"));

                    cp.setProfesor(p);
                    cp.setCurso(c);
                    
                    return cp;
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
}
