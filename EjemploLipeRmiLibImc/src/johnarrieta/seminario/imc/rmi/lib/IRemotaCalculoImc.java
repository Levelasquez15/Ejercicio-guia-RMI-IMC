package johnarrieta.seminario.imc.rmi.lib;

/**
 * Interfaz remota para el servicio de calculo de IMC.
 * 
 * @author JOHN CARLOS ARRIETA ARRIETA
 */
public interface IRemotaCalculoImc {

    public DatosImc calcularImc(DatosImc datos);

    public default void conectar(String cliente) {
    }

    public default void desconectar(String cliente) {
    }
}
