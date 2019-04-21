package ar.uba.fi.pruebasutilidadesdane.notificaciones;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.text.ParseException;
import java.util.Date;

import ar.uba.fi.pruebasutilidadesdane.R;
import ar.uba.fi.pruebasutilidadesdane.calendario.PruebaCalendarioMain;
import ar.uba.fi.pruebasutilidadesdane.calendario.PruebaObjetoCalendarizado;
import ar.uba.fi.utilidadesdane.calendario.ObjetoCalendarizado;
import ar.uba.fi.utilidadesdane.notificaciones.AdministradorNotificaciones;
import ar.uba.fi.utilidadesdane.notificaciones.Notificacion;
import ar.uba.fi.utilidadesdane.utils.FechaUtils;

/**
 * Created by Vir on 09-Jul-17.
 */

public class PruebaNotificacionesMain extends AppCompatActivity {
    Notificacion notificacion;
    AdministradorNotificaciones administradorNotificaciones;
    int idNotificacion = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba_notificaciones_main);
        administradorNotificaciones = new AdministradorNotificaciones();

        Button botonNotificacion = findViewById(R.id.boton_notificacion);
        botonNotificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarNotificacion();
            }
        });
        Button botonCancelar = findViewById(R.id.boton_cancelar);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelarNotificacion();
            }
        });
        Button botonNotificaciones = findViewById(R.id.boton_notificaciones);
        botonNotificaciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generarNotificaciones();
            }
        });

        Notificacion notificacionNoAgendada = new Notificacion(this, "RECORDATORIO", "Este es un recordatorio que no debería sonar", R.drawable.alarm, PruebaCalendarioMain.class);

    }

    private void generarNotificaciones() {
        /*Para crear objetos en la BD*/

        long tiempo1 = System.currentTimeMillis() + 5000;
        PruebaObjetoCalendarizado pruebaObjetoCalendarizado1 = new PruebaObjetoCalendarizado(tiempo1);
        PruebaObjetoCalendarizado pruebaObjetoCalendarizado2 = new PruebaObjetoCalendarizado(System.currentTimeMillis() + 7000);
        PruebaObjetoCalendarizado pruebaObjetoCalendarizado3 = new PruebaObjetoCalendarizado(System.currentTimeMillis() + 30000);
        Date date = new Date();
        try {
            date = FechaUtils.convertirStringADate("2018-05-24", "17:26:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long tiempo4 = date.getTime();
        PruebaObjetoCalendarizado pruebaObjetoCalendarizado4 = new PruebaObjetoCalendarizado(tiempo4);
        generarNotificacion(pruebaObjetoCalendarizado1, 1);
        generarNotificacion(pruebaObjetoCalendarizado2, 2);
        generarNotificacion(pruebaObjetoCalendarizado3, 3);
        generarNotificacion(pruebaObjetoCalendarizado4, 4);

    }

    private void generarNotificacion() {
        String titulo = "Titulo";
        String texto = "Texto";
        notificacion = new Notificacion(this, titulo, texto, R.drawable.alarm, PruebaCalendarioMain.class);
        long fechaNotificacion = System.currentTimeMillis() + 5000;
        administradorNotificaciones.agendarNotificacionConWakeUp(notificacion, fechaNotificacion, this);
        idNotificacion = notificacion.getId();
    }

    private void generarNotificacion(ObjetoCalendarizado objeto, int i) {
        String titulo = "Titulo " + i;
        String texto = "Texto " + i;
        Notificacion notificacion = new Notificacion(this, titulo, texto, R.drawable.alarm, null);
        AdministradorNotificaciones administradorNotificaciones = new AdministradorNotificaciones();
        try {
            administradorNotificaciones.agendarNotificacionConWakeUp(notificacion, objeto, this);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }
        objeto.setIdNotificacion(notificacion.getId());
    }

    private void cancelarNotificacion() {
        administradorNotificaciones.cancelarNotificacion(this, idNotificacion);
    }
}
