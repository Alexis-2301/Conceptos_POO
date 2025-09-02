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
public class Persona {
    private double id;
    private String nombres;
    private String apellidos;
    private String email;
    private String tipo;
    
    public Persona(double id, String nombres, String apellidos, String email, String tipo){
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.tipo = tipo;
    }

    public Persona() {
    }
    
    public double getId(){
        return id;
    }
    public void setId(double id){
        this.id = id;
    }
        public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", nombres=" + nombres + ", apellidos=" + apellidos + ", email=" + email + ", tipo=" + tipo + '}';
    }
    
    public void guardarPersona(JTextField id, JTextField nombre, JTextField apellido, JTextField email, JComboBox tipo){
        if (id.getText().trim().isEmpty() || 
            nombre.getText().trim().isEmpty() || 
            apellido.getText().trim().isEmpty() || 
            email.getText().trim().isEmpty() || 
            tipo.getSelectedItem() == null || 
            tipo.getSelectedItem().toString().trim().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos.");
        }else{
            setNombres(nombre.getText());
            setId(Integer.parseInt(id.getText()));
            setApellidos(apellido.getText());
            setEmail(email.getText());
            setTipo((String) tipo.getSelectedItem());

            String sql = "INSERT INTO persona (nombre, apellido, correo, tipo, id) values (?, ?, ?, ?, ?)";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, getNombres());
                stmt.setString(2, getApellidos());
                stmt.setString(3, getEmail());
                stmt.setString(4, getTipo());
                stmt.setInt(5, (int) getId());

                stmt.executeUpdate();

                JOptionPane.showMessageDialog(null, "Agregado correctamente");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al agregar persona " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e.toString());
            }
        }
    }
    
    public void eliminarPersona(JTextField id) {
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede ser vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
        
            String sql = "Delete from persona where id = ? ";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) getId());
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
    }
    
    public void actualizarPersona(JTextField id, JTextField nombre, JTextField apellido, JTextField email, JComboBox tipo){
        if (id.getText().trim().isEmpty() || 
            nombre.getText().trim().isEmpty() || 
            apellido.getText().trim().isEmpty() || 
            email.getText().trim().isEmpty() || 
            tipo.getSelectedItem() == null || 
            tipo.getSelectedItem().toString().trim().isEmpty()) {

            JOptionPane.showMessageDialog(null, "Debe llenar todos los campos.");
        }else{
            setNombres(nombre.getText());
            setId(Integer.parseInt(id.getText()));
            setApellidos(apellido.getText());
            setEmail(email.getText());
            setTipo((String) tipo.getSelectedItem());

            String sql = "UPDATE persona SET nombre = ?, apellido = ?, correo = ?, tipo = ? where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, getNombres());
                stmt.setString(2, getApellidos());
                stmt.setString(3, getEmail());
                stmt.setString(4, getTipo());
                stmt.setInt(5, (int) getId());
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
    }
    
    public void mostrarPersonas(JTable totalPersonas){
        DefaultTableModel modelo = new DefaultTableModel();
        
        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<TableModel>(modelo);
        totalPersonas.setRowSorter(ordenarTabla);
        
        modelo.addColumn("id");
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Correo");
        modelo.addColumn("Tipo");
        
        totalPersonas.setModel(modelo);
        
        String sql = "select * from persona";
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
            
            totalPersonas.setModel(modelo);
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar alumnos" +  e.getMessage());
        }
        
    }
    
    public void buscarPersona(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede ser vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
            String sql = "Select * from persona where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) getId());
                ResultSet rs = stmt.executeQuery();

                while(rs.next()){
                    String persona = "ID: " + rs.getInt("id") + 
                                 "\nNombre: " + rs.getString("nombre") +
                                 "\nApellidos: " + rs.getString("apellido") +
                                 "\nCorreo: " + rs.getString("correo") +
                                 "\nTipo: " + rs.getString("tipo");

                JOptionPane.showMessageDialog(null, persona);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar Persona " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e);
            }
        }
        
    }
}
