package seminario.imc.rmi;

import seminario.imc.rmi.vistas.VentanaPrincipal;

/**
 * Clase principal para lanzar la interfaz grafica del cliente IMC.
 * 
 * @author Lewis Velasquez
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        VentanaPrincipal v = new VentanaPrincipal();
        v.setLocationRelativeTo(null);
        v.setVisible(true);
    }
}
