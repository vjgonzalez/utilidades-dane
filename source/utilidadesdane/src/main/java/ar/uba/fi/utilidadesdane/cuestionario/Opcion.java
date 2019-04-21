package ar.uba.fi.utilidadesdane.cuestionario;

/**
 * Representa una opción de una {@link Pregunta}.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class Opcion {

    /**
     * Texto de la opción.
     */
    private String textoOpcion;

    /**
     * Constructor con texto de la opción default.
     */
    public Opcion() {
        this("OPCIÓN NO SETEADA");
    }

    /**
     * Constructor.
     *
     * @param textoOpcion Texto de la opción
     */
    public Opcion(String textoOpcion) {
        this.textoOpcion = textoOpcion;
    }

    /**
     * Devuelve el texto de la opción.
     *
     * @return Texto de la opción
     */
    public String getTextoOpcion() {
        return textoOpcion;
    }

    /**
     * Modifica el texto de la opción.
     *
     * @param textoOpcion Texto de la opción
     */
    public void setTextoOpcion(String textoOpcion) {
        this.textoOpcion = textoOpcion;
    }

}
