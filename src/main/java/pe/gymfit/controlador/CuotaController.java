package pe.gymfit.controlador;

import java.util.logging.Level;
import java.util.logging.Logger;
import pe.gymfit.dao.DatosException;
import pe.gymfit.modelo.Cuota;
import pe.gymfit.modelo.Usuario;
import pe.gymfit.seguridad.Rol;
import pe.gymfit.seguridad.Sesion;
import pe.gymfit.servicio.CuotaService;
import pe.gymfit.servicio.PagoInvalidoException;
import pe.gymfit.vista.CobrarCuotaView;

/**
 * Capa CONTROLADOR: recibe el clic de la vista, controla el rol (R3),
 * llama al servicio y traduce las excepciones a mensajes para el usuario.
 */
public class CuotaController {

    private static final Logger LOG = Logger.getLogger(CuotaController.class.getName());

    private final CuotaService servicio;
    private final CobrarCuotaView vista;
    private final Sesion sesion;

    public CuotaController(CuotaService servicio, CobrarCuotaView vista, Sesion sesion) {
        this.servicio = servicio;
        this.vista = vista;
        this.sesion = sesion;
        this.vista.setControlador(this);
    }

    public void mostrar() {
        Usuario u = sesion.getUsuario();
        vista.mostrarUsuario(u.getNombre() + " · " + u.getRol());
        vista.setVisible(true);
    }

    /** Lo llama la vista cuando se pulsa el botón "Registrar pago". */
    public void onRegistrarPago() {
        if (!sesion.tieneRol(Rol.ASESOR, Rol.ADMIN)) {                       // R3
            vista.mostrarError("No tienes permiso para registrar pagos.");
            return;
        }
        try {
            Cuota c = servicio.registrarPago(vista.getIdCuota(),
                                             vista.getMonto(), vista.getMedio());
            vista.mostrarExito("Pago registrado: S/ " + c.getMonto());
            vista.limpiar();
        } catch (NumberFormatException e) {
            vista.mostrarError("Escribe números válidos. Ejemplo: cuota 1, monto 129.00");
        } catch (PagoInvalidoException e) {
            vista.mostrarError(e.getMessage());                              // mensaje de negocio
        } catch (DatosException e) {
            LOG.log(Level.SEVERE, "Error al registrar pago", e);             // detalle solo en el log
            vista.mostrarError("No se pudo registrar el pago. Intenta de nuevo.");
        }
    }
}
