package pe.gymfit.servicio;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pe.gymfit.dao.CuotaDAOMemoria;
import pe.gymfit.modelo.Cuota;
import pe.gymfit.modelo.EstadoCuota;
import pe.gymfit.modelo.MedioPago;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pruebas de las reglas R1 y R2. Se escriben ANTES que el servicio (TDD):
 * primero fallan (rojo) y luego pasan (verde).
 */
class CuotaServiceTest {

    CuotaDAOMemoria dao = new CuotaDAOMemoria();
    CuotaService servicio = new CuotaService(dao);
    BigDecimal s129 = new BigDecimal("129.00");

    @BeforeEach
    void datos() {
        dao.agregar(new Cuota(1, 10, s129, LocalDate.now()));
    }

    @Test
    void registraPagoDeCuotaPendiente() {
        servicio.registrarPago(1, s129, MedioPago.TARJETA);
        assertEquals(EstadoCuota.PAGADA, dao.buscarPorId(1).get().getEstado());
    }

    @Test
    void rechazaCuotaYaPagada() {                                        // R1
        servicio.registrarPago(1, s129, MedioPago.TARJETA);
        assertThrows(PagoInvalidoException.class,
                () -> servicio.registrarPago(1, s129, MedioPago.EFECTIVO));
    }

    @Test
    void rechazaMontoDistinto() {                                        // R2
        assertThrows(PagoInvalidoException.class,
                () -> servicio.registrarPago(1, new BigDecimal("100.00"), MedioPago.EFECTIVO));
    }
}
