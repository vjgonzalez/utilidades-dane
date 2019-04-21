package ar.uba.fi.pruebasutilidadesdane.calendario;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ar.uba.fi.pruebasutilidadesdane.R;
import ar.uba.fi.utilidadesdane.calendario.CalendarioView;
import ar.uba.fi.utilidadesdane.calendario.ObjetoCalendarizado;
import ar.uba.fi.utilidadesdane.calendario.OnSeleccionFechaListener;
import ar.uba.fi.utilidadesdane.persistencia.DBUtils;
import ar.uba.fi.utilidadesdane.utils.FechaUtils;

/**
 * Created by Vir on 25-Jun-17.
 */

public class PruebaCalendarioMain extends AppCompatActivity implements OnSeleccionFechaListener {

    CalendarioView calendarioView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inicializarBD();

        setContentView(R.layout.prueba_calendario_main);
        calendarioView = findViewById(R.id.calendario_view3);
        calendarioView.setClaseObjetoCalendarizado(PruebaObjetoCalendarizado.class);
        calendarioView.setOnSeleccionFechaListener(this);

    }

    private void inicializarBD() {
        /*Elimina objetos de la BD*/
        ObjetoCalendarizado.borrarTodos(PruebaObjetoCalendarizado.class);

        /*Para crear objetos en la BD*/
        PruebaObjetoCalendarizado pruebaObjetoCalendarizado = new PruebaObjetoCalendarizado(new Date().getTime(), "Texto evento 1");
        DateFormat df = new SimpleDateFormat("hh:mm:ss_yyyy.MM.dd");
        try {
            Date date2 = df.parse("00:00:00_2019.01.01");
            PruebaObjetoCalendarizado pruebaObjetoCalendarizado2 = new PruebaObjetoCalendarizado(date2.getTime(), "Texto evento 2");
            Date date3 = df.parse("19:10:55_2019.01.02");
            PruebaObjetoCalendarizado pruebaObjetoCalendarizado3 = new PruebaObjetoCalendarizado(date3.getTime(), "Texto evento 3");
            Date date4 = df.parse("00:04:00_2019.01.03");
            PruebaObjetoCalendarizado pruebaObjetoCalendarizado4 = new PruebaObjetoCalendarizado(date4.getTime(), "Texto evento 4");
            Date date5 = df.parse("00:00:00_2019.01.03");
            PruebaObjetoCalendarizado pruebaObjetoCalendarizado5 = new PruebaObjetoCalendarizado(date5.getTime(), "Texto evento 5");
            Date date6 = df.parse("20:00:00_2019.01.04");
            PruebaObjetoCalendarizado pruebaObjetoCalendarizado6 = new PruebaObjetoCalendarizado(date6.getTime(), "Texto evento 6");

            DBUtils.dump("Sugar.db", getPackageName(), "DebugSugar.db", this);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSeleccionFechaListener(List<ObjetoCalendarizado> eventos, Date fechaSeleccionada) {
        String textoFecha = FechaUtils.convertirDateATexto(fechaSeleccionada);
        TextView textView = findViewById(R.id.textoFecha);
        textView.setText(textoFecha);

        int i = 0;
        String textoAMostrar = "";
        while (i < eventos.size()) {
            textoAMostrar += "\n" + ((PruebaObjetoCalendarizado) eventos.get(i)).getOtroDato();
            i++;
        }

        TextView textViewObjetos = findViewById(R.id.objetos);
        textViewObjetos.setText(textoAMostrar);
    }


}
