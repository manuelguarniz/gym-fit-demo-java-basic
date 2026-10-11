package pe.gymfit.vista;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Capa VISTA: cuadro de diálogo para iniciar sesión.
 */
public class LoginView {

    private final JTextField txtUsuario = new JTextField(15);
    private final JPasswordField txtClave = new JPasswordField(15);

    /** @return true si el usuario pulsó Aceptar. */
    public boolean pedirCredenciales() {
        txtClave.setText("");
        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(new JLabel("Usuario:"));
        panel.add(txtUsuario);
        panel.add(new JLabel("Contraseña:"));
        panel.add(txtClave);
        int r = JOptionPane.showConfirmDialog(null, panel, "GymFit · Iniciar sesión",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        return r == JOptionPane.OK_OPTION;
    }

    public String getUsuario() {
        return txtUsuario.getText().trim();
    }

    public char[] getClave() {
        return txtClave.getPassword();
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje, "GymFit", JOptionPane.ERROR_MESSAGE);
    }
}
