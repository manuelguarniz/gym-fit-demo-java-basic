package pe.gymfit.dao;

import java.util.List;
import java.util.Optional;
import pe.gymfit.modelo.Cuota;

/**
 * Capa DAO: el CONTRATO para guardar y leer cuotas.
 * No dice cómo (MySQL, memoria, archivo): eso lo decide cada implementación.
 */
public interface CuotaDAO {

    Optional<Cuota> buscarPorId(int idCuota);

    List<Cuota> listarPendientes(int idSocio);

    void actualizarPago(Cuota cuota);
}
