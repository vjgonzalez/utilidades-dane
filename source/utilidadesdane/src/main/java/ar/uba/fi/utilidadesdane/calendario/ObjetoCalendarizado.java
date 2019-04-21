package ar.uba.fi.utilidadesdane.calendario;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import ar.uba.fi.utilidadesdane.notificaciones.Notificacion;
import ar.uba.fi.utilidadesdane.persistencia.ObjetoPersistente;
import ar.uba.fi.utilidadesdane.utils.FechaUtils;

/**
 * Representa un {@link ObjetoPersistente} asociado a una fecha y hora determinada. Utilizada por {@link CalendarioView}.
 * Puede extenderse para añadir atributos.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class ObjetoCalendarizado extends ObjetoPersistente {

    public static final int ID_SIN_NOTIFICACION = -1;

    /**
     * Fecha asociada al objeto, en formato SQL ("yyyy-MM-dd") para su almacenamiento en la base de datos
     */
    private String fecha;

    /**
     * Hora asociada al objeto, en formato SQL ("HH:mm:ss") para su almacenamiento en la base de datos
     */
    private String hora;

    /**
     * Indica si existe una notificación asociada a este evento. El valor debe setearlo el usuario, no busca notificaciones.
     */
    private int idNotificacion = ID_SIN_NOTIFICACION;

    /**
     * Constructor vacío necesario para {@link ObjetoPersistente}.
     */
    public ObjetoCalendarizado() {
    }

    /**
     * Constructor.
     *
     * @param fecha Fecha en formato SQL ("yyyy-MM-dd")
     * @param hora  Hora en formato SQL ("HH:mm:ss")
     */
    public ObjetoCalendarizado(String fecha, String hora) {
        super();
        this.fecha = fecha;
        this.hora = hora;
        save();
    }

    /**
     * Constructor.
     *
     * @param fechaObj Fecha y hora en formato de milisegundos desde 1 de enero de 1970, 00:00:00 GMT.
     */
    public ObjetoCalendarizado(long fechaObj) {
        super();
        fecha = FechaUtils.convertirLongAFechaSQL(fechaObj);
        hora = FechaUtils.convertirLongAHoraSQL(fechaObj);
        save();
    }

    /**
     * Constructor.
     *
     * @param fechaObj Fecha y hora
     */
    public ObjetoCalendarizado(Date fechaObj) {
        super();
        fecha = FechaUtils.convertirDateAFechaSQL(fechaObj);
        hora = FechaUtils.convertirDateAHoraSQL(fechaObj);
        save();
    }

    /**
     * Devuelve la fecha del objeto en formato "yyyy-MM-dd".
     *
     * @return Fecha en formato "yyyy-MM-dd"
     */
    public String getFecha() {
        return this.fecha;
    }

    /**
     * Devuelve la hora del objeto en formato "HH:mm:ss".
     *
     * @return Hora en formato "HH:mm:ss"
     */
    public String getHora() {
        return this.hora;
    }

    /**
     * Devuelve la hora del objeto en formato "HH:mm".
     *
     * @return Hora en formato "HH:mm"
     */
    public String getHoraSinSegundos() {
        return this.hora.substring(0, 5);
    }

    /**
     * Modifica la fecha del objeto.
     *
     * @param fecha Fecha en formato "yyyy-MM-dd"
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
        save();
    }

    /**
     * Modifica la hora del objeto.
     *
     * @param hora Hora en formato "HH:mm:ss"
     */
    public void setHora(String hora) {
        this.hora = hora;
        save();
    }

    /**
     * Modifica la fecha del objeto.
     *
     * @param fechaDate Nueva fecha
     */
    public void setFecha(Date fechaDate) {
        fecha = FechaUtils.convertirDateAFechaSQL(fechaDate);
        save();
    }

    /**
     * Modifica la hora del objeto.
     *
     * @param horaDate Nueva hora
     */
    public void setHora(Date horaDate) {
        hora = FechaUtils.convertirDateAHoraSQL(horaDate);
        save();
    }

    /**
     * Obtiene el ID de la notificación asociada al objeto.
     *
     * @return ID de la notificación.
     */
    public int getIdNotificacion() {
        return this.idNotificacion;
    }

    /**
     * Modifica el ID de la notificación asociada al objeto.
     *
     * @param idNotificacion Nuevo ID
     */
    public void setIdNotificacion(int idNotificacion) {
        this.idNotificacion = idNotificacion;
        save();
    }

    /**
     * Modifica el ID de la notificación asociada al objeto.
     *
     * @param notificacion Notificación asociada al objeto
     */
    public void setIdNotificacion(Notificacion notificacion) {
        this.idNotificacion = notificacion.getId();
        save();
    }

    /**
     * Verifica si el objeto tiene una notificación asociada.
     *
     * @return True si el objeto tiene una notificación asociada, de lo contrario devuelve false
     */
    public boolean tieneNotificacion() {
        return this.idNotificacion != ID_SIN_NOTIFICACION;
    }

    /**
     * Devuelve la fecha y hora del objeto en un objeto de tipo {@link Date}.
     *
     * @return Fecha y hora
     * @throws ParseException Excepción lanzada si hay errores en la conversión de un formato a otro
     */
    public Date getDate() throws ParseException {
        return FechaUtils.convertirStringADate(fecha, hora);
    }

    /**
     * Devuelve los objetos de la clase almacenados en la base de datos asociados a una fecha y hora determinadas.
     *
     * @param clase       Objeto {@link Class} que representa la clase del {@link ObjetoCalendarizado} utilizado
     * @param fechaBuscar Fecha para la búsqueda en formato "yyyy-MM-dd"
     * @param horaBuscar  Hora para la búsqueda en formato "HH:mm:ss"
     * @return Lista con los objetos de la clase del parámetro clase
     */
    public static <T> List<T> obtenerPorFechayHora(Class<T> clase, String fechaBuscar, String horaBuscar) {
        return ObjetoPersistente.encontrar(clase, "fecha = ? and hora = ?", fechaBuscar, horaBuscar);
    }

    /**
     * Devuelve los objetos de la clase almacenados en la base de datos asociados a una fecha, sin especificar la hora.
     *
     * @param clase       Objeto {@link Class} que representa la clase del {@link ObjetoCalendarizado} utilizado
     * @param fechaBuscar Fecha para la búsqueda en formato "yyyy-MM-dd"
     * @return Lista con los objetos de la clase del parámetro clase
     */
    public static <T> List<T> obtenerPorFecha(Class<T> clase, String fechaBuscar) {
        return ObjetoPersistente.encontrar(clase, "fecha = ? order by fecha asc, hora asc", fechaBuscar);
    }

    /**
     * Devuelve los objetos de la clase almacenados en la base de datos asociados a una fecha y hora determinadas por un objeto de tipo {@link Date}.
     *
     * @param clase       Objeto {@link Class} que representa la clase del {@link ObjetoCalendarizado} utilizado
     * @param fechaBuscar Fecha para la búsqueda
     * @return Lista con los objetos de la clase del parámetro clase
     */
    public static <T> List<T> obtenerPorFecha(Class<T> clase, Date fechaBuscar) {
        String fechaStr = FechaUtils.convertirDateAFechaSQL(fechaBuscar);
        return obtenerPorFecha(clase, fechaStr);
    }

    /**
     * Devuelve los objetos de la clase almacenados en la base de datos asociados a fechas comprendidas dentro de un intervalo.
     * Los parámetros de fecha pueden estar dados en cualquier orden (la menor primero o la mayor primero).
     *
     * @param clase        Objeto {@link Class} que representa la clase del {@link ObjetoCalendarizado} utilizado
     * @param primeraFecha Primera fecha del intervalo.
     * @param segundaFecha Segunda fecha del intervalo.
     * @return Lista con los objetos de la clase del parámetro clase
     */
    public static <T> List<T> obtenerRangoFecha(Class<T> clase, Date primeraFecha, Date segundaFecha) {
        String fecha1;
        String fecha2;
        if (primeraFecha.compareTo(segundaFecha) <= 0) {
            fecha1 = FechaUtils.convertirDateAFechaSQL(primeraFecha);
            fecha2 = FechaUtils.convertirDateAFechaSQL(segundaFecha);
        } else {
            fecha1 = FechaUtils.convertirDateAFechaSQL(segundaFecha);
            fecha2 = FechaUtils.convertirDateAFechaSQL(primeraFecha);
        }
        return ObjetoCalendarizado.encontrar(clase, "fecha between ? and ? order by fecha asc, hora asc", fecha1, fecha2);
    }

    /**
     * Devuelve los objetos de la clase almacenados en la base de datos asociados fechas posteriores a la fecha indicada.
     *
     * @param clase       Objeto {@link Class} que representa la clase del {@link ObjetoCalendarizado} utilizado
     * @param fechaActual Fecha para la búsqueda en formato "yyyy-MM-dd"
     * @return Lista con los objetos de la clase del parámetro clase
     */
    public static <T> List<T> obtenerEventosPosteriores(Class<T> clase, String fechaActual) {
        return ObjetoCalendarizado.encontrar(clase, "fecha >= ?", fechaActual);
    }

    /**
     * Devuelve los objetos de la clase almacenados en la base de datos asociados fechas posteriores a la fecha indicada.
     *
     * @param clase       Objeto {@link Class} que representa la clase del {@link ObjetoCalendarizado} utilizado
     * @param fechaActual Fecha para la búsqueda. Sólo se considera la fecha para la búsqueda, no la hora.
     * @return Lista con los objetos de la clase del parámetro clase
     */
    public static <T> List<T> obtenerEventosPosteriores(Class<T> clase, Date fechaActual) {
        String fechaStr = FechaUtils.convertirDateAFechaSQL(fechaActual);
        return ObjetoCalendarizado.encontrar(clase, "fecha >= ?", fechaStr);
    }

}
