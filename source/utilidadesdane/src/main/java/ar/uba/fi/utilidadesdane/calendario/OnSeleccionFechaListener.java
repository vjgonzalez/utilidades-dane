package ar.uba.fi.utilidadesdane.calendario;

import java.util.Date;
import java.util.List;

/**
 * Interface a ser implementada por la activity que contiene el calendario para definir una acción a realizar cuando
 * se selecciona una fecha.
 */
public interface OnSeleccionFechaListener {

    /**
     * Contiene la/s accion/es a realizar cuando se selecciona una fecha en el calendario.
     *
     * @param eventos           lista de objetos ObjetoCalendarizado guardados en la base de datos asociados a la fecha seleccionada
     * @param fechaSeleccionada fecha seleccionada
     */
    void onSeleccionFechaListener(List<ObjetoCalendarizado> eventos, Date fechaSeleccionada);
}
