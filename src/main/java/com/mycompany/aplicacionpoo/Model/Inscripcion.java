/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Model;

/**
 *
 * @author nicol
 */
public class Inscripcion {
    private Curso curso;
    private int año;
    private int semestre;
    private Estudiante estudiante;

    public Inscripcion(Curso curso, int año, int semestre, Estudiante estudiante) {
        this.curso = curso;
        this.año = año;
        this.semestre = semestre;
        this.estudiante = estudiante;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public int getAño() {
        return año;
    }

    public void setAño(int año) {
        this.año = año;
    }

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Inscripcion{curso='%s', año=%d, semestre=%d, estudiante='%s (%s)'}",
            curso.getNombre(), año, semestre,
            estudiante.getNombres() + " " + estudiante.getApellidos(),
            estudiante.getCodigo()
        );
    }

}
