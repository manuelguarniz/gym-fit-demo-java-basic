package pe.gymfit.dao;

/**
 * Error técnico al acceder a los datos (MySQL apagado, clave incorrecta...).
 * Es "unchecked" (RuntimeException) para no llenar de throws las capas de arriba,
 * y guarda la causa original para el log.
 */
public class DatosException extends RuntimeException {

    public DatosException(String msg, Throwable causa) {
        super(msg, causa);
    }
}
