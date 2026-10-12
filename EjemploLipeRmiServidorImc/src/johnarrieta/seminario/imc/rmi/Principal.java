package johnarrieta.seminario.imc.rmi;

import johnarrieta.seminario.imc.rmi.net.Servidor;

/**
 *
 * @author JOHN CARLOS ARRIETA ARRIETA
 */
public class Principal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Servidor servicio = new Servidor();
        try {
            servicio.iniciar();
            System.out.println("Servidor RMI (LipeRMI) iniciado y a la escucha en el puerto 9007...");
            System.out.println("Esperando solicitudes de calculo de IMC...");
            // Mantener el hilo principal activo para atender conexiones remotas
            Thread.currentThread().join();
        } catch (Exception ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }
}
