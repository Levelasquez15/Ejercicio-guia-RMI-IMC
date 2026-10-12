package seminario.imc.rmi.lib;

/**
 * Interfaz remota que define el contrato del servicio distribuido para el calculo del IMC.
 * 
 * @author Lewis Velasquez
 */
public interface IRemotaCalculoImc {

    public DatosImc calcularImc(DatosImc datos);

    public default void conectar(String cliente) {
    }

    public default void desconectar(String cliente) {
    }
}
