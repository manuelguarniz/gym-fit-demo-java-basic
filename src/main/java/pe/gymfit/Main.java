package pe.gymfit;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import pe.gymfit.config.Conexion;
import pe.gymfit.controlador.CuotaController;
import pe.gymfit.controlador.LoginController;
import pe.gymfit.dao.DatosException;
import pe.gymfit.dao.mysql.CuotaDAOMySQL;
import pe.gymfit.dao.mysql.UsuarioDAOMySQL;
import pe.gymfit.seguridad.AuthService;
import pe.gymfit.seguridad.Sesion;
import pe.gymfit.servicio.CuotaService;
import pe.gymfit.vista.CobrarCuotaView;
import pe.gymfit.vista.LoginView;

/**
 * Punto de entrada. Es el ÚNICO lugar que crea las implementaciones (new ...)
 * y las conecta entre sí: vista -> controlador -> servicio -> DAO -> MySQL.
 */
public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        usarAspectoNimbus();
        java.awt.EventQueue.invokeLater(() -> {
            try {
                Conexion conexion = new Conexion();

                // 1) Iniciar sesión (usuario y contraseña de la tabla usuario)
                AuthService auth = new AuthService(new UsuarioDAOMySQL(conexion));
                if (!new LoginController(auth, new LoginView()).autenticar()) {
                    System.exit(0);
                }

                // 2) Armar las capas de "Registrar pago" y mostrar la ventana
                CuotaService servicio = new CuotaService(new CuotaDAOMySQL(conexion));
                new CuotaController(servicio, new CobrarCuotaView(), Sesion.actual()).mostrar();

            } catch (DatosException e) {
                LOG.log(Level.SEVERE, "No se pudo iniciar GymFit", e);
                JOptionPane.showMessageDialog(null, e.getMessage(), "GymFit",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }

    /** Mismo aspecto (Nimbus) que NetBeans pone en los JFrame Form. */
    private static void usarAspectoNimbus() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info
                    : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException e) {
            LOG.log(Level.WARNING, "No se pudo aplicar Nimbus", e);
        }
    }
}
