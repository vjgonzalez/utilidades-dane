package ar.uba.fi.utilidadesdane.notificaciones;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.util.Date;

import ar.uba.fi.utilidadesdane.calendario.ObjetoCalendarizado;
import ar.uba.fi.utilidadesdane.utils.FechaUtils;

/**
 * Permite crear alarmas en determinadas fechas a partir de notificaciones.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class AdministradorNotificaciones {

    /**
     * Crea una alarma asociada a una notificación, que despertará al dispositivo si este está en modo sleep.
     *
     * @param notificacion Notificación
     * @param fecha        Fecha en la que se disparará la alarma
     * @param context      Contexto
     * @return True si se logró crear la alarma, false de lo contrario.
     */
    public boolean agendarNotificacionConWakeUp(Notificacion notificacion, Date fecha, Context context) {
        long fechaNotificacion = FechaUtils.convertirDateALong(fecha);
        return agendarNotificacion(notificacion, fechaNotificacion, context, AlarmManager.RTC_WAKEUP);
    }

    /**
     * Crea una alarma asociada a una notificación, que no despertará al dispositivo si este está en modo sleep.
     *
     * @param notificacion Notificación
     * @param fecha        Fecha en la que se disparará la alarma
     * @param context      Contexto
     * @return True si se logró crear la alarma, false de lo contrario.
     */
    public boolean agendarNotificacionSinWakeUp(Notificacion notificacion, Date fecha, Context context) {
        long fechaNotificacion = FechaUtils.convertirDateALong(fecha);
        return agendarNotificacion(notificacion, fechaNotificacion, context, AlarmManager.RTC);
    }

    /**
     * Crea una alarma asociada a una notificación, que despertará al dispositivo si este está en modo sleep.
     *
     * @param notificacion Notificación
     * @param fecha        Fecha en la que se disparará la alarma
     * @param context      Contexto
     * @return True si se logró crear la alarma, false de lo contrario.
     */
    public boolean agendarNotificacionConWakeUp(Notificacion notificacion, long fecha, Context context) {
        return agendarNotificacion(notificacion, fecha, context, AlarmManager.RTC_WAKEUP);
    }

    /**
     * Crea una alarma asociada a una notificación, que no despertará al dispositivo si este está en modo sleep.
     *
     * @param notificacion Notificación
     * @param fecha        Fecha en la que se disparará la alarma
     * @param context      Contexto
     * @return True si se logró crear la alarma, false de lo contrario.
     */
    public boolean agendarNotificacionSinWakeUp(Notificacion notificacion, long fecha, Context context) {
        return agendarNotificacion(notificacion, fecha, context, AlarmManager.RTC);
    }

    /**
     * Crea una alarma asociada a una notificación, que despertará al dispositivo si este está en modo sleep.
     *
     * @param notificacion        Notificación
     * @param objetoCalendarizado Objeto asociado a la notificación. Su fecha se utiliza para agendar la alarma.
     * @param context             Contexto
     * @return True si se logró crear la alarma, false de lo contrario.
     */
    public boolean agendarNotificacionConWakeUp(Notificacion notificacion, ObjetoCalendarizado objetoCalendarizado, Context context) throws ParseException {
        Date fecha = objetoCalendarizado.getDate();
        if (fecha != null)
            return agendarNotificacion(notificacion, FechaUtils.convertirDateALong(fecha), context, AlarmManager.RTC_WAKEUP);
        else
            return false;
    }

    /**
     * Crea una alarma asociada a una notificación, que no despertará al dispositivo si este está en modo sleep.
     *
     * @param notificacion        Notificación
     * @param objetoCalendarizado Objeto asociado a la notificación. Su fecha se utiliza para agendar la alarma.
     * @param context             Contexto
     * @return True si se logró crear la alarma, false de lo contrario.
     */
    public boolean agendarNotificacionSinWakeUp(Notificacion notificacion, ObjetoCalendarizado objetoCalendarizado, Context context) throws ParseException {
        Date fecha = objetoCalendarizado.getDate();
        if (fecha != null)
            return agendarNotificacion(notificacion, FechaUtils.convertirDateALong(fecha), context, AlarmManager.RTC);
        else
            return false;
    }

    /**
     * Crea la alarma asociada a una notificación
     *
     * @param notificacion      Notificación
     * @param fechaNotificacion Fecha en la que se disparará la alarma
     * @param context           Contexto
     * @param tipoAlarma        Tipo de alarma. Definida por constantes de la clase {@link AlarmManager}. Posibles valores: RTC_WAKEUP, RTC, ELAPSED_REALTIME_WAKEUP or ELAPSED_REALTIME.
     * @return True si se logró crear la alarma, false de lo contrario.
     */
    private boolean agendarNotificacion(Notificacion notificacion, long fechaNotificacion, Context context, int tipoAlarma) {
        if (notificacion != null && notificacion.getNotification() != null) {
            PendingIntent pendingIntent = crearIntent(context, notificacion);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(tipoAlarma, fechaNotificacion, pendingIntent);
            return true;
        } else
            return false;
    }

    /**
     * Crea el objeto intent de broadcast a partir de una notificación.
     *
     * @param context      Contexto
     * @param notificacion Notificación
     * @return Intent asociado a la notificación
     */
    private PendingIntent crearIntent(Context context, Notificacion notificacion) {
        Intent notificationIntent = new Intent(context, Receptor.class);
        notificationIntent.putExtra(Receptor.NOTIFICACION_ID, notificacion.getId());
        notificationIntent.putExtra(Receptor.NOTIFICACION, notificacion.getNotification());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificacion.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /**
     * Crea el objeto intent de broadcast para cancelar una notificación por su ID.
     *
     * @param context        Contexto
     * @param idNotificacion ID de la notificación a cancelar
     * @return Intent asociado al ID de notificación
     */
    private PendingIntent crearIntentParaCancelacion(Context context, int idNotificacion) {
        Intent notificationIntent = new Intent(context, Receptor.class);
        notificationIntent.putExtra(Receptor.NOTIFICACION_ID, idNotificacion);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, idNotificacion, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    /**
     * Cancela la alarma asociada a una notificación para que no se genere.
     *
     * @param context      Contexto
     * @param notificacion Notificación que desea cancelarse
     */
    public void cancelarNotificacion(Context context, Notificacion notificacion) {
        cancelarNotificacion(context, notificacion.getId());
    }

    /**
     * Cancela la alarma asociada a una notificación para que no se genere.
     *
     * @param context        Contexto
     * @param idNotificacion ID de la notificación que desea cancelarse
     */
    public void cancelarNotificacion(Context context, int idNotificacion) {
        PendingIntent intent = crearIntentParaCancelacion(context, idNotificacion);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(intent);
        intent.cancel();
    }
}
