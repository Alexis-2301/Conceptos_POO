/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Controller;

import com.mycompany.aplicacionpoo.Config.ConexionDB;
import com.mycompany.aplicacionpoo.Model.Curso;
import com.mycompany.aplicacionpoo.Model.Estudiante;
import com.mycompany.aplicacionpoo.Model.Inscripcion;
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
public class InscripcionController {
    public ArrayList<Inscripcion> mostrarInscripcion(){
        ArrayList<Inscripcion> listaInscripcion = new ArrayList<>();
        
        String sql = """
                     select i.anio, i.semestre, e.id as id_estudiante, p.nombre as nombre_estudiante, c.id as id_curso, c.nombre as nombre_curso
                     from inscripcion i
                     inner join estudiante e on i.estudiante_id = e.id
                     inner join persona p on p.id = e.id
                     inner join curso c on i.curso_id = c.id
                     """;    
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                Inscripcion i = new Inscripcion();
                i.setAño(rs.getInt("anio"));
                i.setSemestre(rs.getInt("semestre"));
                
                Estudiante e = new Estudiante();
                e.setId(rs.getDouble("id_estudiante"));
                e.setNombres(rs.getString("nombre_estudiante"));
                
                Curso c = new Curso();
                c.setId(rs.getInt("id_curso"));
                c.setNombre(rs.getString("nombre_curso"));
                
                i.setEstudiante(e);
                i.setCurso(c);
                listaInscripcion.add(i);
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Inscripción" +  e.getMessage());
        }
        return listaInscripcion;
        
    }
    
    public void agregarInscripcion(int año, int semestre, double idEstudiante, int idCurso) {
        String sql = "INSERT INTO inscripcion (anio, semestre, estudiante_id, curso_id) VALUES (?, ?, ?, ?)";
                
                try(Connection conn = ConexionDB.conectar();
                        PreparedStatement stmt = conn.prepareStatement(sql)){

                        stmt.setInt(1, año);
                        stmt.setInt(2, semestre);
                        stmt.setDouble(3, idEstudiante);
                        stmt.setInt(4, idCurso);
                        
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Inscripción agregada exitosamente.");
                    
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar Inscripción: " + ex.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
                }
        }
    
    public void actualizarInscripcion(int año, int semestre, double idEstudiante, int idCurso){
        String sql = "update inscripcion set anio = ?, semestre = ? where estudiante_id = ? and curso_id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, año);
                stmt.setInt(2, semestre);
                stmt.setDouble(3, idEstudiante);
                stmt.setInt(4, idCurso);

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
    
    public void eliminarInscripcion(double idEstudiante, int idCurso){
        String sql = "delete from inscripcion where Estudiante_id = ? and Curso_id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setDouble(1, idEstudiante);
                stmt.setInt(2, idCurso);

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Inscipción eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ninguna Inscripción.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex);
            } 
    }
    
    public Inscripcion buscarInscripcion(double idEstudiante, int idCurso){
        String sql = """
                     select i.anio, i.semestre, 
                            p.id as id_estudiante, p.nombre as nombre_estudiante, 
                            c.id as id_curso, c.nombre as nombre_curso
                     from inscripcion i
                     inner join persona p on i.estudiante_id = p.id
                     inner join curso c on i.curso_id = c.id
                     where i.estudiante_id = ? and i.curso_id = ?
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setDouble(1, idEstudiante);
                stmt.setInt(2, idCurso);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    Inscripcion i = new Inscripcion();
                    i.setAño(rs.getInt("anio"));
                    i.setSemestre(rs.getInt("semestre"));

                    Estudiante e = new Estudiante();
                    e.setId(rs.getDouble("id_estudiante"));
                    e.setNombres(rs.getString("nombre_estudiante"));

                    Curso c = new Curso();
                    c.setId(rs.getInt("id_curso"));
                    c.setNombre(rs.getString("nombre_curso"));

                    i.setEstudiante(e);
                    i.setCurso(c);
                    return i;
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
