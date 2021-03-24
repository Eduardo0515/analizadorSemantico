/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leerarchivo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Hugo Ruiz
 */
public class LeerArchivo {
    private String[][] tabla = new String[45][45];
    
    public void leerExcel() throws IOException {
        File file = new File("src\\files\\TablaProgramaFinal.xlsx");
        try {
            FileInputStream fileIn = new FileInputStream(file);

            XSSFWorkbook wb = new XSSFWorkbook(fileIn);
            XSSFSheet sheet = wb.getSheetAt(0);

            int numFilas = sheet.getLastRowNum();
            for (int a = 0; a <= numFilas; a++) {
                Row fila = sheet.getRow(a);
                int numCols = fila.getLastCellNum();
                for (int b = 0; b < numCols; b++) {
                    Cell celda = fila.getCell(b);
                    
                    switch(celda.getCellTypeEnum().toString()){
                        case("NUMERIC"):
                            //System.out.print(celda.getNumericCellValue() + " ");
                            break;                     
                        case("STRING"):
                            //System.out.print(celda.getStringCellValue()+ " ");
                            tabla[a][b] = celda.getStringCellValue();
                            break;
                        case("FORMULA"):
                            //System.out.print(celda.getCellFormula()+ " ");
                            break;
                    }
                }
                //System.out.println("");
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(LeerArchivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public String[][] getTabla(){
        return tabla;
    }
}
