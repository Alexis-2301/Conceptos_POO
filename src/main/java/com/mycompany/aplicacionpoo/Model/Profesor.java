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
public class Profesor extends Persona{
    private String tipoContrato;

    public Profesor(double id, String nombres, String apellidos, String email, String tipoContrato) {
        super(id, nombres, apellidos, email, "profesor");
        this.tipoContrato = tipoContrato;
    }

    public Profesor() {
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    @Override
    public String toString() {
        return String.format(
            "Profesor{%s, tipoContrato='%s'}",
            super.toString(), tipoContrato
        );
    }
    
    public void agregarProfesor(JTextField id, JTextField tipoContrato) {
        if(id.getText().trim().isEmpty() || tipoContrato.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Los campos no pueden ser vacios ");
        }else{
            setId(Integer.parseInt(id.getText()));
            setTipoContrato(tipoContrato.getText());

            String validarSql = "SELECT tipo FROM persona WHERE id = ?";
            String sql = "INSERT INTO profesor (id, tipo_contrato) VALUES (?, ?)";

            try (Connection conn = ConexionDB.conectar()) {
                try (PreparedStatement stmtValidar = conn.prepareStatement(validarSql)) {
                    stmtValidar.setInt(1,(int) getId());
                    try (ResultSet rs = stmtValidar.executeQuery()) {
                        if (rs.next()) {
                            String tipo = rs.getString("tipo");

                            if (!"profesor".equalsIgnoreCase(tipo)) {
                                JOptionPane.showMessageDialog(null, 
                                    "El ID corresponde a una persona tipo " + tipo + 
                                    ", no puede agregarse como profesor.");
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, 
                                "No existe ninguna persona con ese ID.");
                            return;
                        }
                    }
                }


                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, (int) getId());
                    stmt.setString(2, getTipoContrato());
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Profesor agregado exitosamente.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al agregar profesor: " + ex.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
            }
        }
    }

    
    public void actualizarProfesor(JTextField id, JTextField tipoContrato){
        if(id.getText().trim().isEmpty() || tipoContrato.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "los campos no pueden ser vacios");
        }else{
            setId(Integer.parseInt(id.getText()));
            setTipoContrato(tipoContrato.getText());

            String sql = "update profesor set tipo_contrato = ? where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setString(1, getTipoContrato());
                stmt.setInt(2, (int) getId());


                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Profesor actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ningún profesor con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error " + ex);
            }
        }
    }
    
    public void mostrarProfesor(JTable totalProfesor){
        
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<TableModel>(modelo);
        totalProfesor.setRowSorter(ordenarTabla);
        
        modelo.addColumn("id");
        modelo.addColumn("Nombres");
        modelo.addColumn("Apellidos");
        modelo.addColumn("Correo");
        modelo.addColumn("Tipo Contrato");
        
        totalProfesor.setModel(modelo);
        
        String sql = """
                     select p.id, p.nombre, p.apellido, p.correo, pr.tipo_contrato
                     from persona p
                     inner join profesor pr on p.id = pr.id;
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
            
            totalProfesor.setModel(modelo);
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar profesor" +  e.getMessage());
        }
        
    }
    
    public void eliminarProfesor(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede estar vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
        
            String sql = "delete from profesor where id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setInt(1, (int) getId());

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Profesor eliminado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ningún profesor con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex);
            }
        }
        
    }
    
    public void buscarProfesor(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede estar vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
            String sql = """
                     SELECT p.id, p.nombre, p.apellido, p.correo, p.tipo, pr.tipo_contrato
                     FROM persona p
                     INNER JOIN profesor pr ON p.id = pr.id
                     WHERE p.id = ?;
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) getId());
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    String profesor = "ID: " + rs.getInt("id") + 
                                 "\nNombre: " + rs.getString("nombre") +
                                 "\nApellidos: " + rs.getString("apellido") +
                                 "\nCorreo: " + rs.getString("correo") +
                                 "\nTipo Contrato: " + rs.getString("tipo_contrato");

                JOptionPane.showMessageDialog(null, profesor);
                }else{
                    JOptionPane.showMessageDialog(null, "No se encontró ningún profesor");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar Persona " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e);
            }
        }
        
    }
}
