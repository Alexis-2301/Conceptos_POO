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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicol
 */
public class InscripcionesPersonas {
    private List<Persona> personas;

    public InscripcionesPersonas(List<Persona> persona) {
        this.personas = persona;
    }

    public List<Persona> getPersona() {
        return personas;
    }

    public void setPersona(List<Persona> personas) {
        this.personas = personas;
    }
    
    public void inscribirPersona(Persona persona){
        personas.add(persona);
    }
    
    public void eliminarPersona(double id){
        boolean encontrada = false;
        
        for (Persona p : personas) {
            if (p.getId() == id) {
                personas.remove(p);
                encontrada = true;
                break;
            }
        }
        if(encontrada){
            System.out.println("Persona eliminada");
        }else{
            System.out.println("Persona no encontrada");
        }
    }
    
    public void modificarPersona(Persona persona) {
        boolean actualizada = false;

        for (int i = 0; i < personas.size(); i++) {
            if (personas.get(i).getId() == persona.getId()) {
                personas.set(i, persona);
                actualizada = true;
                break;
            }
        }

        if (actualizada) {
            System.out.println("Se actualizó correctamente");
        } else {
            System.out.println("No se encontró la persona para actualizar");
        }
    }

    @Override
    public String toString() {
        return "InscripcionesPersonas{" + "personas=" + personas + '}';
    }
    
    
    
    public void guardarInformacion(Persona persona){
        String sql = "INSERT INTO persona (nombre, apellido, correo, tipo, id) values (?, ?, ?, ?, ?)";
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setString(1, persona.getNombres());
            stmt.setString(2, persona.getApellidos());
            stmt.setString(3, persona.getEmail());
            stmt.setString(4, persona.getTipo());
            stmt.setInt(5, (int) persona.getId());
            
            stmt.executeUpdate();
            
            System.out.println("Datos guardados");
            
        } catch (SQLException ex) {
            System.out.println("Error con la base de datos " + ex);
        } catch (Exception e){
            System.out.println("Error " + e);
        }
    }
    
    public static ArrayList<Persona> mostrarInformacion(){
        ArrayList<Persona> listaInfo = new ArrayList<>();
        
        String sql = "SELECT * FROM persona";
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()){
            
            while(rs.next()){
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String correo = rs.getString("correo");
                String tipo = rs.getString("tipo");
                
                Persona p = new Persona(id, nombre, apellido, correo, tipo);
                listaInfo.add(p);
            }
            
        } catch (SQLException ex) {
            System.out.println("Error en base de datos " + ex);
        } catch (Exception e){
            System.out.println("Error " + e);
        }
        
        return listaInfo;
    }
}
