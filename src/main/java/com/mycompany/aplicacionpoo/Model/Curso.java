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
import javax.swing.JComboBox;
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
public class Curso {
    private int id;
    private String nombre;
    private Programa programa;
    private boolean activo;
    private double idPrograma;

    public Curso(int id, String nombre, Programa programa, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.programa = programa;
        this.activo = activo;
    }

    public Curso() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public double getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(double idPrograma) {
        this.idPrograma = idPrograma;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Curso{id=%d, nombre='%s', programa=%s, activo=%b}",
            id, nombre, programa, activo
        );
    }
    
    public void mostrarCurso(JTable totalFacultad){
        
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<TableModel>(modelo);
        totalFacultad.setRowSorter(ordenarTabla);
        
        modelo.addColumn("Id");
        modelo.addColumn("Nombre");
        modelo.addColumn("Estado");
        modelo.addColumn("Id Programa");
        modelo.addColumn("Programa");
        
        totalFacultad.setModel(modelo);
        
        String sql = """
                     select c.id, c.nombre, c.activo, p.id, p.nombre
                     from curso c
                     inner join programa p on c.programa_id = p.id;
                     """;
        String[] datos = new String[5];
        
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                
                
                modelo.addRow(datos);
            }
            
            totalFacultad.setModel(modelo);
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Curso" +  e.getMessage());
        }
        
    }
    
    public void agregarCurso(JTextField id, JTextField nombre, JComboBox estado, JTextField idPrograma) {
        if(id.getText().trim().isEmpty() || nombre.getText().trim().isEmpty()
                || estado.getSelectedItem().toString().trim().isEmpty()
                || idPrograma.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Los campos no pueden ser vacios ");
        }else{
            setId(Integer.parseInt(id.getText()));
            setNombre(nombre.getText());            
            String seleccionado = estado.getSelectedItem().toString();
            setActivo(seleccionado.equals("Activo"));          
            setIdPrograma(Integer.parseInt(idPrograma.getText()));
            

            String sql = "INSERT INTO curso (id, nombre, activo, programa_id) VALUES (?, ?, ?, ?)";
                
                try(Connection conn = ConexionDB.conectar();
                        PreparedStatement stmt = conn.prepareStatement(sql)){
                        
                       
                        stmt.setDouble(1, getId());
                        stmt.setString(2, getNombre());
                        stmt.setBoolean(3, getActivo());
                        stmt.setDouble(4, getIdPrograma());
                        
                        stmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Curso agregado exitosamente.");
                    
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al agregar Curso: " + ex.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
                }
        }
    }
    
    public void eliminarCurso(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede estar vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
        
            String sql = "delete from curso where id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setInt(1, getId());

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
    }
    
    public void buscarCurso(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede estar vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
            String sql = """
                     SELECT c.id, c.nombre, c.activo, p.id as id_programa, p.nombre as nombre_programa
                     FROM curso c
                     INNER JOIN programa p ON p.id = c.programa_id
                     WHERE c.id = ?;
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) getId());
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    String Programa = "ID: " + rs.getInt("id") + 
                                 "\nNombre: " + rs.getString("nombre") +
                                 "\nEstado: " + rs.getString("activo") +
                                 "\nID Programa: " + rs.getString("id_programa") +
                                 "\nPrograma: " + rs.getString("nombre_programa");

                JOptionPane.showMessageDialog(null, Programa);
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
    
    public void actualizarCurso(JTextField id, JTextField nombre, JComboBox estado, JTextField idPrograma) {
        if(id.getText().trim().isEmpty() || nombre.getText().trim().isEmpty()
                || estado.getSelectedItem().toString().trim().isEmpty()
                || idPrograma.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos.");
        }else{
            setId(Integer.parseInt(id.getText()));
            setNombre(nombre.getText());
            String seleccionado = estado.getSelectedItem().toString();
            setActivo(seleccionado.equals("Activo"));

            setIdPrograma(Integer.parseInt(idPrograma.getText()));

            String sql = "UPDATE curso SET nombre = ?, activo = ?, programa_id = ? where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                
                stmt.setString(1, getNombre());
                stmt.setBoolean(2, getActivo());
                stmt.setDouble(3, getIdPrograma());
                stmt.setDouble(4, getId());
                
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

}
