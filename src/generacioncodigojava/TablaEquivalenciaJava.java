/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generacioncodigojava;

import java.util.Hashtable;

/**
 *
 * @author Hugo Ruiz
 */
public class TablaEquivalenciaJava {

    private Hashtable<String, String> equivalenciasJava;

    public TablaEquivalenciaJava() {
        equivalenciasJava = new Hashtable<>();
        setAllEquivalencias();
    }

    private void setAllEquivalencias() {
        equivalenciasJava.put("program", "public class Programa");
        equivalenciasJava.put("main", "public static void main(String[] args)");
        equivalenciasJava.put("out", "System.out.println");
        equivalenciasJava.put("inp", "//");
        equivalenciasJava.put("fn", "public static ");
        equivalenciasJava.put("strg", "String ");
        equivalenciasJava.put("flt", "double ");
        equivalenciasJava.put("bool", "boolean ");
        equivalenciasJava.put("&", "&&");
        equivalenciasJava.put("|", "||");
    }

    public Hashtable<String, String> getEquivalencias() {
        return equivalenciasJava;
    }
}
