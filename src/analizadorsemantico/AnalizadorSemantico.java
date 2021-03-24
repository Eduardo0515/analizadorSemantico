/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author Hugo Ruiz
 */
public class AnalizadorSemantico {

    private ArrayList lexemas;
    private ArrayList tokens;
    private Variable variable;
    private Funcion funcion;

    private int contadorEntrada;
    private int estadoActual;
    private Hashtable<String, Variable> idsMain = new Hashtable<>();
    private Hashtable<String, Funcion> funciones = new Hashtable<>();
    private ArrayList errores;

    public AnalizadorSemantico(ArrayList lexemas, ArrayList tokens) {
        this.lexemas = lexemas;
        this.tokens = tokens;
        errores = new ArrayList();
    }

    public void analizar() {
        String tipoDato = "";
        String variable = "";
        String contexto = "";
        int numParametro = 0;
        String nombreFuncion = "";
        ArrayList<Variable> parametros = new ArrayList<>();

        int estadoInicio = 0;
        estadoActual = estadoInicio;
        contadorEntrada = 0;
        boolean isFuncion = true;

        for (int i = 0; i < lexemas.size(); i++) {

            if (lexemas.get(contadorEntrada).equals("main")) {
                contexto = "main";
            } else if (lexemas.get(contadorEntrada).equals("fn")) {
                contexto = lexemas.get(contadorEntrada + 2).toString();
                estadoActual = 0;
                isFuncion = true;
            }

            if (estadoActual == 0) {
                if (tokens.get(contadorEntrada).equals("Tipo de dato") || lexemas.get(contadorEntrada).equals("void")) {
                    estadoActual = 1;
                    tipoDato = lexemas.get(contadorEntrada).toString();
                } else if (tokens.get(contadorEntrada).equals("Identificador")) {
                    estadoActual = 7;
                }
            } else if (estadoActual == 1) {
                if (tokens.get(contadorEntrada).equals("Identificador")) {
                    estadoActual = 2;
                }
                variable = lexemas.get(contadorEntrada).toString();
                if (contexto.equals("main")) {
                    if (idsMain.containsKey(variable)) {
                        errores.add("El identificador " + variable + " ha sido declarado previamente.");
                    } else {
                        idsMain.put(variable, new Variable(variable, tipoDato, "No inicializado"));
                    }
                }
            } else if (estadoActual == 2) {
                String lexemaActual = lexemas.get(contadorEntrada).toString();
                if (lexemaActual.equals(";")) {
                    estadoActual = 0;
                } else if (lexemaActual.equals("=")) {
                    estadoActual = 3;
                } else if (lexemaActual.equals("(")) {
                    estadoActual = 4;
                }

            } else if (estadoActual == 3) {
                String valor = tokens.get(contadorEntrada).toString();
                String valorLiteral = lexemas.get(contadorEntrada).toString();
                System.out.println(valorLiteral + " " + tipoDato);
                if (tipoDato.equals("int") && valor.equals("Entero") || tipoDato.equals("flt") && valor.equals("Decimal")
                        || tipoDato.equals("strg") && valor.equals("Cadena") || tipoDato.equals("bool") && valor.equals("Booleano")) {
                    idsMain.get(variable).setEstado("Inicializado");
                } else {
                    errores.add("El valor " + valorLiteral + " no es compatible con " + tipoDato);
                }
                estadoActual = 0;
            } else if (estadoActual == 4) {
                if (lexemas.get(contadorEntrada).equals(")")) {
                    estadoActual = 0;
                } else if (tokens.get(contadorEntrada).equals("Tipo de dato")) {
                    estadoActual = 5;
                    tipoDato = lexemas.get(contadorEntrada).toString();
                }
            } else if (estadoActual == 5) {
                estadoActual = 6;
            } else if (estadoActual == 6) {
                if (lexemas.get(contadorEntrada).equals(",")) {
                    estadoActual = 4;
                } else if (lexemas.get(contadorEntrada).equals(")")) {
                    estadoActual = 0;
                }
            } else if (estadoActual == 7) {
                if (lexemas.get(contadorEntrada).equals("(")) {
                    //Comprobar si hay una función declarada con el nombre especificado
                    nombreFuncion = lexemas.get(contadorEntrada - 1).toString();
                    if (funciones.containsKey(nombreFuncion)) {
                        parametros = funciones.get(nombreFuncion).getVariables();
                        numParametro = 0;
                        estadoActual = 8;
                    } else {
                        errores.add("La función " + nombreFuncion + " no existe");
                        estadoActual = 55;
                    }
                } else if (lexemas.get(contadorEntrada).equals("=")) {
                    //estadoActual = 9;
                } else if (tokens.get(contadorEntrada).equals("Comparacion")
                        || tokens.get(contadorEntrada - 2).equals("Comparacion")) {
                    //estadoActual = 10;
                } else if (lexemas.get(contadorEntrada).equals(";")) {
                    //estadoActual = 11;
                }
            } else if (estadoActual == 8) {
                if (tokens.get(contadorEntrada).equals("Identificador")) {
                    Variable variableParametro = new Variable();
                    boolean isExistente = false;
                    if (contexto.equals("main")) {                      
                        if (idsMain.containsKey(lexemas.get(contadorEntrada))) {
                            variableParametro = idsMain.get(lexemas.get(contadorEntrada));
                            isExistente = true;
                        } else {
                            errores.add("1El identificador " + lexemas.get(contadorEntrada) + " no está declarado en la función " + contexto);
                        }
                    } else {
                        if (isVariableExistente(funciones.get(contexto).getVariables(), lexemas.get(contadorEntrada).toString())) {
                            variableParametro = getVariable(funciones.get(contexto).getVariables(), lexemas.get(contadorEntrada).toString());
                            isExistente = true;
                        } else {
                            errores.add("2El identificador " + lexemas.get(contadorEntrada) + " no está declarado en la función " + contexto);
                        }
                    }
                    if (isExistente) {
                        System.out.println(parametros.size() +" "+numParametro);
                        System.out.println(lexemas.get(contadorEntrada));
                        System.out.println(lexemas.get(contadorEntrada-1));
                        if (parametros.size() >= numParametro + 1) {
                            if (!parametros.get(numParametro).getTipo().equals(variableParametro.getTipo())) {
                                errores.add("El tipo del identificador " + variableParametro.getNombre() + " no coincide con el tipo especificado en los parámetros de la función " + nombreFuncion);
                            }
                        } else {
                            errores.add("1No se esperaba el argumento " + lexemas.get(contadorEntrada) + " en la función " + nombreFuncion);
                        }
                    }
                } else if (tokens.get(contadorEntrada).equals("Entero")) {
                    if (parametros.size() >= numParametro + 1) {
                        if (!parametros.get(numParametro).getTipo().equals("int")) {
                            errores.add("El número entero " + lexemas.get(contadorEntrada) + " no coincide con el tipo especificado en los parámetros de la función " + nombreFuncion);
                        }
                    } else {
                        errores.add("2No se esperaba el argumento " + lexemas.get(contadorEntrada) + " en la función " + nombreFuncion);
                    }
                } else if (tokens.get(contadorEntrada).equals("Decimal")) {
                    if (parametros.size() >= numParametro + 1) {
                        if (!parametros.get(numParametro).getTipo().equals("flt")) {
                            errores.add("El número decimal " + lexemas.get(contadorEntrada) + " no coincide con el tipo especificado en los parámetros de la función " + nombreFuncion);
                        }
                    } else {
                        errores.add("3No se esperaba el argumento " + lexemas.get(contadorEntrada) + " en la función " + nombreFuncion);
                    }
                } else if (tokens.get(contadorEntrada).equals("Cadena")) {
                    if (parametros.size() >= numParametro + 1) {
                        if (!parametros.get(numParametro).getTipo().equals("strg")) {
                            errores.add("La cadena " + lexemas.get(contadorEntrada) + " no coincide con el tipo especificado en los parámetros de la función " + nombreFuncion);
                        }
                    } else {
                        errores.add("4No se esperaba el argumento " + lexemas.get(contadorEntrada) + " en la función " + nombreFuncion);
                    }
                } else if (tokens.get(contadorEntrada).equals("Booleano")) {
                    if (parametros.size() >= numParametro + 1) {
                        if (!parametros.get(numParametro).getTipo().equals("bool")) {
                            errores.add("El valor booleano " + lexemas.get(contadorEntrada) + " no coincide con el tipo especificado en los parámetros de la función " + nombreFuncion);
                        }
                    } else {
                        errores.add("5No se esperaba el argumento " + lexemas.get(contadorEntrada) + " en la función " + nombreFuncion);
                    }
                } else if (lexemas.get(contadorEntrada).equals(")")) {
                    if (parametros.size() > numParametro) {
                        errores.add("La función " + nombreFuncion + " espera más argumentos");
                    }
                    estadoActual = 0;
                }
                numParametro++;
            } else if (estadoActual == 9) {
                if (contexto.equals("main")) {
                    System.out.println("En main");
                    String variableMain = lexemas.get(contadorEntrada - 2).toString();
                   // if () {

                    //  }
                } else {
                    ArrayList<Variable> variablesFuncion = funciones.get(contexto).getVariables();
                    System.out.println("En funcion " + contexto);

                }
            } else if (estadoActual == 10) {

            } else if (estadoActual == 11) {

            } else if (estadoActual == 55) {
                if (lexemas.get(contadorEntrada).equals(")")) {
                    contadorEntrada = 0;
                }
            }
            contadorEntrada++;
        }

        System.out.println(errores);
        /* ArrayList<Variable> test = funciones.get("funcion").getVariables();
         for(Variable var: test){
         System.out.println(var.getNombre());
         }*/
    }

