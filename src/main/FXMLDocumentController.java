/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import analizadorlexico.principal.AutomataPrincipal;
import analizadorsemantico.AnalizadorSemantico;
import analizadorsintactico.analisisnorecursivo.AnalisisNoRecursivo;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import leerarchivo.LeerArchivo;

/**
 *
 * @author Hugo Ruiz
 */
public class FXMLDocumentController implements Initializable {

    private AutomataPrincipal automataPrincipal;
    private AnalisisNoRecursivo analisis;
    private AnalizadorSemantico analisisSemantico;
    private String[][] tabla;
    private ArrayList lexemas;
    private ArrayList lexemasLiterales;
    private ArrayList tokens;

    @FXML
    private Slider textosSlider;

    @FXML
    private TextArea entradaTxt, resultadoTxt, ejemploText;
    @FXML
    private Pane ventanaAnalizar, ventanaEjemplo;
    @FXML
    private Rectangle btnAnalizar, btnEjemplo;
    @FXML
    private Text soloMain, funcion, iftext, invocacion, todo, textEjemplo;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (isEntradaAceptada(entradaTxt.getText())) {
            automataPrincipal.analizar(entradaTxt.getText());
            lexemas = automataPrincipal.getElementos();
            tokens = automataPrincipal.getSignificados();
            lexemasLiterales = automataPrincipal.getElementosLiterales();
            System.out.println(lexemas);
            System.out.println(tokens);
            try {
                //Análisis sintáctico
                leerTabla();
                analisis = new AnalisisNoRecursivo(lexemas, tabla);
                analisis.metodoPredictivoNoRecursivo();
                resultadoTxt.setText(analisis.getResultado());
                System.out.println("");
                //Análisis semántico
                analisisSemantico = new AnalizadorSemantico(lexemasLiterales, tokens);
                analisisSemantico.addFunciones();
                analisisSemantico.analizar();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            alert();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        automataPrincipal = new AutomataPrincipal();
        estilo();
        modificarTextoEjemplo(0);
    }

    public void leerTabla() throws IOException {
        LeerArchivo leerArchivo = new LeerArchivo();
        leerArchivo.leerExcel();
        tabla = leerArchivo.getTabla();
    }

    public void estilo() {
        entradaTxt.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: "
                + "#827e7e; -fx-highlight-text-fill: #000000; -fx-text-fill: #ffffff; -fx-font-size: 18");
        resultadoTxt.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: "
                + "#827e7e; -fx-highlight-text-fill: #000000; -fx-text-fill: #ffffff; -fx-font-size: 16");
        ejemploText.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-highlight-fill: "
                + "#827e7e; -fx-highlight-text-fill: #000000; -fx-text-fill: #ffffff; -fx-font-size: 18");
        btnAnalizar.setFill(Color.rgb(50, 59, 70));
        ventanaAnalizar.toFront();
        textosSlider.setOnMouseDragged(mouseEvent -> {
            modificarTextoEjemplo(textosSlider.getValue());
        });
        textosSlider.setOnMouseClicked(e -> {
            modificarTextoEjemplo(textosSlider.getValue());
        });
    }

    public void modificarTextoEjemplo(double valor) {
        if (valor <= 20) {
            ejemploText.setText(soloMain.getText());
            textEjemplo.setText("Ejemplo con función principal");
        } else if (valor <= 40) {
            ejemploText.setText(funcion.getText());
            textEjemplo.setText("Ejemplo con declaración de función");
        } else if (valor <= 60) {
            ejemploText.setText(iftext.getText());
            textEjemplo.setText("Ejemplo con función y declaracion if");
        } else if (valor <= 80) {
            ejemploText.setText(invocacion.getText());
            textEjemplo.setText("Ejemplo con invocación de función");
        } else if (valor <= 100) {
            ejemploText.setText(todo.getText());
            textEjemplo.setText("Ejemplo con salida de dato");
        }
    }

    public boolean isEntradaAceptada(String entradatxt) {
        Pattern p = Pattern.compile("^[\\s]+$");
        Matcher m = p.matcher(entradatxt);

        if (m.find() || entradatxt.length() == 0) {
            return false;
        }
        p = Pattern.compile("\\$");
        m = p.matcher(entradatxt);
        if (m.find()) {
            return false;
        }
        System.out.println(entradatxt.matches("\\$"));
        return true;
    }

    public void alert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Error en la entrada");
        alert.setContentText("Por favor, verifique la cadena ingresada y vuelva a ejecutar.");
        alert.showAndWait();
    }

    double x, y;

    @FXML
    void dragged(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }

    @FXML
    private void min(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void max(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setFullScreen(true);
    }

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    void analizador(MouseEvent event) {
        ventanaAnalizar.toFront();
        btnAnalizar.setFill(Color.rgb(50, 59, 70));
        btnEjemplo.setFill(Color.rgb(26, 32, 40));
    }

    @FXML
    void ejemplos(MouseEvent event) {
        ventanaEjemplo.toFront();
        btnEjemplo.setFill(Color.rgb(50, 59, 70));
        btnAnalizar.setFill(Color.rgb(26, 32, 40));
    }
}
