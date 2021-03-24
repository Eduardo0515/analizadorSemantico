/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorlexico.principal;

import analizadorlexico.diccionarioautomata.Reservada;
import analizadorlexico.diccionarioautomata.Diccionario;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import analizadorlexico.modelo.Lexico;

/**
 *
 * @author Hugo Ruiz
 */
public class AutomataPrincipal {

    Reservada reservadas;
    ObservableList<Lexico> tokensLexema;
    ArrayList listElements;
    ArrayList significados;
    ArrayList listElementosLiterales;
    ArrayList indefinidos;
    Diccionario diccionario;
    Lexico lexico;
    String aux;

    int contadorEntrada;
    char arrayDatos[];
    int estadoActual;

    public void analizar(String entrada) {
        reservadas = new Reservada();
        tokensLexema = FXCollections.observableArrayList();
        listElements = new ArrayList();
        listElementosLiterales = new ArrayList();
        significados = new ArrayList();
        indefinidos = new ArrayList();
        diccionario = new Diccionario();
        aux = "";
        arrayDatos = entrada.toCharArray();
        int estadoInicio = 0;
        boolean fin = false;
        estadoActual = estadoInicio;
        contadorEntrada = 0;

        while (fin == false) {
            if (contadorEntrada > arrayDatos.length - 1) {
                fin = true;
                break;
            }

            if (estadoActual == 0) {
                if (arrayDatos[contadorEntrada] == '+') {
                    addLista("+");
                } else if (arrayDatos[contadorEntrada] == '&') {
                    addLista("&");
                } else if (arrayDatos[contadorEntrada] == '|') {
                    addLista("|");
                } else if (arrayDatos[contadorEntrada] == '(') {
                    addLista("(");
                } else if (arrayDatos[contadorEntrada] == ')') {
                    addLista(")");
                } else if (arrayDatos[contadorEntrada] == '{') {
                    addLista("{");
                } else if (arrayDatos[contadorEntrada] == '}') {
                    addLista("}");
                } else if (arrayDatos[contadorEntrada] == '=') {
                    concatenarEvaluar(22);
                } else if (arrayDatos[contadorEntrada] == '!') {
                    concatenarEvaluar(22);
                } else if (arrayDatos[contadorEntrada] == '>') {
                    concatenarEvaluar(22);
                } else if (arrayDatos[contadorEntrada] == '<') {
                    concatenarEvaluar(22);
                } else if (arrayDatos[contadorEntrada] == '"') {
                    concatenarEvaluar(15);
                } else if (arrayDatos[contadorEntrada] == ';') {
                    addLista(";");
                } else if (arrayDatos[contadorEntrada] == ',') {
                    addLista(",");
                } else if (Character.isDigit(arrayDatos[contadorEntrada])) {
                    concatenarEvaluar(17);
                } else if (Character.isLetter(arrayDatos[contadorEntrada])) {
                    concatenarEvaluar(19);
                } else if (arrayDatos[contadorEntrada] == ' ' || arrayDatos[contadorEntrada] == '\t' || arrayDatos[contadorEntrada] == '\n') {
                } else {
                    concatenarEvaluar(999);
                }
                contadorEntrada++;
                continue;
            }

            if (estadoActual == 15) {
                if (arrayDatos[contadorEntrada] == '"') {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                } else {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    isFinalCadena();
                }
                continue;
            }

            if (estadoActual == 17) {
                if (Character.isDigit(arrayDatos[contadorEntrada])) {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    isFinalCadena();
                } else if (arrayDatos[contadorEntrada] == '.') {
                    aux += arrayDatos[contadorEntrada];
                    estadoActual = 18;
                    contadorEntrada++;
                    isFinalCadena();
                } else {
                    evaluarIsAceptado();
                }
                continue;
            }

            if (estadoActual == 18) {
                if (Character.isDigit(arrayDatos[contadorEntrada])) {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    isFinalCadena();
                } else {
                    evaluarIsAceptado();
                }
                continue;
            }

            if (estadoActual == 19) {
                if (Character.isLetterOrDigit(arrayDatos[contadorEntrada])) {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    isFinalCadena();
                } else {
                    evaluarIsAceptado();
                }
                continue;
            }

            if (estadoActual == 20) {
                if (arrayDatos[contadorEntrada] == '/') {
                    aux += arrayDatos[contadorEntrada];
                    estadoActual = 21;
                    contadorEntrada++;
                    isFinalCadena();
                } else {
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                }
                continue;
            }

            if (estadoActual == 21) {
                if (arrayDatos[contadorEntrada] == '\n') {
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                    contadorEntrada++;
                } else {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    isFinalCadena();
                }
                continue;
            }

            if (estadoActual == 22) {
                if (arrayDatos[contadorEntrada] == '=') {
                    aux += arrayDatos[contadorEntrada];
                    contadorEntrada++;
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                } else {
                    addLista(aux);
                    aux = "";
                    estadoActual = 0;
                }
                continue;
            }

            if (estadoActual == 999) {
                evaluarIsAceptado();
            }
        }
    }

    public ArrayList getElementos() {
        return listElements;
    }

    public ArrayList getElementosLiterales() {
        return listElementosLiterales;
    }
    
    public ArrayList getSignificados() {
        return significados;
    }

    public ObservableList getTokensLexema() {
        return tokensLexema;
    }

    public void addLista(String cadena) {
        String significado = "";
        boolean isEncontrado = false;
        if (estadoActual == 19) {
            reservadas.evaluarCadena(cadena);
            if (!reservadas.getResultado().equals("No encontrado")) {
                significado = reservadas.getResultado();
                isEncontrado = true;
            }
        }
        if (!isEncontrado) {
            significado = diccionario.getDescriptionOf(cadena);
            if (significado.equals("Indefinido")) {
                indefinidos.add(cadena);
            }
        }
        if(significado.equals("Identificador") || significado.equals("Entero") ||
            significado.equals("Decimal") || significado.equals("Cadena")){
            listElements.add(significado);
        }else{
            listElements.add(cadena);
        }
        listElementosLiterales.add(cadena);
        significados.add(significado);
        lexico = new Lexico(cadena, significado);
        tokensLexema.add(lexico);
    }

    public void concatenarEvaluar(int numEstadoActual) {
        aux += arrayDatos[contadorEntrada];
        estadoActual = numEstadoActual;
        if (contadorEntrada + 1 == arrayDatos.length) {
            addLista(aux);
            aux = "";
            estadoActual = 0;
        }
    }

    public void evaluarIsAceptado() {
        if (arrayDatos[contadorEntrada] == ' ' || arrayDatos[contadorEntrada] == '\t' || arrayDatos[contadorEntrada] == '\n') {
            addLista(aux);
            aux = "";
            estadoActual = 0;
            contadorEntrada++;
        } else if (String.valueOf(arrayDatos[contadorEntrada]).matches("[=+;&|!(){}<>,]")) {
            addLista(aux);
            aux = "";
            estadoActual = 0;
        } else {
            aux += arrayDatos[contadorEntrada];
            estadoActual = 999;
            contadorEntrada++;
            isFinalCadena();
        }
    }

    public void isFinalCadena() {
        if (contadorEntrada == arrayDatos.length) {
            addLista(aux);
            aux = "";
            estadoActual = 0;
            contadorEntrada++;
        }
    }

    public ArrayList getIndefinidos() {
        return indefinidos;
    }
}
