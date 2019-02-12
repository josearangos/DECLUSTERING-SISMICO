/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.*;

/**
 *
 * @author josearangos
 */
public class ModelExcel {

    Workbook wb;
    double[][] matrizValores;
    
    

    public String Importar(File archivo, JTable tablaD, JTextField txtPath, JLabel MySplash) {
        
        String respuesta = "No se pudo realizar la importación.";
        DefaultTableModel modeloT = new DefaultTableModel();
        tablaD.setModel(modeloT);
        tablaD.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        try {
            wb = WorkbookFactory.create(new FileInputStream(archivo));
            Sheet hoja = wb.getSheetAt(0);
            Iterator filaIterator = hoja.rowIterator();

            int indiceFila = -1;
            while (filaIterator.hasNext()) {
                indiceFila++;
                Row fila = (Row) filaIterator.next();
                Iterator columnaIterator = fila.cellIterator();
                Object[] listaColumna = new Object[19000];
                int indiceColumna = -1;
                while (columnaIterator.hasNext()) {
                    indiceColumna++;
                    Cell celda = (Cell) columnaIterator.next();
                    if (indiceFila == 0) {
                        modeloT.addColumn(celda.getStringCellValue());
                    } else {
                        if (celda != null) {
                            switch (celda.getCellType()) {
                                case Cell.CELL_TYPE_NUMERIC:
                                    listaColumna[indiceColumna] = (double) (celda.getNumericCellValue());
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    listaColumna[indiceColumna] = celda.getStringCellValue();
                                    break;
                                case Cell.CELL_TYPE_BOOLEAN:
                                    listaColumna[indiceColumna] = celda.getBooleanCellValue();
                                    break;
                                default:
                                    listaColumna[indiceColumna] = celda.getDateCellValue();
                                    break;
                            }
                            // System.out.println("col"+indiceColumna+" valor: true - "+celda+".");
                        }
                    }
                }
                if (indiceFila != 0) {
                    modeloT.addRow(listaColumna);
                }
            }

            respuesta = "Importación exitosa";
            txtPath.setText(archivo.getPath());
        } catch (IOException | InvalidFormatException | EncryptedDocumentException e) {
            System.err.println(e.getMessage());
        }
        
        return respuesta;
    }

    public String Exportar(File archivo, JTable tablaD) {

        String respuesta = "No se realizo con exito la exportación.";

        int numFila = tablaD.getRowCount(), numColumna = tablaD.getColumnCount();

        if (archivo.getName().endsWith("xls")) {
            wb = new HSSFWorkbook();
        } else {
            wb = new XSSFWorkbook();
        }
        Sheet hoja = wb.createSheet("Pruebita");

        try {
            for (int i = -1; i < numFila; i++) {
                Row fila = hoja.createRow(i + 1);
                for (int j = 0; j < numColumna; j++) {
                    Cell celda = fila.createCell(j);
                    if (i == -1) {
                        celda.setCellValue(String.valueOf(tablaD.getColumnName(j)));
                    } else {
                        celda.setCellValue(String.valueOf(tablaD.getValueAt(i, j)));
                    }
                    wb.write(new FileOutputStream(archivo));
                }
            }
            respuesta = "Exportación exitosa.";
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return respuesta;
    }

    public double[][] Calcular(File archivo, JTable tablaD){
        
        
        int numFila = tablaD.getRowCount(), numColumna = tablaD.getColumnCount();
        matrizValores = new double[numFila][numColumna];
        
                                 
            for (int i = -1; i < numFila; i++) {
                for (int j = 0; j < numColumna; j++) {
                    if (i != -1) {
                        matrizValores[i][j] = Double.valueOf(String.valueOf(tablaD.getValueAt(i, j)));
                    }

                }
            }
       
      
     
        for (int i = 0; i < numFila; i++) {     
            
            double Mi = matrizValores[i][4];
            double constanted = 0.1238;
            double constante2d = 0.983;
            double di = Math.pow(10, ((constanted * Mi) + constante2d));
            double constantet = 0.032;
            double constantet2 = 2.7389;
            double constantet3 = 0.5409;
            double constantet4 = 0.547;
            double ti = 0;
            if (Mi >= 6.5) {
                ti = Math.pow(10, ((constantet * Mi) + constantet2));
            } else {
                ti = Math.pow(10, ((constantet3 * Mi) - constantet4));
            }
            //Calcular la distancia y tiempo con respecto a otros
            double d12 = 0;
            double t12 = 0;
            //Recoremos hacia abajo
            for (int j = i + 1; j < numFila; j++) {
                if (matrizValores[j][6] != 1) {
                    double x = matrizValores[i][1] - matrizValores[j][1];
                    double y = matrizValores[i][2] - matrizValores[j][2];
                    double z = matrizValores[i][3] - matrizValores[j][3];
                    double dtemporal = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
                    d12 = Math.pow(dtemporal, 0.5);
                    double taux = matrizValores[i][5] - matrizValores[j][5];
                    t12 = Math.pow(Math.pow(taux, 2), 0.5);
                    //Validar si pongo 1 o 0
                    if (di > d12 && ti > t12 && Mi > matrizValores[j][4]) {
                        matrizValores[j][6] = 1;
                    }

                } else {
                    continue;
                }
            }
            //Recoremos hacia arriba
            if (i != 0) {
                for (int k = i - 1; k >= 0; k--) {
                    if (matrizValores[k][6] != 1) {
                        double x = matrizValores[i][1] - matrizValores[k][1];
                        double y = matrizValores[i][2] - matrizValores[k][2];
                        double z = matrizValores[i][3] - matrizValores[k][3];
                        double dtemporal = Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
                        d12 = Math.pow(dtemporal, 0.5);
                        double taux = matrizValores[i][5] - matrizValores[k][5];
                        t12 = Math.pow(Math.pow(taux, 2), 0.5);
                        //Validar si pongo 1 o 0
                        if (di > d12 && ti > t12 && Mi > matrizValores[k][4]) {
                            matrizValores[k][6] = 1;
                        }

                    } else {
                        continue;
                    }

                }
            }

        }        
        //lbl.setVisible(false);
        return matrizValores;

    }

    public String ExportMatrizToExcel(File archivo, double[][] matrizValores) {
        String[] header= new String[]{"ID","X","Y","Z","M","T","O"};
        String respuesta = "No se realizo con exito la exportación.";

        int numFila = matrizValores.length, numColumna = matrizValores[0].length;

        if (archivo.getName().endsWith("xls")) {
            wb = new HSSFWorkbook();
        } else {
            wb = new XSSFWorkbook();
        }
        Sheet hoja = wb.createSheet("Pruebita");

        try {
            for (int i = -1; i < numFila; i++) {               
                   Row fila = hoja.createRow(i + 1);                    
                    for (int j = 0; j < numColumna; j++) {
                        Cell celda = fila.createCell(j);
                        if (i == -1) {
                            celda.setCellValue(header[j]);
                        } else {
                            if(matrizValores[i][6]!=1){
                                    
                            celda.setCellValue(String.valueOf(matrizValores[i][j]));
                            }

                        }
                        wb.write(new FileOutputStream(archivo));
                    
                   }
               
                                
            }
            respuesta = "Exportación exitosa.";
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return respuesta;
    }

    public ModelExcel() {
    }

}
