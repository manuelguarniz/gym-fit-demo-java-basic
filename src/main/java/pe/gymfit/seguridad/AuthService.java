package pe.gymfit.seguridad;

import java.util.Optional;
import pe.gymfit.dao.UsuarioDAO;
import pe.gymfit.modelo.Usuario;

/**
 * Inicia sesión comparando la clave escrita con el hash BCrypt guardado en MySQL.
 */
public class AuthService {

    private final UsuarioDAO dao;

    public AuthService(UsuarioDAO dao) {
        this.dao = dao;
    }

    public Optional<Usuario> iniciarSesion(String username, char[] clave) {
        Optional<Usuario> usuario = dao.buscarPorUsername(username)
                .filter(u -> Hash.coincide(new String(clave), u.getPasswordHash()));
        usuario.ifPresent(Sesion::iniciar);
        return usuario;
    }
}
