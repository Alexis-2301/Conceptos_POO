/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Model;

import com.mycompany.aplicacionpoo.Config.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author nicol
 */
public class CursoProfesor {
    private Profesor profesor;
    private int año;
    private int semestre;
    private Curso curso;
    private int idProfesor;
    private int idCurso;

    public CursoProfesor(Profesor profesor, int año, int semestre, Curso curso) {
        this.profesor = profesor;
        this.año = año;
        this.semestre = semestre;
        this.curso = curso;
    }

    public CursoProfesor() {
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getIdProfesor() {
        return idProfesor;
    }

    public void setIdProfesor(int idProfesor) {
        this.idProfesor = idProfesor;
    }

    public int getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(int idCurso) {
        this.idCurso = idCurso;
    }
    
    @Override
    public String toString() {
        return String.format(
            "CursoProfesor{profesor='%s %s', año=%d, semestre=%d, curso='%s'}",
            profesor.getNombres(), profesor.getApellidos(),
            año, semestre,
            curso.getNombre()
        );
    }
    
    public void mostrarCursoProfesor(JTable totalCursoProfesor){
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<TableModel>(modelo);
        totalCursoProfesor.setRowSorter(ordenarTabla);
        
        modelo.addColumn("Año");
        modelo.addColumn("Semestre");
        modelo.addColumn("Id");
        modelo.addColumn("Profesor");
        modelo.addColumn("Id");
        modelo.addColumn("Curso");
        
        totalCursoProfesor.setModel(modelo);
        
        String sql = """
                     select cp.anio, cp.semestre, p.id, pe.nombre, c.id, c.nombre
                     from cursoprofesor cp
                     inner join profesor p on cp.profesor_id = p.id
                     inner join persona pe on p.id = pe.id
                     inner join curso c on cp.curso_id = c.id
                     """;
        String[] datos = new String[6];
        
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5); 
                datos[5] = rs.getString(6);
                
                modelo.addRow(datos);
            }
            
            totalCursoProfesor.setModel(modelo);
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Inscripción" +  e.getMessage());
        }
        
    }
    
    public void agregarCursoProfesor(JTextField año, JTextField semestre, JTextField idProfesor, JTextField idCurso) {
        if(año.getText().trim().isEmpty() || semestre.getText().trim().isEmpty() || idProfesor.getText().trim().isEmpty()
                || idCurso.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Los campos no pueden ser vacios ");
        }else{
                setAño(Integer.parseInt(año.getText()));
                setSemestre(Integer.parseInt(semestre.getText()));
                setIdProfesor(Integer.parseInt(idProfesor.getText()));
                setIdCurso(Integer.parseInt(idCurso.getText()));
                
                String sql = "INSERT INTO cursoprofesor (anio, semestre, profesor_id, curso_id) VALUES (?, ?, ?, ?)";
                
                try(Connection conn = ConexionDB.conectar();
                        PreparedStatement stmt = conn.prepareStatement(sql)){
                        
                       
                        stmt.setInt(1, getAño());
                        stmt.setInt(2, getSemestre());
                        stmt.setInt(3, getIdProfesor());
                        stmt.setInt(4, getIdCurso());
                        
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Inforomación Agregada exitosamente.");
                    
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar Información: " + ex.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
                }
        }
    }
    
    public void actualizarCursoProfesor(JTextField año, JTextField semestre, JTextField idProfesor, JTextField idCurso){
        if(año.getText().trim().isEmpty() || semestre.getText().trim().isEmpty()
                || idProfesor.getText().trim().isEmpty()
                || idCurso.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "los campos no pueden ser vacios");
        }else{
            setAño(Integer.parseInt(año.getText()));
            setSemestre(Integer.parseInt(semestre.getText()));
            setIdProfesor(Integer.parseInt(idProfesor.getText()));
            setIdCurso(Integer.parseInt(idCurso.getText()));
            
            String sql = "update cursoprofesor set anio = ?, semestre = ? where profesor_id = ? and curso_id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, getAño());
                stmt.setInt(2, getSemestre());
                stmt.setInt(3, getIdProfesor());
                stmt.setInt(4, getIdCurso());

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
    }
    
    public void eliminarCursoProfesor(JTextField idProfesor, JTextField idCurso){
        if(idProfesor.getText().trim().isEmpty() || idCurso.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID's no pueden estar vacio");
        }else{
            setIdProfesor(Integer.parseInt(idProfesor.getText()));
            setIdCurso(Integer.parseInt(idCurso.getText()));
        
            String sql = "delete from cursoprofesor where profesor_id = ? and Curso_id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setInt(1, getIdProfesor());
                stmt.setInt(2, getIdCurso());

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
    }
    
    public void buscarCursoProfesor(JTextField idProfesor, JTextField idCurso){
        if(idProfesor.getText().trim().isEmpty() || idCurso.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID's no pueden estar vacio");
        }else{
            setIdProfesor(Integer.parseInt(idProfesor.getText()));
            setIdCurso(Integer.parseInt(idCurso.getText()));
            
            String sql = """
                     select i.anio, i.semestre, pe.id, pe.nombre as nombre_profesor, c.nombre as nombre_curso
                     from cursoprofesor i
                     inner join persona pe on i.profesor_id = pe.id
                     inner join curso c on i.curso_id = c.id
                     where i.profesor_id = ? and i.curso_id = ?
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, getIdProfesor());
                stmt.setInt(2, getIdCurso());
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    String programa ="\nAño: " + rs.getString("anio") +
                                 "\nSemestre: " + rs.getString("semestre") +
                                 "\nProfesor: " + rs.getString("nombre_profesor") +
                                 "\nCurso: " + rs.getString("nombre_curso");

                JOptionPane.showMessageDialog(null, programa);
                }else{
                    JOptionPane.showMessageDialog(null, "No se encontró ninguna Información");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar información " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e);
            }
        }
    }
}
