package pe.gymfit.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Capa MODELO: una cuota mensual de la membresía de un socio.
 * Java puro: no importa nada de javax.swing ni de java.sql.
 */
public class Cuota {

    private int idCuota;
    private int idMembresia;
    private BigDecimal monto;                 // dinero: nunca double
    private LocalDate fechaVencimiento;
    private EstadoCuota estado = EstadoCuota.PENDIENTE;
    private MedioPago medio;
    private LocalDateTime fechaPago;

    public Cuota(int idCuota, int idMembresia, BigDecimal monto, LocalDate vence) {
        this.idCuota = idCuota;
        this.idMembresia = idMembresia;
        this.monto = monto;
        this.fechaVencimiento = vence;
    }

    /** Comportamiento: la cuota sabe si todavía se puede pagar. */
    public boolean estaPendiente() {
        return estado == EstadoCuota.PENDIENTE;
    }

    /** Comportamiento: la cuota se marca a sí misma como pagada. */
    public void marcarPagada(MedioPago medio) {
        this.estado = EstadoCuota.PAGADA;
        this.medio = medio;
        this.fechaPago = LocalDateTime.now();
    }

    // Getters generados con Alt+Insert > Getter...

    public int getIdCuota() {
        return idCuota;
    }

    public int getIdMembresia() {
        return idMembresia;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public EstadoCuota getEstado() {
        return estado;
    }

    public MedioPago getMedio() {
        return medio;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    /** Solo lo usa el DAO al leer una fila de la base de datos. */
    public void cargarPago(EstadoCuota estado, MedioPago medio, LocalDateTime fechaPago) {
        this.estado = estado;
        this.medio = medio;
        this.fechaPago = fechaPago;
    }
}
