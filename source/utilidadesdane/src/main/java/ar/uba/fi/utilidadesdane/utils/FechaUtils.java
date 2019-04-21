package ar.uba.fi.utilidadesdane.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase de utilidades para conversión de formatos de fechas y horas.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class FechaUtils {

    /**
     * Formato de fecha para su almacenamiento en base de datos ("yyyy-MM-dd")
     */
    protected static final SimpleDateFormat formatoFechaSQL = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Formato de hora para su almacenamiento en base de datos ("HH:mm:ss")
     */
    protected static final SimpleDateFormat formatoHoraSQL = new SimpleDateFormat("HH:mm:ss");

    /**
     * Formato de fecha y hora para su almacenamiento en base de datos ("yyyy-MM-dd HH:mm:ss")
     */
    protected static final SimpleDateFormat formatoFechayHoraSQL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Formato de mes y año para mostrar como texto (Ejemplo: "Agosto 2017")
     */
    protected static final SimpleDateFormat formatoMesTexto = new SimpleDateFormat("MMMM yyyy");

    /**
     * Formato de fecha para mostrar como texto ("dd/MM/yyyy")
     */
    protected static final SimpleDateFormat formatoFechaTexto = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Listado de meses del año con su nombre en texto en español
     */
    protected static String[] meses = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
            "Septiembre", "Octubre", "Noviembre", "Diciembre"};

    protected static final String regexFechaSQL = "\\d{4}\\-\\d{2}\\-\\d{2}";

    /**
     * Convierte una fecha en formato de milisegundos a un String con la fecha en formato de almacenamiento en la base de datos ("yyyy-MM-dd").
     *
     * @param fechaLong Fecha en formato de milisegundos desde 1 de enero de 1970, 00:00:00 GMT
     * @return Fecha en formato "yyyy-MM-dd"
     */
    public static String convertirLongAFechaSQL(long fechaLong) {
        Date date = new Date(fechaLong);
        return formatoFechaSQL.format(date);
    }

    /**
     * Convierte una fecha en formato de milisegundos a un String con la hora en formato de almacenamiento en la base de datos ("HH:mm:ss")
     *
     * @param fechaLong Fecha en formato de milisegundos desde 1 de enero de 1970, 00:00:00 GMT
     * @return Hora en formato "HH:mm:ss"
     */
    public static String convertirLongAHoraSQL(long fechaLong) {
        Date date = new Date(fechaLong);
        return formatoHoraSQL.format(date);
    }

    /**
     * Convierte una fecha en formato {@link Date} a milisegundos desde 1 de enero de 1970, 00:00:00 GMT
     *
     * @param fechaDate Fecha a convertir
     * @return Fecha en milisegundos desde 1 de enero de 1970, 00:00:00 GMT. -1 si el parámetro es nulo
     */
    public static long convertirDateALong(Date fechaDate) {
        if (fechaDate == null)
            return -1;
        return fechaDate.getTime();
    }

    /**
     * Convierte una fecha en formato {@link Date} a un String con la fecha en formato de almacenamiento en la base de datos ("yyyy-MM-dd").
     *
     * @param fechaDate Fecha a convertir
     * @return Fecha en formato "yyyy-MM-dd". Null si el parámetro es nulo
     */
    public static String convertirDateAFechaSQL(Date fechaDate) {
        if (fechaDate == null)
            return null;
        return formatoFechaSQL.format(fechaDate);
    }

    /**
     * Convierte una fecha en formato {@link Date} a un String con la hora en formato de almacenamiento en la base de datos ("HH:mm:ss").
     *
     * @param fechaDate Fecha a convertir
     * @return Hora en formato "HH:mm:ss". Null si el parámetro es nulo
     */
    public static String convertirDateAHoraSQL(Date fechaDate) {
        if (fechaDate == null)
            return null;
        return formatoHoraSQL.format(fechaDate);
    }

    /**
     * Convierte una fecha en formato {@link Date} a fecha para mostrar como texto ("dd/MM/yyyy")
     *
     * @param fecha Fecha a convertir
     * @return Fecha en formato "dd/MM/yyyy". Null si el parámetro es nulo
     */
    public static String convertirDateATexto(Date fecha) {
        if (fecha == null)
            return null;
        return formatoFechaTexto.format(fecha);
    }

    /**
     * Convierte una fecha en formato {@link Date} a nombre de mes y año para mostrar como texto (Ejemplo: "Agosto 2017")
     *
     * @param fecha Fecha a convertir
     * @return Mes en formato texto y año "MMMM yyyy". Null si el parámetro es nulo
     */
    public static String convertirDateATextoMes(Date fecha) {
        if (fecha == null)
            return null;
        String fechaStr = formatoMesTexto.format(fecha).toUpperCase();
        return fechaStr;
    }

    /**
     * Convierte fecha y hora en formato de almacenamiento en la base de datos ("yyyy-MM-dd", "HH:mm:ss") a objeto {@link Date}
     *
     * @param fecha Fecha en formato de almacenamiento en la base de datos ("yyyy-MM-dd")
     * @param hora  Hora en formato de almacenamiento en la base de datos ("HH:mm:ss")
     * @return Fecha y hora convertidas. Null si algún parámetro es nulo
     * @throws ParseException Excepción lanzada si hay errores en la conversión de un formato a otro
     */
    public static Date convertirStringADate(String fecha, String hora) throws ParseException {
        if (fecha == null || hora == null)
            return null;
        String fechaYHora = fecha + " " + hora;
        Date date = formatoFechayHoraSQL.parse(fechaYHora);
        return date;
    }

    /**
     * Convierte fecha en formato de almacenamiento en la base de datos ("yyyy-MM-dd") a texto ("dd/MM/yyyy")
     *
     * @param fecha Fecha en formato de almacenamiento en la base de datos ("yyyy-MM-dd")
     * @return Fecha convertida. Null si el formato de la fecha es incorrecto
     */
    public static String convertirStringATexto(String fecha) {
        if (!fecha.matches(regexFechaSQL))
            return null;
        String[] partesFecha = fecha.split("-");
        String fechaTexto = partesFecha[2] + "/" + partesFecha[1] + "/" + partesFecha[0];
        return fechaTexto;

    }

    /**
     * Resta tiempo a una fecha
     *
     * @param fechaOriginal Fecha de la cual se parte
     * @param tiempoARestar Tiempo a restar en milisegundos
     * @return Fecha resultante de la resta en milisegundos. -1 si la fecha original es nula
     */
    public static long restarTiempo(Date fechaOriginal, long tiempoARestar) {
        if (fechaOriginal == null)
            return -1;
        long fechaOriginalLong = fechaOriginal.getTime();
        Date resultado = new Date(fechaOriginalLong - tiempoARestar);
        return resultado.getTime();
    }
}
