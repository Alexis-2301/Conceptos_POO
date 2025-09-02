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
public class Estudiante extends Persona{
    private double codigo;
    private Programa programa;
    private boolean activo;
    private double promedio;
    private int idPrograma;

    public Estudiante(double id, String nombres, String apellidos, String email, double codigo, Programa programa, boolean activo, double promedio) {
        super(id, nombres, apellidos, email, "Estudiante");
        this.codigo = codigo;
        this.programa = programa;
        this.activo = activo;
        this.promedio = promedio;
    }

    public Estudiante() {
    }

    public double getCodigo() {
        return codigo;
    }

    public void setCodigo(double codigo) {
        this.codigo = codigo;
    }

    public Programa getPrograma() {
        return programa;
    }

    public void setPrograma(Programa programa) {
        this.programa = programa;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public int getIdPrograma() {
        return idPrograma;
    }

    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Estudiante{%s, codigo=%.0f, programa='%s', activo=%b, promedio=%.2f}",
            super.toString(), codigo, programa.getNombre(), activo, promedio
        );
    }
    
    public void mostrarEstudiante(JTable totalFacultad){
        
        
        DefaultTableModel modelo = new DefaultTableModel();
        
        TableRowSorter<TableModel> ordenarTabla = new TableRowSorter<TableModel>(modelo);
        totalFacultad.setRowSorter(ordenarTabla);
        
        modelo.addColumn("Id");
        modelo.addColumn("Codigo");
        modelo.addColumn("Nombre");
        modelo.addColumn("Promedio");
        modelo.addColumn("Estado");
        modelo.addColumn("Programa");
        
        totalFacultad.setModel(modelo);
        
        String sql = """
                     select e.id, e.codigo, pe.nombre, e.promedio, e.activo, p.nombre
                     from estudiante e
                     inner join persona pe on e.id = pe.id
                     inner join programa p on e.programa_id = p.id;
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
            
            totalFacultad.setModel(modelo);
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Estudiante" +  e.getMessage());
        }
        
    }
    
    public void agregarEstudiante(JTextField id, JTextField codigo, JTextField promedio, JComboBox estado, JTextField idPrograma) {
        if(id.getText().trim().isEmpty() || codigo.getText().trim().isEmpty() || promedio.getText().trim().isEmpty()
                || estado.getSelectedItem().toString().trim().isEmpty() || idPrograma.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Los campos no pueden ser vacios ");
        }else{
            setId(Integer.parseInt(id.getText()));
            setCodigo(Double.parseDouble(codigo.getText()));
            setPromedio(Double.parseDouble(promedio.getText()));
            String seleccionado = estado.getSelectedItem().toString();
            setActivo(seleccionado.equals("Activo"));
            setIdPrograma(Integer.parseInt(idPrograma.getText()));
            

            String validarSql = "SELECT tipo FROM persona WHERE id = ?";
            String sql = "INSERT INTO estudiante (id, codigo, promedio, activo, programa_id) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = ConexionDB.conectar()) {
                try (PreparedStatement stmtValidar = conn.prepareStatement(validarSql)) {
                    stmtValidar.setDouble(1, getId());
                    try (ResultSet rs = stmtValidar.executeQuery()) {
                        if (rs.next()) {
                            String tipo = rs.getString("tipo");

                            if (!"estudiante".equalsIgnoreCase(tipo)) {
                                JOptionPane.showMessageDialog(null, 
                                    "El ID corresponde a una persona tipo " + tipo + 
                                    ", no puede agregarse como estudinate.");
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
                    stmt.setDouble(1, getId());
                    stmt.setDouble(2, getCodigo());
                    stmt.setDouble(3, getPromedio());
                    stmt.setBoolean(4, isActivo());
                    stmt.setInt(5, getIdPrograma());
                    
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Estdiante agregado exitosamente.");
                    
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al agregar profesor: " + ex.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
            }
        }
    }
    
    public void eliminarEstudiante(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede estar vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
        
            String sql = "delete from estudiante where id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setDouble(1, getId());

                int filas = stmt.executeUpdate();

                if (filas > 0) {
                    JOptionPane.showMessageDialog(null, "Estudinate eliminado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No existe ningún Estudinate con ese ID.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error" + ex);
            }
        }  
    }
    
    public void buscarEstudiante(JTextField id){
        if(id.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "ID no puede estar vacio");
        }else{
            setId(Integer.parseInt(id.getText()));
            String sql = """
                     select pe.id, e.codigo, e.promedio, e.activo, pe.nombre, p.id as id_programa, p.nombre as nombre_programa
                         from estudiante e
                         inner join persona pe on pe.id = e.id
                         inner join programa p on p.id = e.programa_id
                         where e.id = ?
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) getId());
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    String estudiante = "ID: " + rs.getInt("id") + 
                                 "\nCodigo: " + rs.getDouble("codigo") +
                                 "\nNombre: " + rs.getString("nombre") +
                                 "\nEstado: " + rs.getString("activo") +
                                 "\nPromedio: " + rs.getDouble("promedio") +
                                 "\nID Programa: " + rs.getString("id_programa") +
                                 "\nPrograma: " + rs.getString("nombre_programa");

                JOptionPane.showMessageDialog(null, estudiante);
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
    
    public void actualizarEstudiante(JTextField id, JTextField codigo, JTextField promedio, JComboBox estado, JTextField idPrograma) {
        if(id.getText().trim().isEmpty() || codigo.getText().trim().isEmpty() || promedio.getText().trim().isEmpty()
                || estado.getSelectedItem().toString().trim().isEmpty() || idPrograma.getText().trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Los campos no pueden ser vacios ");
        }else{
            setId(Integer.parseInt(id.getText()));
            setCodigo(Double.parseDouble(codigo.getText()));
            setPromedio(Double.parseDouble(promedio.getText()));
            String seleccionado = estado.getSelectedItem().toString();
            setActivo(seleccionado.equals("Activo"));
            setIdPrograma(Integer.parseInt(idPrograma.getText()));
            

            String validarSql = "SELECT tipo FROM persona WHERE id = ?";
            String sql = "UPDATE estudiante SET codigo = ?, promedio = ?, activo = ?, programa_id = ? where id = ?";

            try (Connection conn = ConexionDB.conectar()) {
                try (PreparedStatement stmtValidar = conn.prepareStatement(validarSql)) {
                    stmtValidar.setDouble(1, getId());
                    try (ResultSet rs = stmtValidar.executeQuery()) {
                        if (rs.next()) {
                            String tipo = rs.getString("tipo");

                            if (!"estudiante".equalsIgnoreCase(tipo)) {
                                JOptionPane.showMessageDialog(null, 
                                    "El ID corresponde a una persona tipo " + tipo + 
                                    ", no puede agregarse como estudinate.");
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
                    stmt.setDouble(1, getCodigo());
                    stmt.setDouble(2, getPromedio());
                    stmt.setBoolean(3, isActivo());
                    stmt.setDouble(4, getIdPrograma());
                    stmt.setDouble(5, getId());

                    int filas = stmt.executeUpdate();

                    if (filas > 0) {
                        JOptionPane.showMessageDialog(null, "Estudiante actualizado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(null, "No existe ningún estudiante con ese ID.");
                    }
                }
            }catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al actualizar " + ex);                
            } catch (Exception e){
                JOptionPane.showMessageDialog(null, "Error " + e.toString());
            }
        }
    }
}
