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
public class Facultad {
    private double id;
    private String nombre;
    private Persona decano;
    private double idDecano;

    public Facultad(double id, String nombre, Persona decano, double idDecano) {
        this.id = id;
        this.nombre = nombre;
        this.decano = decano;
        this.idDecano = idDecano;
    }

    public Facultad() {
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

    public Persona getDecano() {
        return decano;
    }

    public void setDecano(Persona decano) {
        this.decano = decano;
    }

    public double getIdDecano() {
        return idDecano;
    }

    public void setIdDecano(double idDecano) {
        this.idDecano = idDecano;
    }
    
    
    
    @Override
    public String toString() {
        return String.format(
            "Facultad{id=%.0f, nombre='%s', decano=%s}",
            id, nombre, decano
        );
    }
    
    public void mostrarFacultad(JTable totalFacultad){
        
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<TableModel>(modelo);
        totalFacultad.setRowSorter(ordenarTabla);
        
        modelo.addColumn("Id");
        modelo.addColumn("Facultad");
        modelo.addColumn("Id Decano");
        modelo.addColumn("Nombre");
        
        totalFacultad.setModel(modelo);
        
        String sql = """
                     select f.id, f.nombre, p.id, p.nombre
                     from persona p
                     inner join Facultad f on p.id = f.decano_id;
                     """;
        String[] datos = new String[4];
        
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                
                
                modelo.addRow(datos);
            }
            
            totalFacultad.setModel(modelo);
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Facultad" +  e.getMessage());
        }
        
    }
    
    public void agregarFacultad(JTextField id, JTextField nombreFacultad, JTextField idDecano) {
        if(id.getText().trim().isEmpty() || nombreFacultad.getText().trim().isEmpty()
                || idDecano.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Los campos no pueden ser vacios ");
        }else{
            setId(Integer.parseInt(id.getText()));
            setNombre(nombreFacultad.getText());
            setIdDecano(Integer.parseInt(idDecano.getText()));
            

            String validarSql = "SELECT tipo FROM persona WHERE id = ?";
            String sql = "INSERT INTO facultad (id, nombre, decano_id) VALUES (?, ?, ?)";

            try (Connection conn = ConexionDB.conectar()) {
                try (PreparedStatement stmtValidar = conn.prepareStatement(validarSql)) {
                    stmtValidar.setInt(1,(int) getIdDecano());
                    try (ResultSet rs = stmtValidar.executeQuery()) {
                        if (rs.next()) {
                            String tipo = rs.getString("tipo");

                            if (!"profesor".equalsIgnoreCase(tipo)) {
                                JOptionPane.showMessageDialog(null, 
                                    "El ID corresponde a una persona tipo " + tipo + 
                                    ", no puede agregarse como decano.");
                                return;
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, 
                                "No existe ninguna Persona con ese ID.");
                            return;
                        }
                    }
                }


                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, (int) getId());
                    stmt.setString(2, getNombre());
                    stmt.setInt(3, (int) getIdDecano());
                    
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Facultad agregada exitosamente.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al agregar Facultad: " + ex.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
                System.out.println(e);
            }
        }
    }
    
    public void actualizarFacultad(JTextField id, JTextField nombreFacultad, JTextField idDecano){
        if(id.getText().trim().isEmpty() || nombreFacultad.getText().trim().isEmpty()
                || idDecano.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "los ID'S no pueden ser vacios");
        }else{
            setId(Integer.parseInt(id.getText()));
            setNombre(nombreFacultad.getText());
            setIdDecano(Integer.parseInt(idDecano.getText()));

            String sql = "update Facultad set decano_id = ?, nombre = ? where id = ?";

            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) getIdDecano());
                stmt.setString(2, getNombre());
                stmt.setInt(3, (int) getId());


                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Facultad actualizada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe Facultad con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error " + ex);
            }
        }
    }
    
    public void eliminarFacultad(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede estar vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
        
            String sql = "delete from facultad where id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setInt(1, (int) getId());

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Facultad eliminada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ninguna Facultad con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex);
            }
        }  
    }
    
    public void buscarFacultad(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede estar vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
            String sql = """
                     SELECT p.id, p.nombre, p.apellido, p.correo, f.nombre as nombre_facultad, f.id as id_facultad
                     FROM persona p
                     INNER JOIN facultad f ON p.id = f.decano_id
                     WHERE f.id = ?;
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) getId());
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    String facultad = "ID: " + rs.getInt("id") + 
                                 "\nNombre: " + rs.getString("nombre") +
                                 "\nApellidos: " + rs.getString("apellido") +
                                 "\nCorreo: " + rs.getString("correo") +
                                 "\nID Facultad: " + rs.getString("id_facultad") +
                                 "\nFacultad: " + rs.getString("nombre_facultad");

                JOptionPane.showMessageDialog(null, facultad);
                }else{
                    JOptionPane.showMessageDialog(null, "No se encontró ningún Información");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al buscar información " + ex);
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e);
            }
        }
    }   
     
}
