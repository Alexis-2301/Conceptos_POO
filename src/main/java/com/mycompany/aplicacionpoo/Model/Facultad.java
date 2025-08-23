/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Model;

/**
 *
 * @author nicol
 */
public class Facultad {
    private double id;
    private String nombre;
    private Persona decano;

    public Facultad(double id, String nombre, Persona decano) {
        this.id = id;
        this.nombre = nombre;
        this.decano = decano;
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
    
    @Override
    public String toString() {
        return String.format(
            "Facultad{id=%.0f, nombre='%s', decano=%s}",
            id, nombre, decano
        );
    }
}
