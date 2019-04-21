package ar.uba.fi.utilidadesdane.calendario;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.uba.fi.utilidadesdane.R;

/**
 * Adaptador para mostrar la cuadrícula de la vista {@link CalendarioView}
 *
 * @author Virginia González y Alfredo Hodes
 */
public class AdaptadorCalendario extends ArrayAdapter {

    /**
     * Fechas que muestra el calendario en su vista actual.
     */
    private List<Date> fechasAMostrar;

    /**
     * Mes actual.
     */
    private Calendar mesActual;

    /**
     * Lista de {@link ObjetoCalendarizado} asociados a las fechas de la vista actual.
     */
    private List<ObjetoCalendarizado> eventos;

    /**
     * Posición correspondiente a la fecha seleccionada por el usuario (-1 cuando no se selecciona ninguna fecha).
     */
    private int posicionSeleccionada;

    /**
     * Inflater de la vista.
     */
    private LayoutInflater layoutInflater;

    /**
     * Color de las celdas de fechas correspondientes al mes seleccionado.
     */
    private String colorMesActual;

    /**
     * Color de las celdas de fechas correspondientes al mes anterior o siguiente.
     */
    private String colorOtroMes;

    /**
     * Color de la celda de fecha seleccionada.
     */
    private String colorFechaSeleccionada;

    /**
     * Color del marcador de celdas de fechas que contienen algún {@link ObjetoCalendarizado} asociado.
     */
    private String colorFechaConEvento;

    /**
     * Constructor.
     *
     * @param context              Contexto donde se utiliza el calendario
     * @param fechasAMostrar       Lista de fechas que debe mostrar el calendario en su vista actual
     * @param mesActual            Mes actual
     * @param eventos              Lista de {@link ObjetoCalendarizado} asociados a las fechas de la vista actual
     * @param posicionSeleccionada Posición correspondiente a la fecha seleccionada por el usuario. -1 si no se seleccionó una fecha
     */
    public AdaptadorCalendario(Context context, List<Date> fechasAMostrar, Calendar mesActual, List<ObjetoCalendarizado> eventos, int posicionSeleccionada, String colorMesActual, String colorOtroMes, String colorFechaConEvento, String colorFechaSeleccionada) {
        super(context, R.layout.calendario_celda_layout);
        this.fechasAMostrar = fechasAMostrar;
        this.mesActual = mesActual;
        this.eventos = eventos;
        this.layoutInflater = LayoutInflater.from(context);
        this.posicionSeleccionada = posicionSeleccionada;
        this.colorMesActual = colorMesActual;
        this.colorOtroMes = colorOtroMes;
        this.colorFechaConEvento = colorFechaConEvento;
        this.colorFechaSeleccionada = colorFechaSeleccionada;
    }

    /**
     * @see android.widget.ArrayAdapter#getView(int, View, ViewGroup)
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Date mDate = fechasAMostrar.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        int displayYear = dateCal.get(Calendar.YEAR);
        int currentMonth = mesActual.get(Calendar.MONTH) + 1;
        int currentYear = mesActual.get(Calendar.YEAR);
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.calendario_celda_layout, parent, false);
        }
        if (position == posicionSeleccionada)
            view.setBackgroundColor(Color.parseColor(colorFechaSeleccionada));

        TextView cellNumber = view.findViewById(R.id.numero_celda);

        if (displayMonth == currentMonth && displayYear == currentYear) {
            cellNumber.setTextColor(Color.parseColor(colorMesActual));
        } else {
            cellNumber.setTextColor(Color.parseColor(colorOtroMes));
        }

        cellNumber.setText(String.valueOf(dayValue));

        TextView eventIndicator = view.findViewById(R.id.marca_evento);
        Calendar eventCalendar = Calendar.getInstance();
        for (int i = 0; i < eventos.size(); i++) {
            try {
                eventCalendar.setTime(eventos.get(i).getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
                    && displayYear == eventCalendar.get(Calendar.YEAR)) {
                eventIndicator.setBackgroundColor(Color.parseColor(colorFechaConEvento));
            }
        }
        return view;
    }

    /**
     * Método heredado de la clase ArrayAdapter. Devuelve la cantidad de fechas a mostrar.
     *
     * @return Cantidad de items del Array
     * @see ArrayAdapter#getCount()
     */
    @Override
    public int getCount() {
        return fechasAMostrar.size();
    }

    /**
     * Método heredado de la clase ArrayAdapter. Devuelve la fecha asociada a una posición del Array.
     *
     * @param position Posición del item en el Array
     * @return Objeto (de tipo {@link Date} ) asociado a la posición solicitada
     * @see ArrayAdapter#getItem(int)
     */
    @Nullable
    @Override
    public Object getItem(int position) {
        return fechasAMostrar.get(position);
    }

    /**
     * Método heredado de la clase ArrayAdapter. Devuelve la posición de una fecha en el Array.
     *
     * @param item Objeto (de tipo {@link Date} ) para el cual se desea conocer la posición en el Array
     * @return Posición del item en el Array. -1 si el item no está presente en el array.
     * @see ArrayAdapter#getPosition(Object)
     */
    @Override
    public int getPosition(Object item) {
        if (item instanceof Date)
            return fechasAMostrar.indexOf(item);
        else return -1;
    }

    /**
     * Modifica el listado de objetos {@link ObjetoCalendarizado}.
     *
     * @param events Listado nuevo
     */
    public void setEvents(List<ObjetoCalendarizado> events) {
        eventos = events;
    }

}