/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analizadorsemantico;

import java.util.ArrayList;

/**
 *
 * @author Hugo Ruiz
 */
public class Funcion {
    private String tipo;
    private ArrayList<Variable> variables;
    private ArrayList<Variable> parametros;
    
    public Funcion(String tipo){
        this.tipo = tipo;
        variables = new ArrayList<Variable>();
        parametros = new ArrayList<Variable>();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ArrayList<Variable> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<Variable> variables) {
        this.variables = variables;
    }
    
    public void setVariableInd(Variable variable){
        this.variables.add(variable);
    }
    
     public ArrayList<Variable> getParametros() {
        return parametros;
    }

    public void setParametros(ArrayList<Variable> parametros) {
        this.parametros = parametros;
    }
    
    public void setParametroInd(Variable variable){
        this.parametros.add(variable);
    }
    
}
