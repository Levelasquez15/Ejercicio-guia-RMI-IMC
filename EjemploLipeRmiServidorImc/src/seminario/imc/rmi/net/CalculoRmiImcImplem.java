package seminario.imc.rmi.net;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import seminario.imc.rmi.lib.DatosImc;
import seminario.imc.rmi.lib.IRemotaCalculoImc;

/**
 * Implementacion del calculo del IMC del lado del servidor.
 * Atiende invocaciones remotas RMI y registra eventos en consola.
 * 
 * @author Lewis Velasquez
 */
public class CalculoRmiImcImplem implements IRemotaCalculoImc {

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");

    public CalculoRmiImcImplem() {
    }

    private String timestamp() {
        return LocalDateTime.now().format(dtf);
    }

    @Override
    public void conectar(String cliente) {
        System.out.println("\n--------------------------------------------------------------");
        System.out.println("[" + timestamp() + "] [SERVIDOR] >>> CLIENTE CONECTADO: " + cliente);
        System.out.println("--------------------------------------------------------------");
    }

    @Override
    public void desconectar(String cliente) {
        System.out.println("\n--------------------------------------------------------------");
        System.out.println("[" + timestamp() + "] [SERVIDOR] <<< CLIENTE DESCONECTADO: " + cliente);
        System.out.println("--------------------------------------------------------------");
    }

    @Override
    public DatosImc calcularImc(DatosImc datos) {
        System.out.println("\n==============================================================");
        System.out.println("[" + timestamp() + "] [SERVIDOR] PETICION DE CALCULO DE IMC RECIBIDA");
        System.out.println("  -> Datos de entrada : Peso = " + datos.getPeso() + " kg | Altura = " + datos.getAltura() + " m");

        float resultado = 0;
        if (datos.getPeso() <= 0 || datos.getAltura() <= 0) {
            datos.setInterpretacion("ERROR: El peso y la altura deben ser mayores que 0");
            System.out.println("  -> [ERROR] El peso y la altura deben ser mayores que 0");
            System.out.println("==============================================================");
            return datos;
        } else {
            resultado = datos.getPeso() / (datos.getAltura() * datos.getAltura());
            datos.setResultado(resultado);
            if (resultado < 18.5) {
                datos.setInterpretacion("Debes consultar un Medico, tu peso es muy bajo");
            } else if (resultado >= 18.5 && resultado <= 24.9) {
                datos.setInterpretacion("Estas bien de peso");
            } else if (resultado > 24.9 && resultado <= 29.9) {
                datos.setInterpretacion("Debes bajar un poco de peso");
            } else {
                datos.setInterpretacion("Debes consultar un Medico, tu peso es muy alto");
            }
            System.out.println("  -> IMC Calculado    : " + resultado);
            System.out.println("  -> Diagnostico      : " + datos.getInterpretacion());
            System.out.println("==============================================================");
            return datos;
        }
    }
}
