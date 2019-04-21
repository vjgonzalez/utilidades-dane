package ar.uba.fi.utilidadesdane.listas;

import android.util.Log;

import com.orm.dsl.Unique;

import java.util.List;

import ar.uba.fi.utilidadesdane.persistencia.ObjetoPersistente;

/**
 *
 * @author Virginia González y Alfredo Hodes
 */
public class Etiqueta extends ObjetoPersistente {

    /**
     * Nombre de la etiqueta. Debe ser único.
     */
    @Unique
    String nombre;

    /**
     * Constructor
     */
    public Etiqueta() {
        super();
    }

    /**
     * Constructor
     * @param nombre nombre de la etiqueta
     */
    public Etiqueta(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Cambia el nombre de la etiqueta
     * @param nuevoNombre nuevo nombre
     */
    public void renombrar(String nuevoNombre) {
        Log.d("DANE", "renombrar Etiqueta(" + nombre + ") -> " + nuevoNombre);
        this.nombre = nuevoNombre;
        save();
    }

    /**
     * Representación como String de la etiqueta
     * @return string que incluye el id y el nombre de la etiqueta
     */
    public String toString() {
        return "{Etiqueta(" + getId() + ") - Nombre: " + nombre + "}";
    }

    /**
     * busca una etiqueta por nombre y, si no existe, la crea e inicializa
     * @param nombreEtiqueta nombre
     * @return etiqueta inicializada (si no existe, es creada en el momento)
     */
    public static Etiqueta obtenerOCrear(String nombreEtiqueta) {
        Etiqueta etiqueta = obtener(nombreEtiqueta);
        if (etiqueta == null) {
            etiqueta = new Etiqueta(nombreEtiqueta);
            etiqueta.save();
        }
        return etiqueta;
    }

    /**
     * busca una etiqueta por nombre
     * @param nombreEtiqueta nombre
     * @return etiqueta si existe, null en caso contrario
     */
    public static Etiqueta obtener(String nombreEtiqueta) {
        List<Etiqueta> etiquetas = ObjetoPersistente.encontrar(Etiqueta.class, "nombre = ?", nombreEtiqueta);
        if (etiquetas == null || etiquetas.size() == 0) return null;
        return etiquetas.get(0);
    }


    /**
     * Método heredado de {@link ObjetoPersistente}. Elimina la etiqueta de persistencia.
     * @return True si fue borrado, false en caso contrario
     * @see ObjetoPersistente#delete()
     */
    @Override
    public boolean delete() {
        // Antes de eliminar Etiqueta, elminar las relaciones Many-to-Many con Etiquetas
        ParEtiquetaItem.eliminarRelacionesParaEtiqueta(this);
        return super.delete();
    }

    public String getNombre() {
        return nombre;
    }
}
