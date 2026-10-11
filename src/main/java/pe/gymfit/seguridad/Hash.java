package pe.gymfit.seguridad;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Hash de contraseñas con BCrypt: nunca se guarda ni se compara la clave en texto plano.
 */
public final class Hash {

    private Hash() {
    }

    public static String generar(String clave) {
        return BCrypt.hashpw(clave, BCrypt.gensalt(10));
    }

    public static boolean coincide(String clave, String hashGuardado) {
        return hashGuardado != null && BCrypt.checkpw(clave, hashGuardado);
    }
}
