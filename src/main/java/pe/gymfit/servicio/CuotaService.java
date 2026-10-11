package pe.gymfit.servicio;

import java.math.BigDecimal;
import pe.gymfit.dao.CuotaDAO;
import pe.gymfit.modelo.Cuota;
import pe.gymfit.modelo.MedioPago;

/**
 * Capa SERVICIO: aquí viven las reglas del negocio.
 * R1: solo se pagan cuotas PENDIENTE.
 * R2: el monto debe ser igual al de la cuota.
 */
public class CuotaService {

    private final CuotaDAO dao;

    public CuotaService(CuotaDAO dao) {      // el DAO se inyecta por el constructor
        this.dao = dao;
    }

    public Cuota registrarPago(int idCuota, BigDecimal monto, MedioPago medio) {
        Cuota cuota = dao.buscarPorId(idCuota)
                .orElseThrow(() -> new PagoInvalidoException("La cuota no existe"));
        if (!cuota.estaPendiente()) {                          // R1
            throw new PagoInvalidoException("La cuota ya fue pagada");
        }
        if (cuota.getMonto().compareTo(monto) != 0) {          // R2
            throw new PagoInvalidoException("El monto no coincide con la cuota");
        }
        cuota.marcarPagada(medio);
        dao.actualizarPago(cuota);
        return cuota;
    }
}
