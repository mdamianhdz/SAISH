package org.apli.tickets;

/**
 *
 * @author mdamianhdz
 */
public class Usuario {

    private String usuario;
    private String nombre;

    public Usuario(String usuario, String nombre) {
        this.usuario = usuario;
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Usuario() {
    }
    

    @Override
    public String toString() {
        return "Usuario{" + "usuario=" + usuario + ", nombre=" + nombre + '}';
    }

}
