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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class Programa {
    private double id;
    private String nombre;
    private double duracion;
    private Date registro;
    private Facultad facultad;
    private int idFacultad;

    public Programa(double id, String nombre, double duracion, Date registro, Facultad facultad, int idFacultad) {
        this.id = id;
        this.nombre = nombre;
        this.duracion = duracion;
        this.registro = registro;
        this.facultad = facultad;
    }

    public Programa() {
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getDuracion() {
        return duracion;
    }

    public void setDuracion(double duracion) {
        this.duracion = duracion;
    }

    public Date getRegistro() {
        return registro;
    }

    public void setRegistro(Date registro) {
        this.registro = registro;
    }

    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }

    public int getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(int idFacultad) {
        this.idFacultad = idFacultad;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Programa{id=%.0f, nombre='%s', duracion=%.1f, registro=%s, facultad=%s}",
            id, nombre, duracion, registro, facultad
        );
    }
    
    public void mostrarPrograma(JTable totalPrograma){
        
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<TableModel>(modelo);
        totalPrograma.setRowSorter(ordenarTabla);
        
        modelo.addColumn("Id");
        modelo.addColumn("Nombres");
        modelo.addColumn("Duracion");
        modelo.addColumn("Registro");
        modelo.addColumn("Id Facultad");
        modelo.addColumn("Facultad");
        
        totalPrograma.setModel(modelo);
        
        String sql = """
                     select p.id, p.nombre, p.duracion, p.registro, f.id, f.nombre
                     from programa p
                     inner join facultad f on p.facultad_id = f.id
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
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Programa" +  e.getMessage());
        }
        
    }
    
    public void guardarPrograma(JTextField id, JTextField nombrePrograma, JTextField duracion, JTextField registro, JTextField idFacultad) {
        if(id.getText().trim().isEmpty() || nombrePrograma.getText().trim().isEmpty()
                || duracion.getText().trim().isEmpty() || registro.getText().trim().isEmpty()
                || idFacultad.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Los campos no pueden ser vacios ");
        }else{
                setId(Integer.parseInt(id.getText()));
                setNombre(nombrePrograma.getText());
                setDuracion(Double.parseDouble(duracion.getText()));
                
                SimpleDateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    setRegistro(fd.parse(registro.getText()));
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(null, "Fecha no aceptada"  + ex);
                }
                
                setIdFacultad(Integer.parseInt(idFacultad.getText()));
                
                String sql = "INSERT INTO programa (id, nombre, duracion, registro, facultad_id) VALUES (?, ?, ?, ?, ?)";
                
                try(Connection conn = ConexionDB.conectar();
                        PreparedStatement stmt = conn.prepareStatement(sql)){
                        
                       
                        stmt.setDouble(1, getId());
                        stmt.setString(2, getNombre());
                        stmt.setDouble(3, getDuracion());
                        stmt.setDate(4, new java.sql.Date(getRegistro().getTime()));
                        stmt.setInt(5, getIdFacultad());
                        
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Programa agregado exitosamente.");
                    
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar Programa: " + ex.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
                }
        }
    }
    
    public void actualizarPrograma(JTextField id, JTextField nombrePrograma, JTextField duracion, JTextField registro, JTextField idFacultad){
        if(id.getText().trim().isEmpty() || nombrePrograma.getText().trim().isEmpty()
                || duracion.getText().trim().isEmpty() || registro.getText().trim().isEmpty()
                || idFacultad.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "los campos no pueden ser vacios");
        }else{
            setId(Integer.parseInt(id.getText()));
            setNombre(nombrePrograma.getText());
            setDuracion(Double.parseDouble(duracion.getText()));
            
            try{
               SimpleDateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
               setRegistro(fd.parse(registro.getText()));
            } catch (ParseException e){
                JOptionPane.showMessageDialog(null, "Fecha no aceptad" + e);
            }
            
            setIdFacultad(Integer.parseInt(idFacultad.getText()));

            String sql = "update Programa set nombre = ?, duracion = ?, registro = ?, facultad_id = ? where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, getNombre());
                stmt.setDouble(2, getDuracion());
                stmt.setDate(3, new java.sql.Date(getRegistro().getTime()));
                stmt.setDouble(4, getIdFacultad());
                stmt.setDouble(5, getId());

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
    
    public void eliminarPrograma(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede estar vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
        
            String sql = "delete from programa where id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setDouble(1, getId());

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
    }
    
    public void buscarPrograma(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede estar vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
            
            String sql = """
                     SELECT p.id, p.nombre, p.duracion, p.registro, f.nombre as nombre_facultad, f.id as id_facultad
                     FROM programa p
                     INNER JOIN facultad f ON p.facultad_id = f.id
                     WHERE p.id = ?;
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) getId());
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    String programa = "ID: " + rs.getInt("id") + 
                                 "\nNombre: " + rs.getString("nombre") +
                                 "\nDuración: " + rs.getString("duracion") +
                                 "\nRegistro: " + rs.getString("Registro") +
                                 "\nID Facultad: " + rs.getString("id_facultad") +
                                 "\nFacultad: " + rs.getString("nombre_facultad");

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
