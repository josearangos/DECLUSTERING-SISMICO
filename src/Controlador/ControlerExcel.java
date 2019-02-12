package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import Vista.ventanaPrincipal;
import Modelo.ModelExcel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author josearangos
 */
public class ControlerExcel  implements ActionListener {
    double[][] matrizValores;
    ModelExcel modeloE = new ModelExcel();
    ventanaPrincipal vistaE= new ventanaPrincipal();
    JFileChooser selecArchivo = new JFileChooser();
    File archivo;
    int contAccion=0;
    
    public ControlerExcel(ventanaPrincipal vistaE, ModelExcel modeloE){
        this.vistaE= vistaE;
        this.modeloE=modeloE;
        this.vistaE.btnFile.addActionListener(this);
        this.vistaE.btnExport.addActionListener(this);
        this.vistaE.btnCalculate.addActionListener(this);
    }
    
    public void AgregarFiltro(){
        selecArchivo.setFileFilter(new FileNameExtensionFilter("Excel (*.xls)", "xls"));
        selecArchivo.setFileFilter(new FileNameExtensionFilter("Excel (*.xlsx)", "xlsx"));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        contAccion++;
        if(contAccion==1)AgregarFiltro();
        
        if(e.getSource() == vistaE.btnFile){
            vistaE.Mysplash.setVisible(true);
            if(selecArchivo.showDialog(null, "Seleccionar archivo")==JFileChooser.APPROVE_OPTION){
                archivo=selecArchivo.getSelectedFile();
                if(archivo.getName().endsWith("xls") || archivo.getName().endsWith("xlsx")){
                    JOptionPane.showMessageDialog(null, 
                            modeloE.Importar(archivo, vistaE.jtDatos,vistaE.txtPath, vistaE.Mysplash) + "\n Formato ."+ archivo.getName().substring(archivo.getName().lastIndexOf(".")+1), 
                            "IMPORTAR EXCEL", JOptionPane.INFORMATION_MESSAGE);
                }else{
                    JOptionPane.showMessageDialog(null, "Elija un formato valido.");
                }
            }
            vistaE.Mysplash.setVisible(false);
        }
        
        if(e.getSource() == vistaE.btnExport){
          
            if(selecArchivo.showDialog(null, "Exportar")==JFileChooser.APPROVE_OPTION){
                vistaE.Mysplash.setVisible(true);
                archivo=selecArchivo.getSelectedFile();
                if(archivo.getName().endsWith("xls") || archivo.getName().endsWith("xlsx")){
                    if (matrizValores !=null) {
                        
                    JOptionPane.showMessageDialog(null, modeloE.ExportMatrizToExcel(archivo, matrizValores) + "\n Formato ."+ archivo.getName().substring(archivo.getName().lastIndexOf(".")+1));
                
                    } else {
                         JOptionPane.showMessageDialog(null,"Antes de exportar debe calcular");
                    }
                    }else{
                    JOptionPane.showMessageDialog(null, "Elija un formato valido.");
                }
                 vistaE.Mysplash.setVisible(false);
            }
        }
        if(e.getSource() == vistaE.btnCalculate){
            vistaE.Mysplash.setVisible(true);
            matrizValores=modeloE.Calcular(archivo, vistaE.jtDatos);
             if (matrizValores != null) {              
                JOptionPane.showMessageDialog(null,"Se calculo con exito!");
            } else {
                 JOptionPane.showMessageDialog(null,"Algo Fallo :/");
            }
            vistaE.Mysplash.setVisible(false);
        }
        
    }
    
}
