/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.aplicacionpoo;


import com.mycompany.aplicacionpoo.Model.Curso;
import com.mycompany.aplicacionpoo.Model.CursoProfesor;
import com.mycompany.aplicacionpoo.Model.CursosInscritos;
import com.mycompany.aplicacionpoo.Model.CursosProfesores;
import com.mycompany.aplicacionpoo.Model.Estudiante;
import com.mycompany.aplicacionpoo.Model.Facultad;
import com.mycompany.aplicacionpoo.Model.Inscripcion;
import com.mycompany.aplicacionpoo.Model.InscripcionesPersonas;
import com.mycompany.aplicacionpoo.Model.Persona;
import com.mycompany.aplicacionpoo.Model.Profesor;
import com.mycompany.aplicacionpoo.Model.Programa;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;




/**
 *
 * @author nicol
 */
//Nicolás Orejuela Hernández 160004899 - Edward Alexis Murcia Rodriguez 160004825
public class AplicacionPOO {

    public static void main(String[] args) throws SQLException {
        Date fecha = Date.valueOf("1988-08-23");
        
        Persona p1 = new Persona(8, "Santiago", "Hernandez", "SH@gmail.com", "Profesor");
        Profesor p3 = new Profesor(9, "Juan", "Hernandez", "JHernandez@gmail.com", "Catedratico");
        
        
        Facultad f1 = new Facultad(1, "Ingenieria", p1);
        
        Programa pr1 = new Programa(1, "Ing Sistemas", 10, fecha, f1 );
        
        Estudiante e1 = new Estudiante(1, "Alenadro", "Fino", "AF@gmail.com", 001, pr1,true, 38.3);
        Estudiante e2 = new Estudiante(2, "Valentina", "Rodriguez", "valentinaR@gmail.com", 002, pr1, true, 41.2);
        Estudiante e3 = new Estudiante(3, "Esteban", "Villamizar", "EvillamizarqGmail.com", 003, pr1, true, 35.6);
        Estudiante e4 = new Estudiante(4, "camilo", "Franco", "camilof@gmail.com", 004, pr1, true, 36.2);
        
        Curso c1 = new Curso(1, "Programacion 1", pr1, true);
        
        Inscripcion i1 = new Inscripcion(c1, 2023, 1, e1);
        Inscripcion i2 = new Inscripcion(c1, 2023, 1, e2);
        Inscripcion i3 = new Inscripcion(c1, 2023, 1, e3);
        Inscripcion i4 = new Inscripcion(c1, 2023, 1, e4);  
       
        CursosInscritos ci = new CursosInscritos(new ArrayList<>());
        
        ci.inscribirCursos(i1);
        ci.inscribirCursos(i2);
        ci.inscribirCursos(i3);
        ci.inscribirCursos(i4);

        System.out.println(ci.toString());
        
        CursoProfesor cp1 = new CursoProfesor(p3, 2023, 1, c1);
        
        CursosProfesores cp = new CursosProfesores(new ArrayList<>());
        
        cp.inscribirCursoProfesor(cp1);
        System.out.println(cp.toString());
        
        InscripcionesPersonas ip = new InscripcionesPersonas(new ArrayList<>());
        ip.inscribirPersona(p1);
        ip.inscribirPersona(p3);
        
        System.out.println(ip.toString());
       
        
        //Mostrar desde el base de datos lista de InscripcionesPersonas
        /*for(Persona p: InscripcionesPersonas.mostrarInformacion()){
            System.out.println(p);
        }*/
        
    }
}
