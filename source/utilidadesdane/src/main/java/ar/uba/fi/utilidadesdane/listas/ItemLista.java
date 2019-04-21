package ar.uba.fi.utilidadesdane.listas;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.utilidadesdane.persistencia.ObjetoPersistente;

/**
 * Objeto persistente que puede pertenecer a una Categoría y tener varias etiquetas asociadas
 * @author Virginia González y Alfredo Hodes
 */
public class ItemLista extends ObjetoPersistente {

    /**
     * Categoria a la que pertenece este objeto persistente
     */
    Categoria categoria;

    /**
     * Constructor
     */
    public ItemLista() {
        super();
    }

    /**
     * Constructor
     * @param categoria Categoria a la que pertenece este objeto persistente
     */
    public ItemLista(Categoria categoria) {
        super();
        this.categoria = categoria;
    }

    /**
     * Representación como String del ItemLista
     * @return string que incluye el id y la categoría a la que pertenece
     */
    public String toString() {
        return "{ItemLista(" + getId() + ") - Categoria: " + categoria + "}";
    }

    /**
     * Agrega una etiqueta a este objeto. Si la etiqueta no existe, la crea
     * @param nombreEtiqueta nombre etiqueta
     */
    public void agregarEtiqueta(String nombreEtiqueta) {
        agregarEtiqueta(Etiqueta.obtenerOCrear(nombreEtiqueta));
    }

    /**
     * Agrega una etiqueta a este objeto
     * @param etiqueta etiqueta a agregar
     */
    public void agregarEtiqueta(Etiqueta etiqueta) {
        ParEtiquetaItem<ItemLista> parEtiquetaItem = new ParEtiquetaItem<>(this, etiqueta);
        parEtiquetaItem.save();
    }

    /**
     * Setea la categoría de este objeto
     * @param categoria categoría a setear. Puede ser null
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        save();
    }

    /**
     * Quita una etiqueta a este objeto
     * @param nombreEtiqueta nombre de la etiqueta a quitar
     */
    public void quitarEtiqueta(String nombreEtiqueta) {
        ParEtiquetaItem.eliminarPar(this, Etiqueta.obtener(nombreEtiqueta));
    }

    /**
     * Quita una etiqueta a este objeto
     * @param etiqueta etiqueta a quitar
     */
    public void quitarEtiqueta(Etiqueta etiqueta) {
        if (etiqueta != null) {
            ParEtiquetaItem.eliminarPar(this, etiqueta);
        }
    }

    /**
     * Obtiene las etiquetas asociadas a este objeto
     * @return lista de etiquetas asociadas
     */
    public List<Etiqueta> obtenerEtiquetas() {
        List<Etiqueta> etiquetas = new ArrayList<>();

        String nombreAtributoItem = "ITEM";
        String nombreAtributoItemType = "ITEM_TYPE_NAME";

        String idItem = getId().toString();
        String valorItemType = getClass().getName();

        String whereClause = nombreAtributoItem + "=? AND " + nombreAtributoItemType + "=?";

        List<ParEtiquetaItem> pares = ObjetoPersistente.encontrar(ParEtiquetaItem.class, whereClause, idItem, valorItemType);
        for (int i = 0; i < pares.size(); i++) {
            etiquetas.add(pares.get(i).etiqueta);
        }
        return etiquetas;
    }

    /**
     * Método heredado de {@link ObjetoPersistente}. Elimina el objeto de persistencia.
     * @return True si fue borrado, false en caso contrario
     * @see ObjetoPersistente#delete()
     */
    @Override
    public boolean delete() {
        // Antes de eliminar ItemLista, elminar las relaciones Many-to-Many con Etiquetas
        ParEtiquetaItem.eliminarRelacionesParaItem(this);
        return super.delete();
    }

    public Categoria getCategoria() {
        return categoria;
    }
}
