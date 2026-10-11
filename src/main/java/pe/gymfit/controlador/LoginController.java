package pe.gymfit.controlador;

import java.util.logging.Level;
import java.util.logging.Logger;
import pe.gymfit.dao.DatosException;
import pe.gymfit.seguridad.AuthService;
import pe.gymfit.vista.LoginView;

/**
 * Capa CONTROLADOR: pide usuario y contraseña (hasta 3 intentos).
 */
public class LoginController {

    private static final Logger LOG = Logger.getLogger(LoginController.class.getName());
    private static final int INTENTOS = 3;

    private final AuthService auth;
    private final LoginView vista;

    public LoginController(AuthService auth, LoginView vista) {
        this.auth = auth;
        this.vista = vista;
    }

    /** @return true si el usuario inició sesión correctamente. */
    public boolean autenticar() {
        for (int i = 1; i <= INTENTOS; i++) {
            if (!vista.pedirCredenciales()) {
                return false;                          // pulsó Cancelar
            }
            try {
                if (auth.iniciarSesion(vista.getUsuario(), vista.getClave()).isPresent()) {
                    return true;
                }
                vista.mostrarError("Usuario o contraseña incorrectos.");
            } catch (DatosException e) {
                LOG.log(Level.SEVERE, "Error al iniciar sesión", e);
                vista.mostrarError("No se pudo conectar con la base de datos."
                        + " Revisa que MySQL esté iniciado.");
                return false;
            }
        }
        return false;
    }
}
