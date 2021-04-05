package generacioncodigojava.codigo;
import javax.swing.JOptionPane;
public class Programa{ 
public static void main(String[] args){ 
int contador = 0 ; 
int c = 10 ; 
String nombre = "" ; 
while ( contador < 5 ) { 
System.out.println( "Esta es la iteracion: " + contador ) ; 
contador = contador + 1 ; 
nombre = imprimirNombre ( "Hugo" ) ; 
System.out.println( nombre ) ; 
} 
} 
public static String imprimirNombre ( String nombre ) { 
String n = nombre ; 
return n ; 
} 
} 
