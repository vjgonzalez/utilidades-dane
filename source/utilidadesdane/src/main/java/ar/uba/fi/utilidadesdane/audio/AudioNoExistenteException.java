package ar.uba.fi.utilidadesdane.audio;

/**
 * Excepción lanzada por la clase {@link AudioFondo} cuando el audio que se desea reproducir no fue inicializado.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class AudioNoExistenteException extends Exception {

    /**
     * Crea la excepción sin un mensaje
     */
    public AudioNoExistenteException() {
    }

    /**
     * Crea la excepción con un mensaje de error
     *
     * @param mensaje detalla el error ocurrido
     */
    public AudioNoExistenteException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea la excepción con un mensaje de error y una causa
     *
     * @param mensaje detalle del error ocurrido
     * @param causa   causa del error
     */
    public AudioNoExistenteException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Crea la excepción con una causa
     *
     * @param causa causa del error
     */
    public AudioNoExistenteException(Throwable causa) {
        super(causa);
    }
}
