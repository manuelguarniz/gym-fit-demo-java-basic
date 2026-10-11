package pe.gymfit.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import pe.gymfit.config.Conexion;
import pe.gymfit.dao.CuotaDAO;
import pe.gymfit.dao.DatosException;
import pe.gymfit.modelo.Cuota;
import pe.gymfit.modelo.EstadoCuota;
import pe.gymfit.modelo.MedioPago;

/**
 * Capa DAO (implementación MySQL). Todas las consultas usan PreparedStatement
 * con "?" para evitar la inyección SQL. Nunca devuelve un ResultSet: devuelve Cuota.
 */
public class CuotaDAOMySQL implements CuotaDAO {

    private static final String COLUMNAS =
            "c.id_cuota, c.id_membresia, c.monto, c.fecha_vencimiento,"
            + " c.estado, c.medio_pago, c.fecha_pago";

    private final Conexion conexion;

    public CuotaDAOMySQL(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public Optional<Cuota> buscarPorId(int idCuota) {
        String sql = "SELECT " + COLUMNAS + " FROM cuota c WHERE c.id_cuota = ?";
        try (Connection cn = conexion.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idCuota);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatosException("No se pudo buscar la cuota " + idCuota, e);
        }
    }

    @Override
    public List<Cuota> listarPendientes(int idSocio) {
        String sql = "SELECT " + COLUMNAS + " FROM cuota c"
                + " JOIN membresia m ON m.id_membresia = c.id_membresia"
                + " WHERE m.id_socio = ? AND c.estado = 'PENDIENTE'"
                + " ORDER BY c.fecha_vencimiento";
        List<Cuota> lista = new ArrayList<>();
        try (Connection cn = conexion.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, idSocio);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new DatosException("No se pudo listar las cuotas del socio " + idSocio, e);
        }
    }

    @Override
    public void actualizarPago(Cuota c) {
        String sql = "UPDATE cuota SET estado = ?, medio_pago = ?, fecha_pago = ?"
                + " WHERE id_cuota = ?";
        try (Connection cn = conexion.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, c.getEstado().name());
            ps.setString(2, c.getMedio().name());
            ps.setTimestamp(3, Timestamp.valueOf(c.getFechaPago()));
            ps.setInt(4, c.getIdCuota());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("No se pudo registrar el pago", e);
        }
    }

    /** Convierte la fila actual del ResultSet en un objeto Cuota. */
    private Cuota mapear(ResultSet rs) throws SQLException {
        Cuota c = new Cuota(
                rs.getInt("id_cuota"),
                rs.getInt("id_membresia"),
                rs.getBigDecimal("monto"),
                rs.getDate("fecha_vencimiento").toLocalDate());
        String medio = rs.getString("medio_pago");
        Timestamp pago = rs.getTimestamp("fecha_pago");
        c.cargarPago(
                EstadoCuota.valueOf(rs.getString("estado")),
                medio == null ? null : MedioPago.valueOf(medio),
                pago == null ? null : pago.toLocalDateTime());
        return c;
    }
}
