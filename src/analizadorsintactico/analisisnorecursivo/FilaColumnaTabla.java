/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsintactico.analisisnorecursivo;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Hugo Ruiz
 */
public class FilaColumnaTabla {

    private Hashtable<String, Integer> noTerminales = new Hashtable<>();
    private Hashtable<String, Integer> terminales = new Hashtable<>();

    public FilaColumnaTabla() {
        noTerminales.put("GRAMATICAGENERAL", 1);
        noTerminales.put("CUERPOPROGRAMA", 2);
        noTerminales.put("FUNCIONES", 3);
        noTerminales.put("FNPRINCIPAL", 4);
        noTerminales.put("CUERPO", 5);
        noTerminales.put("CONTENIDO", 6);
        noTerminales.put("DECLARACIONASIG", 7);
        noTerminales.put("TIPODATO", 8);
        noTerminales.put("IDENTIFICADOR", 9);
        noTerminales.put("DECLARACION", 10);
        noTerminales.put("VALOR", 11);
        noTerminales.put("BOOLEANO", 12);
        noTerminales.put("CIFRA", 13);
        noTerminales.put("CADENA", 14);
        noTerminales.put("SENTENCIAWHILE", 15);
        noTerminales.put("CONTENIDOESTRUCTURA", 16);
        noTerminales.put("CONDICION", 17);
        noTerminales.put("EXPRESIONES", 18);
        noTerminales.put("ELEMENTO", 19);
        noTerminales.put("OPERADOR", 20);
        noTerminales.put("OPCIONAL", 21);
        noTerminales.put("LOGICO", 22);
        noTerminales.put("SENTENCIAIF", 23);
        noTerminales.put("ELSE", 24);
        noTerminales.put("FUNCION", 25);
        noTerminales.put("TIPORETORNO", 26);
        noTerminales.put("PARAMETRO", 27);
        noTerminales.put("ADICION", 28);
        noTerminales.put("CUERPOFUNCION", 29);
        noTerminales.put("RETORNO", 30);
        noTerminales.put("INVOCACIONFUNCION", 31);
        noTerminales.put("ARGUMENTO", 32);
        noTerminales.put("RESTO", 33);
        noTerminales.put("ENTRADA", 34);
        noTerminales.put("IMPRIMIR", 35);
        noTerminales.put("ELEMENTOIMPRIMIR", 36);
        noTerminales.put("CONCATENACION", 37);
        noTerminales.put("RESTOINVOCACION", 38);
        noTerminales.put("ASIGNACION", 39);
        noTerminales.put("RESTOASIGNACION", 40);

        terminales.put("program", 1);
        terminales.put("{", 2);
        terminales.put("fn", 3);
        terminales.put("}", 4);
        terminales.put("main", 5);
        terminales.put("(", 6);
        terminales.put(")", 7);
        terminales.put("$", 8);
        terminales.put("int", 9);
        terminales.put("strg", 9);
        terminales.put("bool", 9);
        terminales.put("flt", 9);
        terminales.put("Identificador", 10);
        terminales.put("Entero", 11);
        terminales.put(";", 12);
        terminales.put("=", 13);
        terminales.put("true", 14);
        terminales.put("false", 14);
        terminales.put("Decimal", 15);
        terminales.put("Cadena", 16);
        terminales.put("while", 17);
        terminales.put("<", 18);
        terminales.put(">", 18);
        terminales.put("==", 18);
        terminales.put("!=", 18);
        terminales.put("<=", 18);
        terminales.put(">=", 18);
        terminales.put("&", 19);
        terminales.put("|", 19);
        terminales.put("if", 20);
        terminales.put("else", 21);
        terminales.put("void", 22);
        terminales.put(",", 23);
        terminales.put("return", 24);
        terminales.put("inp", 25);
        terminales.put("out", 26);
        terminales.put("+", 27);

    }

    public boolean isTerminal(String elemento) {
        if (noTerminales.containsKey(elemento)) {
            return false;
        } else {
            return true;
        }
    }

    public Hashtable<String, Integer> getNoTerminals() {
        return noTerminales;
    }

    public Hashtable<String, Integer> getTerminals() {
        return terminales;
    }

}
