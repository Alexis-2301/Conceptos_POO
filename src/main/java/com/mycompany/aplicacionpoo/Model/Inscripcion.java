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
public class Inscripcion {
    private Curso curso;
    private int año;
    private int semestre;
    private Estudiante estudiante;
    private int idEstudiante;
    private int idCurso;

    public Inscripcion(Curso curso, int año, int semestre, Estudiante estudiante) {
        this.curso = curso;
        this.año = año;
        this.semestre = semestre;
        this.estudiante = estudiante;
    }

    public Inscripcion() {
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
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

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public int getIdEstudiante() {
        return idEstudiante;
    }

    public void setIdEstudiante(int idEstudiante) {
        this.idEstudiante = idEstudiante;
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
            "Inscripcion{curso='%s', año=%d, semestre=%d, estudiante='%s (%s)'}",
            curso.getNombre(), año, semestre,
            estudiante.getNombres() + " " + estudiante.getApellidos(),
            estudiante.getCodigo()
        );
    }
    
    public void mostrarInscripcion(JTable totalPrograma){
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<TableModel>(modelo);
        totalPrograma.setRowSorter(ordenarTabla);
        
        modelo.addColumn("Año");
        modelo.addColumn("Semestre");
        modelo.addColumn("Id");
        modelo.addColumn("Estudiante");
        modelo.addColumn("Id");
        modelo.addColumn("Curso");
        
        totalPrograma.setModel(modelo);
        
        String sql = """
                     select i.anio, i.semestre, p.id, p.nombre, c.id, c.nombre
                     from inscripcion i
                     inner join persona p on i.estudiante_id = p.id
                     inner join curso c on i.curso_id = c.id
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
            
            totalPrograma.setModel(modelo);
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Inscripción" +  e.getMessage());
        }
        
    }
    
    public void agregarInscripcion(JTextField año, JTextField semestre, JTextField idEstudiante, JTextField idCurso) {
        if(año.getText().trim().isEmpty() || semestre.getText().trim().isEmpty() || idEstudiante.getText().trim().isEmpty()
                || idCurso.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Los campos no pueden ser vacios ");
        }else{
                setAño(Integer.parseInt(año.getText()));
                setSemestre(Integer.parseInt(semestre.getText()));
                setIdEstudiante(Integer.parseInt(idEstudiante.getText()));
                setIdCurso(Integer.parseInt(idCurso.getText()));
                
                String sql = "INSERT INTO inscripcion (anio, semestre, estudiante_id, curso_id) VALUES (?, ?, ?, ?)";
                
                try(Connection conn = ConexionDB.conectar();
                        PreparedStatement stmt = conn.prepareStatement(sql)){
                        
                       
                        stmt.setInt(1, getAño());
                        stmt.setInt(2, getSemestre());
                        stmt.setInt(3, getIdEstudiante());
                        stmt.setInt(4, getIdCurso());
                        
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Inscripción agregada exitosamente.");
                    
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar Inscripción: " + ex.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
                }
        }
    }
    
    public void actualizarInscripcion(JTextField año, JTextField semestre, JTextField idEstudiante, JTextField idCurso){
        if(año.getText().trim().isEmpty() || semestre.getText().trim().isEmpty()
                || idEstudiante.getText().trim().isEmpty()
                || idCurso.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "los campos no pueden ser vacios");
        }else{
            setAño(Integer.parseInt(año.getText()));
            setSemestre(Integer.parseInt(semestre.getText()));
            setIdEstudiante(Integer.parseInt(idEstudiante.getText()));
            setIdCurso(Integer.parseInt(idCurso.getText()));
            
            String sql = "update inscripcion set anio = ?, semestre = ? where estudiante_id = ? and curso_id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, getAño());
                stmt.setInt(2, getSemestre());
                stmt.setInt(3, getIdEstudiante());
                stmt.setInt(4, getIdCurso());

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
    }
    
    public void eliminarInscripcion(JTextField idEstudiante, JTextField idCurso){
        if(idEstudiante.getText().trim().isEmpty() || idCurso.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID's no pueden estar vacio");
        }else{
            setIdEstudiante(Integer.parseInt(idEstudiante.getText()));
            setIdCurso(Integer.parseInt(idCurso.getText()));
        
            String sql = "delete from inscripcion where Estudiante_id = ? and Curso_id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setInt(1, getIdEstudiante());
                stmt.setInt(2, getIdCurso());

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
    }
    
    public void buscarInscripcion(JTextField idEstudiante, JTextField idCurso){
        if(idEstudiante.getText().trim().isEmpty() || idCurso.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID's no pueden estar vacio");
        }else{
            setIdEstudiante(Integer.parseInt(idEstudiante.getText()));
            setIdCurso(Integer.parseInt(idCurso.getText()));
            
            String sql = """
                     select i.anio, i.semestre, p.nombre as nombre_estudiante, c.nombre as nombre_curso
                     from inscripcion i
                     inner join persona p on i.estudiante_id = p.id
                     inner join curso c on i.curso_id = c.id
                     where i.estudiante_id = ? and i.curso_id = ?
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, getIdEstudiante());
                stmt.setInt(2, getIdCurso());
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    String programa ="\nAño: " + rs.getString("anio") +
                                 "\nSemestre: " + rs.getString("semestre") +
                                 "\nEstudiante: " + rs.getString("nombre_estudiante") +
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
