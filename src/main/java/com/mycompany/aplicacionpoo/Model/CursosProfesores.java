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
public class CursosProfesores implements Servicios{
    private List<CursoProfesor> cursoProfesores;

    public CursosProfesores(List<CursoProfesor> cursoProfesores) {
        this.cursoProfesores = cursoProfesores;
    }

    public List<CursoProfesor> getCursoProfesor() {
        return cursoProfesores;
    }

    public void setCursoProfesor(List<CursoProfesor> cursoProfesores) {
        this.cursoProfesores = cursoProfesores;
    }
    
    public void inscribirCursoProfesor(CursoProfesor cursoProfesor){
        cursoProfesores.add(cursoProfesor);
    }


    @Override
    public String imprimirPosicion(int posicion) {
        if(posicion >= 0 && posicion < cursoProfesores.size()){
            return cursoProfesores.get(posicion).toString();
        }else{
            return "posición no valida";
        }
    }

    @Override
    public int cantidadActual() {
        return cursoProfesores.size();
    }

    @Override
    public List<String> imprimirListado() {
        
        List<String> info = new ArrayList<>();
        if(cursoProfesores == null || cursoProfesores.isEmpty()){
            info.add("lista vacia");
        }else{
             for(int i = 0; i < cursoProfesores.size(); i++){
                info.add(cursoProfesores.get(i).toString());
            }
        }
        return info;
    }

    @Override
    public String toString() {
        return "CursosProfesores{" + "cursoProfesores=" + cursoProfesores + '}';
    }
    
    /*public void guardarInformacion(CursoProfesor cp){
        
        String sql = "INSERT INTO cursoProfesor (curso_id, profesor_id, anio, semestre) values (?,?,?,?)";
        
        try(Connection conn = ConexionDB.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, cp.getCurso().getId());
            stmt.setInt(2, (int) cp.getProfesor().getId());
            stmt.setInt(3, cp.getAño());
            stmt.setInt(4, cp.getSemestre());
            
            stmt.executeUpdate();
            System.out.println("Datos guardados");
            
        } catch (SQLException ex) {
            System.out.println("Error al guardar datos " + ex);
        } catch (Exception e){
            System.out.println("Error " + e);
        }
    }
    
    public ArrayList<CursoProfesor> mostrarInformacion() {
    ArrayList<CursoProfesor> listaInfo = new ArrayList<>();
    
    String sql = """
        SELECT cp.anio, cp.semestre, 
               c.id AS curso_id, 
               p.id AS profesor_id
        FROM cursoProfesor cp
        JOIN curso c ON cp.curso_id = c.id
        JOIN profesor p ON cp.profesor_id = p.id
        """;
    
    try (Connection conn = ConexionDB.conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            int curso_id = rs.getInt("curso_id");
            int profesor_id = rs.getInt("profesor_id");
            int anio = rs.getInt("anio");
            int semestre = rs.getInt("semestre");
            
            CursoProfesor cp = new CursoProfesor(curso_id, profesor_id, anio, semestre);
            listaInfo.add(cp);
        }
        
    } catch (SQLException ex) {
        System.out.println("Error al mostrar información de base de datos: " + ex);
    } catch (Exception e) {
        System.out.println("Error: " + e);
    }
    
    return listaInfo;
}*/


}
