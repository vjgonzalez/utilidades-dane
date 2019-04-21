package ar.uba.fi.utilidadesdane.calendario;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ar.uba.fi.utilidadesdane.R;
import ar.uba.fi.utilidadesdane.utils.FechaUtils;

/**
 * Vista de calendario con objetos de tipo {@link ObjetoCalendarizado} asociados a sus fechas.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class CalendarioView extends LinearLayout {

    /**
     * Cantidad de celdas del calendario.
     */
    private static final int MAX_CELDA = 42;

    /**
     * Contexto donde se utiliza el calendario.
     */
    private Context context;

    /**
     * Layout del calendario.
     */
    private View view;

    /**
     * Fechas del mes visualizado.
     */
    private Calendar mesActual = Calendar.getInstance();

    /**
     * Clase del {@link ObjetoCalendarizado} asociado a las fechas del calendario.
     */
    private Class claseObjetoCalendarizado;

    /**
     * Objeto que contiene la acción a realizar al seleccionar una fecha.
     */
    private OnSeleccionFechaListener onSeleccionFechaListener;

    /**
     * Posición de la fecha seleccionada por el usuario. -1 antes de seleccionar una fecha.
     */
    private int posicionActual;

    /**
     * Color de las celdas de fechas correspondientes al mes seleccionado.
     */
    private String colorMesActual = "#000000";

    /**
     * Color de las celdas de fechas correspondientes al mes anterior o siguiente.
     */
    private String colorOtroMes = "#cccccc";

    /**
     * Color de la celda de fecha seleccionada.
     */
    private String colorFechaSeleccionada = "#FCC058";

    /**
     * Color del marcador de celdas de fechas que contienen algún {@link ObjetoCalendarizado} asociado.
     */
    private String colorFechaConEvento = "#8e5572";

    /**
     * Constructor requerido para utilizar la vista en un layout.
     *
     * @param context contexto
     * @param attrs   Atributos tomados del XML
     */
    public CalendarioView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.calendario_layout, this);
        posicionActual = -1;
        marcarFechaActual();
        setMesAnteriorListener();
        setMesSiguienteListener();
        setClickCeldaListener();

    }

    /**
     * Determina la clase del objeto que se almacena asociado a fechas del calendario.
     * Debe llamarse como primer paso luego de crear el objeto CalendarView.
     *
     * @param claseObjetoCalendarizado clase del objeto a almacenar en la base de datos (debe heredar de {@link ObjetoCalendarizado}
     */
    public void setClaseObjetoCalendarizado(Class<? extends ObjetoCalendarizado> claseObjetoCalendarizado) {
        this.claseObjetoCalendarizado = claseObjetoCalendarizado;
        setUpAdaptador();
    }

    /**
     * Asocia un listener {@link OnSeleccionFechaListener} a la acción de selección de fecha.
     *
     * @param onSeleccionFechaListener objeto que contiene la acción a realizar al seleccionar una fecha
     */
    public void setOnSeleccionFechaListener(OnSeleccionFechaListener onSeleccionFechaListener) {
        this.onSeleccionFechaListener = onSeleccionFechaListener;
    }

    /**
     * Lógica del botón de regreso. Debe llamarse en el constructor.
     */
    protected void setMesAnteriorListener() {
        ImageView botonMesAnterior = view.findViewById(R.id.mes_anterior);
        botonMesAnterior.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mesActual.add(Calendar.MONTH, -1);
                Calendar hoy = Calendar.getInstance();
                if (mesActual.get(Calendar.MONTH) == hoy.get(Calendar.MONTH))
                    marcarFechaActual();
                else
                    posicionActual = -1;
                setUpAdaptador();
            }
        });
    }

    /**
     * Lógica del botón de siguiente. Debe llamarse en el constructor.
     */
    protected void setMesSiguienteListener() {
        ImageView botonMesSiguiente = view.findViewById(R.id.mes_siguiente);
        botonMesSiguiente.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mesActual.add(Calendar.MONTH, 1);
                Calendar hoy = Calendar.getInstance();
                if (mesActual.get(Calendar.MONTH) == hoy.get(Calendar.MONTH))
                    marcarFechaActual();
                else
                    posicionActual = -1;
                setUpAdaptador();
            }
        });
    }

    /**
     * Lógica de selección de fecha. Debe llamarse en el constructor.
     */
    protected void setClickCeldaListener() {
        GridView calendarGridView = view.findViewById(R.id.cuadricula_calendario);
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onSeleccionFechaListener != null) {
                    posicionActual = position;
                    setUpAdaptador();
                    List<Date> fechas = getFechasActuales(position + 1);
                    Date fechaSeleccionada = fechas.get(position);
                    List<ObjetoCalendarizado> eventos = ObjetoCalendarizado.obtenerPorFecha(claseObjetoCalendarizado, fechaSeleccionada);
                    onSeleccionFechaListener.onSeleccionFechaListener(eventos, fechaSeleccionada);
                }
            }
        });
    }

    /**
     * Marca la fecha actual en la vista de calendario.
     */
    protected void marcarFechaActual() {
        Calendar mCal = (Calendar) mesActual.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        int posicion = 0;
        Date date;
        while (posicionActual == -1) {
            date = mCal.getTime();
            if (date.equals(mesActual.getTime())) {
                posicionActual = posicion;
            } else {
                mCal.add(Calendar.DAY_OF_MONTH, 1);
                posicion++;
            }
        }
    }

    /**
     * Obtiene las fechas visibles en el calendario.
     *
     * @param posicionMaxima posición de la última fecha que se desea obtener. En caso de requerir todas las fechas, debe tomar el valor de {@link #MAX_CELDA}.
     * @return lista con todas las fechas requeridas
     */
    protected List<Date> getFechasActuales(int posicionMaxima) {
        Calendar mCal = (Calendar) mesActual.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        List<Date> fechasAMostrarEnCeldas = new ArrayList<>();
        while (fechasAMostrarEnCeldas.size() < posicionMaxima) {
            fechasAMostrarEnCeldas.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return fechasAMostrarEnCeldas;
    }

    /**
     * Prepara las fechas a visualizar y consulta a la base de datos para marcar las fechas que tienen asociado algún {@link ObjetoCalendarizado}.
     * Debe llamarse en los constructores y cuando se cambia el mes.
     */
    protected void setUpAdaptador() {
        List<Date> fechasAMostrarEnCeldas = getFechasActuales(MAX_CELDA);
        String mesTexto = FechaUtils.convertirDateATextoMes(mesActual.getTime());
        TextView fechaActual = view.findViewById(R.id.mes_actual);
        fechaActual.setText(mesTexto);

        List<ObjetoCalendarizado> eventos = ObjetoCalendarizado.obtenerRangoFecha(claseObjetoCalendarizado, fechasAMostrarEnCeldas.get(0), fechasAMostrarEnCeldas.get(MAX_CELDA - 1));

        AdaptadorCalendario adaptador = new AdaptadorCalendario(context, fechasAMostrarEnCeldas, mesActual, eventos, posicionActual, colorMesActual, colorOtroMes, colorFechaConEvento, colorFechaSeleccionada);
        GridView calendarGridView = view.findViewById(R.id.cuadricula_calendario);
        calendarGridView.setAdapter(adaptador);
    }


    /**
     * Devuelve el color utilizado para mostrar en la grilla los días que corresponden al mes visualizado.
     *
     * @return Color utilizado
     */
    public String getColorMesActual() {
        return colorMesActual;
    }

    /**
     * Modifica el color utilizado para mostrar en la grilla los días que corresponden al mes visualizado.
     *
     * @param colorMesActual Color a utilizar
     */
    public void setColorMesActual(String colorMesActual) {
        this.colorMesActual = colorMesActual;
        setUpAdaptador();
    }

    /**
     * Devuelve el color utilizado para mostrar en la grilla los días que no corresponden al mes visualizado.
     *
     * @return Color utilizado
     */
    public String getColorOtroMes() {
        return colorOtroMes;
    }

    /**
     * Modifica el color utilizado para mostrar en la grilla los días que no corresponden al mes visualizado.
     *
     * @param colorOtroMes Color a utilizar
     */
    public void setColorOtroMes(String colorOtroMes) {
        this.colorOtroMes = colorOtroMes;
        setUpAdaptador();
    }

    /**
     * Devuelve el color utilizado para marcar en la grilla los días que contienen algún {@link ObjetoCalendarizado} asociado.
     *
     * @return Color utilizado
     */
    public String getColorFechaConEvento() {
        return colorFechaConEvento;
    }

    /**
     * Modifica el color utilizado para marcar en la grilla los días que contienen algún {@link ObjetoCalendarizado} asociado.
     *
     * @param colorFechaConEvento Color a utilizar
     */
    public void setColorFechaConEvento(String colorFechaConEvento) {
        this.colorFechaConEvento = colorFechaConEvento;
        setUpAdaptador();
    }

    /**
     * Devuelve el color utilizado para marcar en la grilla la fecha seleccionada.
     *
     * @return Color utilizado
     */
    public String getColorFechaSeleccionada() {
        return colorFechaSeleccionada;
    }

    /**
     * Modifica el color utilizado para marcar en la grilla la fecha seleccionada.
     *
     * @param colorFechaSeleccionada Color a utilizar
     */
    public void setColorFechaSeleccionada(String colorFechaSeleccionada) {
        this.colorFechaSeleccionada = colorFechaSeleccionada;
        setUpAdaptador();
    }

    /**
     * Modifica los colores utilizados en el calendario.
     *
     * @param colorMesActual         Color a utilizar para mostrar los días que corresponden al mes visualizado.
     * @param colorOtroMes           Color a utilizar para mostrar los días visibles que no corresponden al mes visualizado.
     * @param colorFechaConEvento    Color a utilizar para señalar una que contienen algún {@link ObjetoCalendarizado} asociado.
     * @param colorFechaSeleccionada Color a utilizar para marcar en la grilla la fecha seleccionada.
     */
    public void setColores(String colorMesActual, String colorOtroMes, String colorFechaConEvento, String colorFechaSeleccionada) {
        this.colorMesActual = colorMesActual;
        this.colorOtroMes = colorOtroMes;
        this.colorFechaConEvento = colorFechaConEvento;
        this.colorFechaSeleccionada = colorFechaSeleccionada;
        setUpAdaptador();
    }

    /**
     * Obtiene el tipo de ObjetoCalendarizado asociado a las fechas.
     *
     * @return Clase del ObjetoCalendarizado
     */
    public Class getClaseObjetoCalendarizado() {
        return claseObjetoCalendarizado;
    }

}
