/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorlexico.diccionarioautomata;

/**
 *
 * @author Hugo Ruiz
 */
public class Reservada {

    private String resultado;
    private String[][] palabras = {//palabras reservadas
        {"program", "Palabra reservada para iniciar programa"},
        {"if", "Evaluar una expresión condicional"},
        {"else", "Valor de default con la sentencia if"},
        {"while", "Estructura repetitiva"},
        {"out", "Imprimir por pantalla"},
        {"main", "Nombre método principal"},
        {"inp", "Entrada de datos"},
        {"fn", "Declaración de función"},
        {"void", "Sin retorno"},
        {"return", "Retorno de valor"}
    };

    private String[][] tiposDatos = {
        {"int", "Variables de tipo entero"}, //tipos de datos
        {"flt", "Variables decimales "},
        {"strg", "Variables de cadenas de Caracteres"},
        {"bool", "Variables de valor true o false"},};

    public void evaluarCadena(String cadena) {
        resultado = "No encontrado";
        for (String[] palabra : palabras) {
            if (cadena.equals(palabra[0])) {
                resultado = "Palabra reservada";
            }
        }

        for (String[] tiposDato : tiposDatos) {
            if (cadena.equals(tiposDato[0])) {
                resultado = "Tipo de dato";
            }
        }
    }

    public String getResultado() {
        return resultado;
    }

    public String[][] palabrasReservadas() {
        return palabras;
    }

    public String[][] tipoDatos() {
        return tiposDatos;
    }
}
