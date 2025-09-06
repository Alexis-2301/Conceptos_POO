/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aplicacionpoo.Model;

/**
 *
 * @author nicol
 */
public class Profesor extends Persona{
    
    private String tipoContrato;

    public Profesor(double id, String nombres, String apellidos, String email, String tipoContrato) {
        super(id, nombres, apellidos, email, "profesor");
        this.tipoContrato = tipoContrato;
    }

    public Profesor() {
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    @Override
    public String toString() {
        return String.format(
            "Profesor{%s, tipoContrato='%s'}",
            super.toString(), tipoContrato
        );
    }
}
