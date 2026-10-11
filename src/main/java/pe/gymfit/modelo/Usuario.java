package pe.gymfit.modelo;

import pe.gymfit.seguridad.Rol;

/**
 * Capa MODELO: persona que usa el sistema (asesor, administrador, entrenador).
 * La contraseña nunca se guarda en texto plano: solo su hash BCrypt.
 */
public class Usuario {

    private final int idUsuario;
    private final String username;
    private final String passwordHash;
    private final String nombre;
    private final Rol rol;

    public Usuario(int idUsuario, String username, String passwordHash, String nombre, Rol rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.passwordHash = passwordHash;
        this.nombre = nombre;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getNombre() {
        return nombre;
    }

    public Rol getRol() {
        return rol;
    }
}
