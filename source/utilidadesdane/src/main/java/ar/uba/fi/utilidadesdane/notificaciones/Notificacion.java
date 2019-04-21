package ar.uba.fi.utilidadesdane.notificaciones;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.Pair;

import java.util.Random;

import ar.uba.fi.utilidadesdane.R;

/**
 * Representa un mensaje para mostrar al usuario en un momento determinado, independientemente de si está utilizando la aplicación.
 * Encapsula un objeto {@link Notification}
 *
 * @author Virginia González y Alfredo Hodes
 */
public class Notificacion {

    /**
     * Notificación.
     */
    private Notification notification;

    /**
     * Identificador de la notificación.
     */
    private int id;

    /**
     * Generador de números aleatorios.
     */
    private Random random = new Random();

    /**
     * Constructor.
     * Crea una notificación que ejecuta una Activity al ser abierta.
     *
     * @param context           Contexto
     * @param titulo            Título que debe mostrar la notificación
     * @param texto             Texto que debe mostrar la notificación
     * @param icono             Icono que debe mostrar la notificación
     * @param activityAEjecutar Activity que debe ejecutarse cuando el usuario abre la notificación
     */
    public Notificacion(Context context, String titulo, String texto, int icono, Class activityAEjecutar, Pair<String, String>... argsParaActivity) {
        crearNotification(context, titulo, texto, icono, activityAEjecutar, argsParaActivity);
        generarId();
    }

    /**
     * Constructor.
     * Crea una notificación a partir de un objeto {@link Notification}.
     *
     * @param notification Notification asociada
     */
    public Notificacion(Notification notification) {
        this.notification = notification;
        generarId();
    }

    /**
     * Crea la notificación. Debe ser llamado dentro de cualquier constructor.
     *
     * @param context           Contexto
     * @param titulo            Título que debe mostrar la notificación
     * @param texto             Texto que debe mostrar la notificación
     * @param icono             Icono que debe mostrar la notificación
     * @param activityAEjecutar Activity que debe ejecutarse cuando el usuario abre la notificación. Null si no se desea ejecutar algo.
     * @param argsParaActivity  Argumentos adicionales para enviar a la activityAEjecutar. El primer argumento del par es el nombre y el segundo el valor.
     */
    private void crearNotification(Context context, String titulo, String texto, int icono, Class activityAEjecutar, Pair<String, String>... argsParaActivity) {
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(titulo);
        builder.setContentText(texto);
        builder.setSmallIcon(icono);

        agregarChannelID(context, builder);

        if (activityAEjecutar != null) {
            Intent notifyIntent = new Intent(context, activityAEjecutar);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            if (argsParaActivity != null) {
                for (Pair arg : argsParaActivity)
                    notifyIntent.putExtra((String) arg.first, (String) arg.second);
            }

            PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(notifyPendingIntent);
        }
        notification = builder.build();
    }

    /**
     * Agrega ID del canal, necesario para notificaciones en SDK >= 26.
     *
     * @param context Contexto
     * @param builder Constructor de notificación
     * @return builder de la notificación modificado
     */
    private Notification.Builder agregarChannelID(Context context, Notification.Builder builder) {
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            String channelID = context.getResources().getString(R.string.app_name); // ID del canal
            CharSequence name = context.getResources().getString(R.string.app_name); // Nombre del canal (visible al usuario)
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel(channelID, name, importance);
            mNotificationManager.createNotificationChannel(mChannel);
            builder.setChannelId(channelID);
        }

        return builder;
    }

    /**
     * Obtiene el objeto {@link Notification} encapsulado
     *
     * @return Notification
     */
    public Notification getNotification() {
        return notification;
    }

    /**
     * Obtiene el identificador de la notificación
     *
     * @return Identificador de la notificación
     */
    public int getId() {
        return id;
    }


    /**
     * Genera un ID para la notificación
     */
    private void generarId() {
        id = random.nextInt(100000);
    }
}
