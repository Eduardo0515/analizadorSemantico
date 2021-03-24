/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorlexico.modelo;

/**
 *
 * @author Hugo Ruiz
 */
public class Lexico {
    private String cadena;
    private String significado;

    public Lexico(String cadena, String significado) {
        this.cadena = cadena;
        this.significado = significado;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public String getSignificado() {
        return significado;
    }

    public void setSignificado(String significado) {
        this.significado = significado;
    }
}

