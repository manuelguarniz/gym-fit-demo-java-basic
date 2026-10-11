package pe.gymfit.dao;

import java.util.Optional;
import pe.gymfit.modelo.Usuario;

/**
 * Capa DAO: contrato para leer los usuarios del sistema.
 */
public interface UsuarioDAO {

    Optional<Usuario> buscarPorUsername(String username);
}
