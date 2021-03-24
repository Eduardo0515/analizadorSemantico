package analizadorlexico.diccionarioautomata;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Diccionario {

    private Hashtable<String, String> dic = new Hashtable<>();

    public Diccionario() {
        dic.put("(", "Agrupacion(");
        dic.put(")", "Agrupacion)");
        dic.put("{", "Agrupacion{");
        dic.put("}", "Agrupacion}");
        dic.put("<", "Comparacion");
        dic.put(">", "Comparacion");
        dic.put("==", "Comparacion");
        dic.put("!=", "Comparacion");
        dic.put("<=", "Comparacion");
        dic.put(">=", "Comparacion");
        dic.put("&", "OpLogico");
        dic.put("|", "OpLogico");
        dic.put(";", "TerminacionLinea");
        dic.put("+", "Concatenacion");
        dic.put("=", "Asignacion");
        dic.put("true", "Booleano");
        dic.put("false", "Booleano");
        dic.put(",", "Coma");
    }

    public ArrayList<String> getStaticKeys() {
        ArrayList<String> lista = new ArrayList<>();

        Enumeration<String> elements = dic.keys();

        while (elements.hasMoreElements()) {
            lista.add(elements.nextElement());
        }

        return lista;
    }

    public ArrayList<String> getDescriptions() {
        ArrayList<String> lista = new ArrayList<>();

        Enumeration<String> elements = dic.elements();

        while (elements.hasMoreElements()) {
            lista.add(elements.nextElement());
        }

        lista.add("Entero");
        lista.add("Decimal");
        lista.add("Identificador");
        lista.add("Cadena");

        return lista;
    }

    public String getDescriptionOf(String entrada) {
        //valida que exista la entrada
        if (dic.containsKey(entrada)) {
            return dic.get(entrada);
        }

        //valida que sea entero
        if (entrada.matches("[0-9]+")) {
            return "Entero";
        }

        //valida que sea double
        if (isValidDouble(entrada)) {
            return "Decimal";
        }

        //valida que sea variable
        Pattern p = Pattern.compile("^[A-Za-z]+");
        Matcher m = p.matcher(entrada);
        if (m.find()) {
            if (entrada.matches("[a-zA-Z0-9]+")) {
                return "Identificador";
            }
        }

        //valida que sea string
        p = Pattern.compile("^\"");
        m = p.matcher(entrada);
        if (m.find()) {
            int ultimo = entrada.length() - 1;
            if (entrada.charAt(ultimo) == '\"' && entrada.length() >= 2) {
                return "Cadena";
            }
        }

        //valida que sea char
        /*p = Pattern.compile("^\'");
        m = p.matcher(entrada);
        if (m.find()) {
            int ultimo = entrada.length() - 1;
            if (entrada.charAt(ultimo) == '\'' && entrada.length() == 3) {
                return "Caracter";
            }
        }*/

        return "Indefinido";
    }

    public boolean existe(String entrada) {
        //valida que exista la entrada
        if (dic.containsKey(entrada)) {
            return true;
        }

        //valida que sea entero
        if (entrada.matches("[0-9]+")) {
            return true;
        }

        //valida que sea double
        if (isValidDouble(entrada)) {
            return true;
        }

        //valida que sea variable
        Pattern p = Pattern.compile("^[A-Za-z]+");
        Matcher m = p.matcher(entrada);
        if (m.find()) {
            if (entrada.matches("[a-zA-Z0-9]+")) {
                return true;
            }
        }

        //valida que sea string
        p = Pattern.compile("^\"");
        m = p.matcher(entrada);
        if (m.find()) {
            int ultimo = entrada.length() - 1;
            if (entrada.charAt(ultimo) == '\"' && entrada.length() >= 2) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidDouble(String s) {
        Pattern p = Pattern.compile("^[0-9]+\\.[0-9]+$");
        Matcher m = p.matcher(s);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public Hashtable<String, String> getDic() {
        return dic;
    }
}
