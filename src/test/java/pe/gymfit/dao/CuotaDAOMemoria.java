package pe.gymfit.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pe.gymfit.modelo.Cuota;

/**
 * DAO falso que guarda las cuotas en memoria (un HashMap).
 * Permite probar el servicio SIN MySQL.
 */
public class CuotaDAOMemoria implements CuotaDAO {

    private final Map<Integer, Cuota> datos = new HashMap<>();

    public void agregar(Cuota c) {
        datos.put(c.getIdCuota(), c);
    }

    @Override
    public Optional<Cuota> buscarPorId(int idCuota) {
        return Optional.ofNullable(datos.get(idCuota));
    }

    @Override
    public List<Cuota> listarPendientes(int idSocio) {
        return new ArrayList<>(datos.values());
    }

    @Override
    public void actualizarPago(Cuota c) {
        datos.put(c.getIdCuota(), c);
    }
}
