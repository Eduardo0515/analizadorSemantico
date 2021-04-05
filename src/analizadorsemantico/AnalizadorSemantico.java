/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

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
                } else if (lexemas.get(contadorEntrada).equals("return")) {
                    estadoActual = 11;
                } else if (lexemas.get(contadorEntrada).equals("inp")) {
                    estadoActual = 15;
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
                } else {
                    if (isVariableExistente(funciones.get(contexto).getVariables(), variable)) {
                        errores.add("El identificador " + variable + " ha sido declarado previamente.");
                    } else {
                        funciones.get(contexto).setVariableInd(new Variable(variable, tipoDato, "No inicializado"));
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
                if (contexto.equals("main")) {
                    if (valor.equals("Identificador")) {
                        if (idsMain.containsKey(valorLiteral)) {
                            if (idsMain.get(valorLiteral).getEstado().equals("Inicializado")) {
                                if (!idsMain.get(valorLiteral).getTipo().equals(tipoDato)) {
                                    errores.add("El tipo del identificador " + valorLiteral + " no coincide con " + tipoDato);
                                }
                            } else {
                                errores.add("El identificador " + valorLiteral + " no ha sido inicializado");
                            }
                        } else {
                            errores.add("El identificador " + valorLiteral + " no se ha declarado en main");
                        }
                    } else if (tipoDato.equals("int") && valor.equals("Entero") || tipoDato.equals("flt") && valor.equals("Decimal")
                            || tipoDato.equals("strg") && valor.equals("Cadena") || tipoDato.equals("bool") && valor.equals("Booleano")) {
                        idsMain.get(variable).setEstado("Inicializado");
                    } else {
                        errores.add("El valor " + valorLiteral + " no es compatible con " + tipoDato);
                    }
                } else {
                    if (valor.equals("Identificador")) {
                        if (isVariableExistente(funciones.get(contexto).getVariables(), valorLiteral)) {
                            Variable variablee = getVariable(funciones.get(contexto).getVariables(), valorLiteral);
                            if (variablee.getEstado().equals("Inicializado")) {
                                if (!variablee.getTipo().equals(tipoDato)) {
                                    errores.add("El tipo del identificador " + valorLiteral + " no coincide con " + tipoDato);
                                }else{
                                    int indicee = getIndiceVariable(funciones.get(contexto).getVariables(), variable);
                                    funciones.get(contexto).getVariables().get(indicee).setEstado("Inicializado");
                                }
                            } else {
                                errores.add("El identificador " + valorLiteral + " no ha sido inicializado");
                            }
                        } else {
                            errores.add("El identificador " + valorLiteral + " no se ha declarado en la función " + contexto);
                        }
                    } else if (tipoDato.equals("int") && valor.equals("Entero") || tipoDato.equals("flt") && valor.equals("Decimal")
                            || tipoDato.equals("strg") && valor.equals("Cadena") || tipoDato.equals("bool") && valor.equals("Booleano")) {
                        int indice = getIndiceVariable(funciones.get(contexto).getVariables(), variable);
                        funciones.get(contexto).getVariables().get(indice).setEstado("Inicializado");
                    } else {
                        errores.add("El valor " + valorLiteral + " no es compatible con " + tipoDato + " en la función " + contexto);
                    }
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
                        parametros = funciones.get(nombreFuncion).getParametros();
                        numParametro = 0;
                        estadoActual = 8;
                    } else {
                        errores.add("La función " + nombreFuncion + " no existe");
                        estadoActual = 55;
                    }
                } else if (lexemas.get(contadorEntrada).equals("=")) {
                    if (lexemas.get(contadorEntrada + 2).equals("+")) {
                        estadoActual = 9;
                    } else if (lexemas.get(contadorEntrada + 2).equals("(")) {
                        estadoActual = 14;
                    }
                } else if (tokens.get(contadorEntrada).equals("Comparacion")
                        || tokens.get(contadorEntrada - 2).equals("Comparacion")) {
                    if (contexto.equals("main")) {
                        if (idsMain.containsKey(lexemas.get(contadorEntrada - 1))) {
                            if (!idsMain.get(lexemas.get(contadorEntrada - 1)).getTipo().equals("int")) {
                                errores.add("El tipo del identificador " + lexemas.get(contadorEntrada - 1) + " debe ser entero");
                            } else {
                                if (idsMain.get(lexemas.get(contadorEntrada - 1)).getEstado().equals("No inicializado")) {
                                    errores.add("El identificador " + lexemas.get(contadorEntrada - 1) + " no ha sido inicializado");
                                }
                            }
                        } else {
                            errores.add("El identificador " + lexemas.get(contadorEntrada - 1) + " no ha sido declarado");
                        }
                    } else {
                        if (isVariableExistente(funciones.get(contexto).getVariables(), lexemas.get(contadorEntrada - 1).toString())) {
                            Variable vari = getVariable(funciones.get(contexto).getVariables(), lexemas.get(contadorEntrada - 1).toString());
                            if (!vari.getTipo().equals("int")) {
                                errores.add("El tipo del identificador " + lexemas.get(contadorEntrada - 1) + " debe ser entero");
                            } else {
                                if (vari.getEstado().equals("No inicializado")) {
                                    errores.add("El identificador " + vari.getNombre() + " no ha sido inicializado");
                                }
                            }
                        } else {
                            errores.add("2El identificador " + lexemas.get(contadorEntrada - 1) + " no está declarado en la función " + contexto);
                        }
                    }
                    estadoActual = 0;
                } else if (lexemas.get(contadorEntrada).equals(";")) {
                    //estadoActual = 11;
                } else {
                    if (contexto.equals("main")) {
                        if (idsMain.containsKey(lexemas.get(contadorEntrada - 1))) {
                            if (idsMain.get(lexemas.get(contadorEntrada - 1)).getEstado().equals("No inicializado")) {
                                errores.add("El identificador " + lexemas.get(contadorEntrada - 1) + " no ha sido inicializado");
                            }
                        } else {
                            errores.add("El identificador " + lexemas.get(contadorEntrada - 1) + " no ha sido declarado");
                        }
                    } else {
                        if (isVariableExistente(funciones.get(contexto).getVariables(), lexemas.get(contadorEntrada - 1).toString())) {
                            Variable vari = getVariable(funciones.get(contexto).getVariables(), lexemas.get(contadorEntrada - 1).toString());
                            if (vari.getEstado().equals("No inicializado")) {
                                errores.add("El identificador " + vari.getNombre() + " no ha sido inicializado");
                            }
                        } else {
                            errores.add("oEl identificador " + lexemas.get(contadorEntrada - 1) + " no está declarado en la función " + contexto);
                        }
                    }
                    estadoActual = 0;
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
                        if (parametros.size() >= numParametro + 1) {
                            if (!parametros.get(numParametro).getTipo().equals(variableParametro.getTipo())) {
                                errores.add("El tipo del identificador " + variableParametro.getNombre() + " no coincide con el tipo especificado en los parámetros de la función " + nombreFuncion);
                            } else {
                                if (variableParametro.getEstado().equals("No inicializado")) {
                                    errores.add("El identificador " + variableParametro.getNombre() + " no ha sido inicializado");
                                }
                            }
                        } else {
                            errores.add("1No se esperaba el argumento " + lexemas.get(contadorEntrada) + " en la función " + nombreFuncion);
                        }
                    }
                    numParametro++;
                } else if (tokens.get(contadorEntrada).equals("Entero")) {
                    if (parametros.size() >= numParametro + 1) {
                        if (!parametros.get(numParametro).getTipo().equals("int")) {
                            errores.add("El número entero " + lexemas.get(contadorEntrada) + " no coincide con el tipo especificado en los parámetros de la función " + nombreFuncion);
                        }
                    } else {
                        errores.add("2No se esperaba el argumento " + lexemas.get(contadorEntrada) + " en la función " + nombreFuncion);
                    }
                    numParametro++;
                } else if (tokens.get(contadorEntrada).equals("Decimal")) {
                    if (parametros.size() >= numParametro + 1) {
                        if (!parametros.get(numParametro).getTipo().equals("flt")) {
                            errores.add("El número decimal " + lexemas.get(contadorEntrada) + " no coincide con el tipo especificado en los parámetros de la función " + nombreFuncion);
                        }
                    } else {
                        errores.add("3No se esperaba el argumento " + lexemas.get(contadorEntrada) + " en la función " + nombreFuncion);
                    }
                    numParametro++;
                } else if (tokens.get(contadorEntrada).equals("Cadena")) {
                    if (parametros.size() >= numParametro + 1) {
                        if (!parametros.get(numParametro).getTipo().equals("strg")) {
                            errores.add("La cadena " + lexemas.get(contadorEntrada) + " no coincide con el tipo especificado en los parámetros de la función " + nombreFuncion);
                        }
                    } else {
                        errores.add("4No se esperaba el argumento " + lexemas.get(contadorEntrada) + " en la función " + nombreFuncion);
                    }
                    numParametro++;
                } else if (tokens.get(contadorEntrada).equals("Booleano")) {
                    if (parametros.size() >= numParametro + 1) {
                        if (!parametros.get(numParametro).getTipo().equals("bool")) {
                            errores.add("El valor booleano " + lexemas.get(contadorEntrada) + " no coincide con el tipo especificado en los parámetros de la función " + nombreFuncion);
                        }
                    } else {
                        errores.add("5No se esperaba el argumento " + lexemas.get(contadorEntrada) + " en la función " + nombreFuncion);
                    }
                    numParametro++;
                } else if (lexemas.get(contadorEntrada).equals(")")) {
                    if (parametros.size() > numParametro) {
                        errores.add("La función " + nombreFuncion + " espera más argumentos");
                    }
                    estadoActual = 0;
                }
                /*if (!lexemas.get(contadorEntrada).equals(",")) {
                 numParametro++;
                 }*/
            } else if (estadoActual == 9) {
                String nombreVar = lexemas.get(contadorEntrada - 2).toString();
                if (contexto.equals("main")) {
                    //Verificar el primer identificador
                    if (idsMain.containsKey(nombreVar)) {
                        if (!idsMain.get(nombreVar).getTipo().equals("int")) {
                            errores.add("El identificador " + nombreVar + " debe ser de tipo entero");
                        }
                    } else {
                        errores.add("El identificador " + nombreVar + " no está declarado en el main");
                    }
                    //Verificar el segundo identificador
                    if (idsMain.containsKey(lexemas.get(contadorEntrada))) {
                        if (idsMain.get(lexemas.get(contadorEntrada)).getTipo().equals("int")) {
                            if (!idsMain.get(lexemas.get(contadorEntrada)).getEstado().equals("Inicializado")) {
                                errores.add("El identificador " + lexemas.get(contadorEntrada) + " no ha sido inicializado");
                            } else {
                                idsMain.get(nombreVar).setEstado("Inicializado");
                            }
                        } else {
                            errores.add("El identificador " + lexemas.get(contadorEntrada) + " debe ser de tipo entero");
                        }
                    } else {
                        errores.add("El identificador " + lexemas.get(contadorEntrada) + " no está declarado en el main");
                    }
                } else {
                    //verificar el primer identificador
                    if (isVariableExistente(funciones.get(contexto).getVariables(), nombreVar)) {
                        Variable vari = getVariable(funciones.get(contexto).getVariables(), nombreVar);
                        if (!vari.getTipo().equals("int")) {
                            errores.add("El identificador " + nombreVar + " debe ser de tipo entero");
                        }
                    } else {
                        errores.add("El identificador " + nombreVar + " no está declarado en la función " + contexto);
                    }
                    //Verificar el segundo identificador
                    if (isVariableExistente(funciones.get(contexto).getVariables(), lexemas.get(contadorEntrada).toString())) {
                        Variable vari2 = getVariable(funciones.get(contexto).getVariables(), lexemas.get(contadorEntrada).toString());
                        if (vari2.getTipo().equals("int")) {
                            if (!vari2.getEstado().equals("Inicializado")) {
                                errores.add("El identificador " + lexemas.get(contadorEntrada) + " no ha sido inicializado");
                            } else {
                                int indiice = getIndiceVariable(funciones.get(contexto).getVariables(), nombreVar);
                                funciones.get(contexto).getVariables().get(indiice).setEstado("Inicializado");
                            }
                        } else {
                            errores.add("El identificador " + lexemas.get(contadorEntrada) + " debe ser de tipo entero");
                        }
                    } else {
                        errores.add("El identificador " + lexemas.get(contadorEntrada) + " no está declarado en la función " + contexto);
                    }
                }
                estadoActual = 0;
            } else if (estadoActual == 11) {
                Funcion funcionR = funciones.get(contexto);
                if (funcionR.getTipo().equals("void")) {
                    errores.add("Return inválido, la función " + contexto + " es de tipo void");
                    estadoActual = 55;
                } else {
                    estadoActual = 13;
                }
            } else if (estadoActual == 13) {
                contadorEntrada = contadorEntrada - 1;
                Funcion funcionReturn = funciones.get(contexto);
                if (tokens.get(contadorEntrada).equals("Identificador")) {
                    if (isVariableExistente(funcionReturn.getVariables(), lexemas.get(contadorEntrada).toString())) {
                        Variable varReturn = getVariable(funcionReturn.getVariables(), lexemas.get(contadorEntrada).toString());
                        if (varReturn.getEstado().equals("No inicializado")) {
                            errores.add("El identificador " + varReturn.getNombre() + " no ha sido inicializado");
                        } else {
                            if (!varReturn.getTipo().equals(funcionReturn.getTipo())) {
                                errores.add("El tipo del identificador " + varReturn.getNombre() + " no coincide con el tipo declarado en la función " + contexto);
                            }
                        }
                    } else {
                        errores.add("rEl identificador " + lexemas.get(contadorEntrada) + " no existe en la función " + contexto);
                    }
                } else if (funcionReturn.getTipo().equals("int") && tokens.get(contadorEntrada).equals("Entero")
                        || funcionReturn.getTipo().equals("flt") && tokens.get(contadorEntrada).equals("Decimal")
                        || funcionReturn.getTipo().equals("strg") && tokens.get(contadorEntrada).equals("Cadena")
                        || funcionReturn.getTipo().equals("bool") && tokens.get(contadorEntrada).equals("Booleano")) {
                } else {
                    errores.add("El tipo de dato retornado en la función " + contexto + " no coincide con el tipo declarado.");
                }
                estadoActual = 0;
            } else if (estadoActual == 14) {
                String nombreVar = lexemas.get(contadorEntrada - 2).toString();
                String tipoDatoAsig = "";
                if (contexto.equals("main")) {
                    if (idsMain.containsKey(nombreVar)) {
                        tipoDatoAsig = idsMain.get(nombreVar).getTipo();
                        idsMain.get(nombreVar).setEstado("Inicializado");
                    } else {
                        errores.add("El identificador " + nombreVar + " no está declarado en el main");
                    }
                } else {
                    if (isVariableExistente(funciones.get(contexto).getVariables(), nombreVar)) {
                        Variable vari = getVariable(funciones.get(contexto).getVariables(), nombreVar);
                        tipoDatoAsig = vari.getTipo();
                        int indiice = getIndiceVariable(funciones.get(contexto).getVariables(), nombreVar);
                        funciones.get(contexto).getVariables().get(indiice).setEstado("Inicializado");
                    } else {
                        errores.add("El identificador " + nombreVar + " no está declarado en la función " + contexto);
                    }
                }
                //Verificar función
                if (funciones.containsKey(lexemas.get(contadorEntrada))) {
                    Funcion funcionAsig = funciones.get(lexemas.get(contadorEntrada));
                    if (!tipoDatoAsig.equals("")) {
                        if (funcionAsig.getTipo().equals("void")) {
                            errores.add("La función " + lexemas.get(contadorEntrada) + " debe retornar un elemento");
                            estadoActual = 55;
                        } else {
                            if (funcionAsig.getTipo().equals(tipoDatoAsig)) {
                                nombreFuncion = lexemas.get(contadorEntrada).toString();
                                parametros = funciones.get(nombreFuncion).getParametros();
                                numParametro = 0;
                                estadoActual = 8;
                            } else {
                                errores.add("El tipo de dato retornado por la función " + lexemas.get(contadorEntrada) + " no coincide con el tipo del identificador donde se desea asignar");
                                estadoActual = 55;
                            }
                        }
                    } else {
                        estadoActual = 55;
                    }
                } else {
                    errores.add("La función " + lexemas.get(contadorEntrada) + " no existe");
                    estadoActual = 55;
                }
            } else if (estadoActual == 15) {
                estadoActual = 55;
            } else if (estadoActual == 55) {
                if (lexemas.get(contadorEntrada).equals(")")) {
                    contadorEntrada = 0;
                } else if (lexemas.get(contadorEntrada).equals(";")) {
                    contadorEntrada = 0;
                }
            }
            contadorEntrada++;
        }
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
                    funciones.get(variable).setParametroInd(new Variable(parametro, tipoDato, "Inicializado"));
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
    }

    public void verificarFuncion() {
        int estadoInicio = 0;
        estadoActual = estadoInicio;
        contadorEntrada = 0;
        Stack llaves = new Stack();
        String nombreFuncion = "";
        boolean existReturn = false;
        llaves.clear();
        while (contadorEntrada < lexemas.size()) {
            if (estadoActual == 0) {
                if (lexemas.get(contadorEntrada).equals("fn")) {
                    estadoActual = 1;
                    existReturn = false;
                    nombreFuncion = lexemas.get(contadorEntrada + 2).toString();
                }
            } else if (estadoActual == 1) {
                if (!lexemas.get(contadorEntrada).equals("void")) {
                    estadoActual = 2;
                } else {
                    estadoActual = 0;
                }
            } else if (estadoActual == 2) {
                if (lexemas.get(contadorEntrada).equals("{")) {
                    llaves.push("{");
                } else if (lexemas.get(contadorEntrada).equals("}")) {
                    llaves.pop();
                    estadoActual = 3;
                }
                if (lexemas.get(contadorEntrada).equals("return")) {
                    existReturn = true;
                }
            } else if (estadoActual == 3) {
                if (llaves.isEmpty()) {
                    if (!existReturn) {
                        errores.add("La función " + nombreFuncion + " necesita un valor de retorno");
                    }
                    estadoActual = 0;
                    contadorEntrada--;
                } else {
                    estadoActual = 2;
                }
            }
            contadorEntrada++;
        }
    }

    private boolean isVariableExistente(ArrayList<Variable> variables, String nombreVariable) {
        boolean isExistente = false;
        for (Variable var : variables) {
            if (var.getNombre().equals(nombreVariable)) {
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

    private int getIndiceVariable(ArrayList<Variable> variables, String variable) {
        int varEncontrada = 0;
        int contador = 0;
        for (Variable var : variables) {
            if (var.getNombre().equals(variable)) {
                varEncontrada = contador;
            }
            contador++;
        }
        return varEncontrada;
    }

    private String getTipoVariable() {
        return "";
    }

    public ArrayList getErrores() {
        return errores;
    }

}
