/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Controller;

import com.mycompany.aplicacionpoo.Config.ConexionDB;
import com.mycompany.aplicacionpoo.Model.Estudiante;
import com.mycompany.aplicacionpoo.Model.Programa;
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
public class EstudianteController {
    public ArrayList<Estudiante> mostrarEstudiante(){
        ArrayList<Estudiante> listaEstudiante = new ArrayList<>();
        String sql = """
                     select pe.id, e.codigo, pe.nombre, e.promedio, e.activo, p.nombre as nombre_programa
                     from estudiante e
                     inner join persona pe on e.id = pe.id
                     inner join programa p on e.programa_id = p.id;
                     """;        
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                Estudiante e = new Estudiante();
                e.setId(rs.getDouble("id"));
                e.setCodigo(rs.getDouble("codigo"));
                e.setNombres(rs.getString("nombre"));
                e.setActivo(rs.getBoolean("activo"));
                e.setPromedio(rs.getDouble("promedio"));
                
                Programa p = new Programa();
                p.setNombre(rs.getString("nombre_programa"));
                
                e.setPrograma(p);
                listaEstudiante.add(e);
            }
            
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "no se pudo mostrar Estudiante" +  e.getMessage());
        }
        return listaEstudiante;
    }
    
    public void agregarEstudiante(double id, double codigo, double promedio, boolean estado, double idPrograma) {
        String validarSql = "SELECT tipo FROM persona WHERE id = ?";
            String sql = "INSERT INTO estudiante (id, codigo, promedio, activo, programa_id) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = ConexionDB.conectar()) {
                try (PreparedStatement stmtValidar = conn.prepareStatement(validarSql)) {
                    stmtValidar.setDouble(1, id);
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
                    stmt.setDouble(1, id);
                    stmt.setDouble(2, codigo);
                    stmt.setDouble(3, promedio);
                    stmt.setBoolean(4, estado);
                    stmt.setDouble(5, idPrograma);
                    
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Estdiante agregado exitosamente.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error al agregar profesor: " + ex.getMessage());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error inesperado: " + e.getMessage());
            }
    }
    
    public void eliminarEstudiante(double id){
        String sql = "delete from estudiante where id = ?";

            try(Connection conn = ConexionDB.conectar();
                    PreparedStatement stmt = conn.prepareCall(sql)){

                stmt.setDouble(1, id);

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
    
    public Estudiante buscarEstudiante(double id){
        String sql = """
                     select pe.id, e.codigo, e.promedio, e.activo, pe.nombre, p.id as id_programa, p.nombre as nombre_programa
                         from estudiante e
                         inner join persona pe on pe.id = e.id
                         inner join programa p on p.id = e.programa_id
                         where e.id = ?
                     """;
        
            try(Connection conn = ConexionDB.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)){

                stmt.setInt(1, (int) id);
                ResultSet rs = stmt.executeQuery();

                if(rs.next()){
                    Estudiante e = new Estudiante();
                    e.setId(rs.getDouble("id"));
                    e.setCodigo(rs.getDouble("codigo"));
                    e.setNombres(rs.getString("nombre"));
                    e.setActivo(rs.getBoolean("activo"));
                    e.setPromedio(rs.getDouble("promedio"));

                    Programa p = new Programa();
                    p.setId(rs.getDouble("id_programa"));
                    p.setNombre(rs.getString("nombre_programa"));

                    e.setPrograma(p);
                    return e;
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
    
    public void actualizarEstudiante(double id, double codigo, double promedio, boolean estado, double idPrograma) {
        String validarSql = "SELECT tipo FROM persona WHERE id = ?";
            String sql = "UPDATE estudiante SET codigo = ?, promedio = ?, activo = ?, programa_id = ? where id = ?";

            try (Connection conn = ConexionDB.conectar()) {
                try (PreparedStatement stmtValidar = conn.prepareStatement(validarSql)) {
                    stmtValidar.setDouble(1, id);
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
                    stmt.setDouble(1, codigo);
                    stmt.setDouble(2, promedio);
                    stmt.setBoolean(3, estado);
                    stmt.setDouble(4, idPrograma);
                    stmt.setDouble(5, id);

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
