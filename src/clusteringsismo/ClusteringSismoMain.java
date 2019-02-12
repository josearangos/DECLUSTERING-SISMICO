
package clusteringsismo;
import Vista.ventanaPrincipal;
import Modelo.ModelExcel;
import Controlador.ControlerExcel;
 
/**
 *
 * @author josearangos
 */
public class ClusteringSismoMain {


    public static void main (String[] args) {
        ModelExcel modeloE = new ModelExcel();
        ventanaPrincipal vistaE = new ventanaPrincipal();
        vistaE.lbl.setVisible(false);
        vistaE.Mysplash.setVisible(false);
        ControlerExcel contraControladorExcel = new ControlerExcel(vistaE, modeloE);
        vistaE.setVisible(true);
        vistaE.setLocationRelativeTo(null);
        
                


    }
    
    
}
