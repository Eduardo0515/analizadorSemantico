/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generacioncodigojava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hugo Ruiz
 */
public class Ejecucion {

    private ArrayList salida;
    private ArrayList errores;

    public Ejecucion() {
        salida = new ArrayList();
        errores = new ArrayList();
    }

    public void compilarCodigo() {
        String compilar = "javac -cp src src\\generacioncodigojava\\codigo\\Programa.java";
        Runtime run = Runtime.getRuntime();
        try {
            Process procC = run.exec(compilar);
            System.out.println("Here is the standard output of the command:\n");
            //printLines(procE.getInputStream());
            System.out.println("Here is the standard error of the command (if any):\n");
            //printLines(procE.getErrorStream());
        } catch (IOException ex) {
            Logger.getLogger(Ejecucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ejecutarCodigo() {
        String ejecutar = "java -cp src\\ generacioncodigojava.codigo.Programa";
        Runtime run = Runtime.getRuntime();
        try {
            Process procE = run.exec(ejecutar);
            try {
                System.out.println("Here is the standard output of the command:\n");
                printLines(procE.getInputStream());

                System.out.println("Here is the standard error of the command (if any):\n");
                errorLines(procE.getErrorStream());
            } catch (Exception ex) {
                Logger.getLogger(CodigoJava.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (IOException ex) {
            Logger.getLogger(CodigoJava.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void printLines(InputStream ins) throws Exception {
        salida.clear();
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            salida.add(line);
        }
    }

    private void errorLines(InputStream ins) throws Exception {
        errores.clear();
        String line = null;
        BufferedReader in = new BufferedReader(
                new InputStreamReader(ins));
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            errores.add(line);
        }
    }

    public ArrayList getSalida() {
        return salida;
    }

    public ArrayList getErrores() {
        return errores;
    }
}
