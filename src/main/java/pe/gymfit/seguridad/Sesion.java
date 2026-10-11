package pe.gymfit.seguridad;

import java.util.Arrays;
import pe.gymfit.modelo.Usuario;

/**
 * Guarda quién inició sesión y responde si tiene un rol permitido (regla R3).
 */
public class Sesion {

    private static Sesion actual;

    private final Usuario usuario;

    private Sesion(Usuario usuario) {
        this.usuario = usuario;
    }

    public static void iniciar(Usuario usuario) {
        actual = new Sesion(usuario);
    }

    public static Sesion actual() {
        if (actual == null) {
            throw new IllegalStateException("Nadie ha iniciado sesión");
        }
        return actual;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean tieneRol(Rol... permitidos) {
        return Arrays.asList(permitidos).contains(usuario.getRol());
    }
}
