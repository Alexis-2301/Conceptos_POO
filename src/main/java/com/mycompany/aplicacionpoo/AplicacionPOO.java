/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.aplicacionpoo;


import com.mycompany.aplicacionpoo.Model.InscripcionesPersonas;
import com.mycompany.aplicacionpoo.Model.Persona;
import java.sql.SQLException;
import java.util.ArrayList;




/**
 *
 * @author nicol
 */
public class AplicacionPOO {

    public static void main(String[] args) throws SQLException {
        
        for(Persona p: InscripcionesPersonas.mostrarInformacion()){
            System.out.println(p);
        }
        /*Persona p1 = new Persona(6, "camilo", "Franco", "camilof@gmail.com", "estudiante");
        Persona p2 = new Persona(7, "Esteban", "Villamizar", "EvillamizarqGmail.com", "Estudiante");
        Persona p3 = new Persona(7, "Juan", "Hernandez", "JHernandez@gmail.com", "Profesor");
       
        
        InscripcionesPersonas ip = new InscripcionesPersonas(new ArrayList<>());
        ip.inscribirPersona(p1);
        ip.inscribirPersona(p2);
        
        ip.eliminarPersona(6);
        ip.modificarPersona(p3);
        
        ip.guardarInformacion(p3);*/
        
    }
}
