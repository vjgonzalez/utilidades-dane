package ar.uba.fi.utilidadesdane.notificaciones;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Recibe y maneja Intents relacionados a notificaciones. Muestra la notificación en el momento indicado.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class Receptor extends BroadcastReceiver {

    /**
     * Constante a añadir al {@link Intent}, asociada al valor del identificador del objeto {@link Notificacion}
     */
    public static String NOTIFICACION_ID = "notification-id";

    /**
     * Constante a añadir al {@link Intent}, asociada al objeto {@link Notification} del objeto {@link Notificacion}
     */
    public static String NOTIFICACION = "notificacion";


    /**
     * Muestra una {@link Notificacion} en la fecha y hora programada
     *
     * @param context Contexto
     * @param intent  Intent con la notificación a mostrar y la acción a realizar al abrirla
     * @see BroadcastReceiver#onReceive(Context, Intent)
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICACION);
        int id = intent.getIntExtra(NOTIFICACION_ID, 0);
        notificationManager.notify(id, notification);
    }
}
