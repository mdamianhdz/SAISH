package org.apli.tickets;

/**
 *
 * @author mdamianhdz
 */
public class Paciente {

    private int folio;
    private String nombre;//Nombre completo de paciente
    private String sApellidoMaterno;
    private String sApellidoPaterno;
    private String sNombre;

    public String getNombreCompleto() {
        return this.sApellidoPaterno
                + (this.sApellidoMaterno == null || this.sApellidoMaterno.equals("") ? "" : " " + this.sApellidoMaterno)
                + (this.sNombre == null || this.sNombre.equals("") ? "" : " " + this.sNombre);
    }

    public int getFolio() {
        return folio;
    }

    public void setFolio(int folio) {
        this.folio = folio;
    }

    public String getApellidoMaterno() {
        return sApellidoMaterno;
    }

    public void setApellidoMaterno(String sApellidoMaterno) {
        this.sApellidoMaterno = sApellidoMaterno;
    }

    public String getApellidoPaterno() {
        return sApellidoPaterno;
    }

    public void setApellidoPaterno(String sApellidoPaterno) {
        this.sApellidoPaterno = sApellidoPaterno;
    }

    public String getNombrePac() {
        return sNombre;
    }

    public void setNombrePac(String sNombre) {
        this.sNombre = sNombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Paciente{" + "folio=" + folio + ", nombre=" + nombre + ", sApellidoMaterno=" + sApellidoMaterno + ", sApellidoPaterno=" + sApellidoPaterno + ", sNombre=" + sNombre + '}';
    }

    public Paciente() {
    }

}
