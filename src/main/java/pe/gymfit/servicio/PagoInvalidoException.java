package pe.gymfit.servicio;

/**
 * Error de NEGOCIO: su mensaje está pensado para que lo lea el usuario.
 */
public class PagoInvalidoException extends RuntimeException {

    public PagoInvalidoException(String msg) {
        super(msg);
    }
}
