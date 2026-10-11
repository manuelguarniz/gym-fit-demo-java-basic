package pe.gymfit.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import pe.gymfit.config.Conexion;
import pe.gymfit.dao.DatosException;
import pe.gymfit.dao.UsuarioDAO;
import pe.gymfit.modelo.Usuario;
import pe.gymfit.seguridad.Rol;

/**
 * Capa DAO (implementación MySQL) para la tabla usuario.
 */
public class UsuarioDAOMySQL implements UsuarioDAO {

    private final Conexion conexion;

    public UsuarioDAOMySQL(Conexion conexion) {
        this.conexion = conexion;
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        String sql = "SELECT id_usuario, username, password_hash, nombre, rol"
                + " FROM usuario WHERE username = ? AND activo = 1";
        try (Connection cn = conexion.obtener();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return Optional.empty();
                }
                return Optional.of(new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("nombre"),
                        Rol.valueOf(rs.getString("rol"))));
            }
        } catch (SQLException e) {
            throw new DatosException("No se pudo leer el usuario", e);
        }
    }
}
