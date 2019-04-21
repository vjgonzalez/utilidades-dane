package ar.uba.fi.utilidadesdane.listas;

import com.orm.dsl.Ignore;

import java.util.List;

import ar.uba.fi.utilidadesdane.persistencia.ObjetoPersistente;

/**
 * clase auxiliar para modelar relación M a N entre etiquetas e items
 * @author Virginia González y Alfredo Hodes
 */
public class ParEtiquetaItem<T extends ItemLista> extends ObjetoPersistente {

    Etiqueta etiqueta;
    T item;

    @Ignore
    private Class<T> itemType;

    private String itemTypeName;

    /**
     * constructor
     */
    public ParEtiquetaItem() {
    }

    /**
     * constructor
     * @param item     item a etiquetar
     * @param etiqueta etiqueta a asociar al ítem
     */
    public ParEtiquetaItem(T item, Etiqueta etiqueta) {
        this.item = item;
        this.etiqueta = etiqueta;

        itemType = (Class<T>) item.getClass();
        itemTypeName = itemType.getName();
    }

    /**
     * Representación como String del par etiqueta-ítem
     * @return string que incluye el id del par, el item y el nombre de la etiqueta
     */
    public String toString() {
        return "{ParEtiquetaItem(" + getId() + ") item: " + item + " - etiqueta: " + etiqueta.getNombre() + "}";
    }

    /**
     * Método heredado de {@link ObjetoPersistente}. Inicializa item e itemType
     * @see ObjetoPersistente#inicializarLuegoDeCargado()
     */
    @Override
    public void inicializarLuegoDeCargado() {
        super.inicializarLuegoDeCargado();
        if (item == null || !item.getClass().toString().equals(itemTypeName)) {
            try {
                itemType = (Class<T>) Class.forName(itemTypeName);
            } catch (Exception ignored) {
            }
            if (itemType != null) {
                String nombreAtributoItem = "ITEM";
                item = ObjetoPersistente.encontrarAtributo(this, nombreAtributoItem, itemType);
            }
        }
    }

    /**
     * obtiene el par que relaciona un itemlista con una etiqueta
     * @param itemLista item etiquetado
     * @param etiqueta etiqueta asociada
     * @return el ParEtiquetaItem que los relaciona si existe, null en caso contrario
     */
    private static ParEtiquetaItem encontrarPar(ItemLista itemLista, Etiqueta etiqueta) {
        if (itemLista == null || etiqueta == null) return null;

        String nombreAtributoItem = "ITEM";
        String nombreAtributoEtiqueta = "ETIQUETA";
        String nombreAtributoItemType = "ITEM_TYPE_NAME";

        String idItem = itemLista.getId().toString();
        String idEtiqueta = etiqueta.getId().toString();
        String valorItemType = itemLista.getClass().getName();

        String whereClause = nombreAtributoItem + "=? AND " + nombreAtributoEtiqueta + "=? AND " + nombreAtributoItemType + "=?";
        return ObjetoPersistente.encontrarPrimero(ParEtiquetaItem.class, whereClause, idItem, idEtiqueta, valorItemType);
    }

    /**
     * elimina la relación entre un itemLista y una etiqueta
     * @param itemLista el itemLista al cual quitarle la etiqueta
     * @param etiqueta etiqueta a quitar
     */
    protected static void eliminarPar(ItemLista itemLista, Etiqueta etiqueta) {
        ParEtiquetaItem par = encontrarPar(itemLista, etiqueta);
        if (par != null) par.delete();
    }

    /**
     * elimina todos las relaciones ParEtiquetaItem de un item
     * @param itemLista item al cual quitarle todas las etiquetas
     */
    protected static void eliminarRelacionesParaItem(ItemLista itemLista) {
        if (itemLista == null) return;

        String nombreAtributoItem = "ITEM";
        String nombreAtributoItemType = "ITEM_TYPE_NAME";

        String idItem = itemLista.getId().toString();
        String valorItemType = itemLista.getClass().getName();

        String whereClause = nombreAtributoItem + "=? AND " + nombreAtributoItemType + "=?";
        List<ParEtiquetaItem> list = ObjetoPersistente.encontrar(ParEtiquetaItem.class, whereClause, idItem, valorItemType);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).delete();
        }
    }

    /**
     * elimina todas las relaciones ParEtiquetaItem de una etiqueta
     * @param etiqueta etiqueta a quitar de todos los items
     */
    protected static void eliminarRelacionesParaEtiqueta(Etiqueta etiqueta) {
        if (etiqueta == null) return;

        String nombreAtributoEtiqueta = "ETIQUETA";
        String idEtiqueta = etiqueta.getId().toString();

        String whereClause = nombreAtributoEtiqueta + "=?";
        List<ParEtiquetaItem> list = ObjetoPersistente.encontrar(ParEtiquetaItem.class, whereClause, idEtiqueta);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).delete();
        }
    }
}