    public void addFunciones() {
        String contexto = "";
        String tipoDato = "";
        String variable = "";

        int estadoInicio = 0;
        boolean isFuncion = true;
        estadoActual = estadoInicio;
        contadorEntrada = 0;

        for (int i = 0; i < lexemas.size(); i++) {

            if (lexemas.get(contadorEntrada).equals("main")) {
                contexto = "main";
            } else if (lexemas.get(contadorEntrada).equals("fn")) {
                contexto = lexemas.get(contadorEntrada + 2).toString();
                estadoActual = 0;
                isFuncion = true;
            }

            if (estadoActual == 0) {
                if (!contexto.equals("main")) {
                    if (tokens.get(contadorEntrada).equals("Tipo de dato") || lexemas.get(contadorEntrada).equals("void")) {
                        estadoActual = 1;
                        tipoDato = lexemas.get(contadorEntrada).toString();
                    }
                }
            } else if (estadoActual == 1) {
                if (tokens.get(contadorEntrada).equals("Identificador")) {
                    estadoActual = 2;
                }
                variable = lexemas.get(contadorEntrada).toString();
                if (isFuncion) {
                    if (funciones.containsKey(variable)) {
                        errores.add("La función " + variable + " ha sido declarada previamente.");
                    } else {
                        funcion = new Funcion(tipoDato);
                        funciones.put(variable, funcion);
                    }
                    isFuncion = false;
                } else {
                    if (isVariableExistente(funciones.get(contexto).getVariables(), variable)) {
                        errores.add("El identificador " + variable + " ha sido declarado previamente.");
                    } else {
                        funciones.get(contexto).setVariableInd(new Variable(variable, tipoDato, "Inicializado"));
                    }
                }

            } else if (estadoActual == 2) {
                String lexemaActual = lexemas.get(contadorEntrada).toString();
                if (lexemaActual.equals("(")) {
                    estadoActual = 4;
                } else {
                    estadoActual = 0;
                }

            } else if (estadoActual == 4) {
                if (lexemas.get(contadorEntrada).equals(")")) {
                    estadoActual = 0;
                } else if (tokens.get(contadorEntrada).equals("Tipo de dato")) {
                    estadoActual = 5;
                    tipoDato = lexemas.get(contadorEntrada).toString();
                }
            } else if (estadoActual == 5) {
                String parametro = lexemas.get(contadorEntrada).toString();
                if (isVariableExistente(funciones.get(variable).getVariables(), parametro)) {
                    errores.add("El identificador " + parametro + " ha sido declarado previamente.");
                } else {
                    funciones.get(variable).setVariableInd(new Variable(parametro, tipoDato, "Inicializado"));
                }
                estadoActual = 6;
            } else if (estadoActual == 6) {
                if (lexemas.get(contadorEntrada).equals(",")) {
                    estadoActual = 4;
                } else if (lexemas.get(contadorEntrada).equals(")")) {
                    estadoActual = 0;
                }
            }
            contadorEntrada++;
        }

        for (Funcion var : funciones.values()) {
            System.out.println(var.getTipo());
        }
        System.out.println("");
    }

    private boolean isVariableExistente(ArrayList<Variable> variables, String parametro) {
        boolean isExistente = false;
        for (Variable var : variables) {
            if (var.getNombre().equals(parametro)) {
                isExistente = true;
            }
        }
        return isExistente;
    }

    private Variable getVariable(ArrayList<Variable> variables, String parametro) {
        Variable varEncontrada = new Variable();
        for (Variable var : variables) {
            if (var.getNombre().equals(parametro)) {
                varEncontrada = var;
            }
        }
        return varEncontrada;
    }

    private String getTipoVariable() {
        return "";
    }

}