package pe.gymfit.modelo;

/**
 * Estados posibles de una cuota. Un enum impide escribir estados inválidos
 * como "pagado" o "PENDIENT".
 */
public enum EstadoCuota {
    PENDIENTE,
    PAGADA,
    ANULADA
}
