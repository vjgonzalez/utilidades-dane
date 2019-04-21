package ar.uba.fi.utilidadesdane.cuestionario;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ar.uba.fi.utilidadesdane.R;

/**
 * Adaptador para mostrar los items {@link Opcion} asociados a una {@link Pregunta} de un {@link Cuestionario}
 *
 * @author Virginia González y Alfredo Hodes
 */
public class AdaptadorOpcionesCuestionario extends BaseAdapter {

    /**
     * Contexto donde se utilizan las opciones.
     */
    private Context context;

    /**
     * Lista de opciones a mostrar.
     */
    private List<Opcion> opciones;

    /**
     * ID del recurso de layout a utilizar para mostar cada opción.
     */
    private int itemRespuestaLayoutId;

    /**
     * Constructor.
     *
     * @param opciones              Opciones a mostrar
     * @param context               Contexto donde se utilizan las opciones
     * @param itemRespuestaLayoutId ID del recurso de layout a utilizar para mostrar cada opción
     */
    public AdaptadorOpcionesCuestionario(List<Opcion> opciones, Context context, int itemRespuestaLayoutId) {
        this.opciones = opciones;
        this.context = context;
        this.itemRespuestaLayoutId = itemRespuestaLayoutId;
    }

    /**
     * Método heredado de la clase BaseAdapter. Devuelve la cantidad de items del opciones disponibles.
     *
     * @return Cantidad de items de la lista
     * @see BaseAdapter#getCount()
     */
    @Override
    public int getCount() {
        return opciones.size();
    }

    /**
     * Método heredado de la clase BaseAdapter. Devuelve la opción asociada a una posición de la lista.
     *
     * @param position Posición del item en la lista
     * @return Objeto (de tipo {@link Opcion} ) asociado a la posición solicitada
     * @see BaseAdapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return opciones.get(position);
    }

    /**
     * Devuelve el ID de la opcion asociada a una posición de la lista.
     *
     * @param position Posición del item en la lista
     * @return ID de la opción asociada a la posición solicitada
     * @see BaseAdapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @see BaseAdapter#getView(int, View, ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View itemOpcion = convertView;

        if (itemOpcion == null)
            itemOpcion = LayoutInflater.from(context).inflate(itemRespuestaLayoutId, parent, false);

        Opcion opcion = (Opcion) getItem(position);

        TextView textoOpcion = itemOpcion.findViewById(R.id.opcion);
        textoOpcion.setText(opcion.getTextoOpcion());

        return itemOpcion;
    }
}

