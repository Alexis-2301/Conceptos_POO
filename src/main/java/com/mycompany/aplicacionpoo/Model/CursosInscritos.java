/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Model;

import com.mycompany.aplicacionpoo.Model.Servicios;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicol
 */
public class CursosInscritos implements Servicios{
    private List<Inscripcion> cursosInscritos; 

    public CursosInscritos(List<Inscripcion> cursosInscritos) {
        this.cursosInscritos = cursosInscritos;
    }

    public List<Inscripcion> getCursosInscritos() {
        return cursosInscritos;
    }

    public void setCursosInscritos(List<Inscripcion> cursosInscritos) {
        this.cursosInscritos = cursosInscritos;
    }
    
    public void inscribirCursos(Inscripcion inscrito){
        cursosInscritos.add(inscrito);        
    }
    
    public void eliminarCursosInscrito(int id){
        boolean encontrado = false;
        
        for(int i = 0; i < cursosInscritos.size(); i++){
            if(cursosInscritos.get(i).getCurso().getId() == id){
                cursosInscritos.remove(i);
                encontrado = true;
                break;
            }
        }
        if(encontrado){
            System.out.println("inscripcion Eliminada");
        }else{
            System.out.println("Inscripcion no encontrada");
        }
    }
    
    public void actualizarCursoInscrito(Inscripcion inscripcion){
        boolean encontrado = false;
        
        for(int i = 0; i < cursosInscritos.size(); i++){
            if(cursosInscritos.get(i).getCurso().getId() == inscripcion.getCurso().getId()){
                cursosInscritos.set(i, inscripcion);
                encontrado = true;
                break;
            }
        }
        if(encontrado){
            System.out.println("Inscipción actualizada correctamente");
        }else{
            System.out.println("No se encontró Inscripción");
        }
    }

    @Override
    public String toString() {
        return "CursosInscritos{" + "cursosInscritos=" + cursosInscritos + '}';
    }

    @Override
    public String imprimirPosicion(int posicion) {
        return cursosInscritos.get(posicion).toString();
    }

    @Override
    public int cantidadActual() {
        return cursosInscritos.size();
    }

    @Override
    public List<String> imprimirListado() {
        List<String> info = new ArrayList<>();
            if(cursosInscritos == null || cursosInscritos.isEmpty()){
                info.add("no hay nada que mostrar");
            }else{
                for(int i = 0; i < cursosInscritos.size(); i++){
                    info.add(cursosInscritos.get(i).toString());
                }
            }
        return info;
    }
}
