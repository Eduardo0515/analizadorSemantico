/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generacioncodigojava;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JOptionPane;

/**
 *
 * @author Hugo Ruiz
 */
public class CodigoJava {

    private ArrayList lexemas;
    private ArrayList tokens;
    private Hashtable<String, String> tablaEqJava;
    private ArrayList codigoJava;

    public CodigoJava(ArrayList lexemas, ArrayList tokens, Hashtable<String, String> tablaEqJava) {
        this.lexemas = lexemas;
        this.tokens = tokens;
        this.tablaEqJava = tablaEqJava;
        codigoJava = new ArrayList();
    }

    public void crearCodigoJava() {
        int contador = 0;
        codigoJava.clear();

        codigoJava.add("package generacioncodigojava.codigo;\n");
        codigoJava.add("import javax.swing.JOptionPane;\n");
        while (contador < lexemas.size()) {
            if (tablaEqJava.containsKey(lexemas.get(contador))) {
                if (lexemas.get(contador).equals("inp")) {
                    codigoJava.add(verifyTipoEntrada(lexemas.get(contador + 2).toString()));
                    contador += 3;
                } else {
                    codigoJava.add(tablaEqJava.get(lexemas.get(contador)));
                    if (lexemas.get(contador).equals("main")) {
                        contador += 2;
                    }
                }
            } else {
                codigoJava.add(lexemas.get(contador) + " ");
                legibilidadCodigo(lexemas.get(contador).toString());
            }
            contador++;
        }
        crearArchivo();
    }

    public String verifyTipoEntrada(String tipo) {
        String inputCode = "";
        switch (tipo) {
            case "int":
                inputCode = "Integer.parseInt(JOptionPane.showInputDialog(\"Ingrese un numero entero\"))";
                break;
            case "flt":
                inputCode = "Double.parseDouble(JOptionPane.showInputDialog(\"Ingrese un numero decimal\"))";
                break;
            case "strg":
                inputCode = "JOptionPane.showInputDialog(\"Ingrese una cadena\")";
                break;
            default:
                inputCode = "JOptionPane.showInputDialog(\"Ingrese el dato\")";
        }
        return inputCode;
    }

    private void legibilidadCodigo(String elemento) {
        if (elemento.equals("{") || elemento.equals("}") || elemento.equals(";")) {
            codigoJava.add("\n");
        }
    }

    private void crearArchivo() {
        FileWriter flwriter = null;
        try {
            flwriter = new FileWriter("src\\generacioncodigojava\\codigo\\Programa.java");
            BufferedWriter bfwriter = new BufferedWriter(flwriter);
            for (Object lineaCodigo : codigoJava) {
                bfwriter.write(lineaCodigo.toString());
            }
            bfwriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (flwriter != null) {
                try {
                    flwriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList getCodigoJava() {
        return codigoJava;
    }
}
