package ar.uba.fi.pruebasutilidadesdane.calendario;

import ar.uba.fi.utilidadesdane.calendario.ObjetoCalendarizado;

/**
 * Created by Vir on 11-Jun-17.
 */

public class PruebaObjetoCalendarizado extends ObjetoCalendarizado {

    private String otroDato;

    public static String datoDefault = "Objeto Prueba";

    public PruebaObjetoCalendarizado() {
        super();
    }

    public PruebaObjetoCalendarizado(long fecha) {
        super(fecha);
        otroDato = datoDefault;
        save();
    }

    public PruebaObjetoCalendarizado(long fecha, String otroDato) {
        super(fecha);
        this.otroDato = otroDato;
        save();
    }

    public String getOtroDato() {
        return otroDato;
    }
}
