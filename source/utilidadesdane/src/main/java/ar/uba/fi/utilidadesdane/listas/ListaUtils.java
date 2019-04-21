package ar.uba.fi.utilidadesdane.listas;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import ar.uba.fi.utilidadesdane.persistencia.ObjetoPersistente;

/**
 * @author Virginia González y Alfredo Hodes
 */
public class ListaUtils {


    /**
     * Lista todos los objetos de una determinada clase
     * @param type clase de los objetos a listar. Debe ser/extender {@link ItemLista}
     * @param <T> tipo de los objetos a listar. Debe ser/extender {@link ItemLista}
     * @return Lista con todos los objetos encontrados
     */
    public static <T extends ItemLista> List<T> listarTodos(Class<T> type) {
        return T.listarTodos(type);
    }

    /**
     * Lista todos los objetos de una determinada clase y categoría
     * @param type clase de los objetos a listar. Debe ser/extender {@link ItemLista}
     * @param categoriaBase categoría base de los objetos a buscar
     * @param incluirSubcategorias si se incluyen objetos pertenecientes a subcategorías de la categoría pasada
     * @param <T>  tipo de los objetos a listar. Debe ser/extender {@link ItemLista}
     * @return Lista con todos los objetos encontrados
     */
    public static <T extends ItemLista> List<T> listarPorCategoria(Class<T> type, Categoria categoriaBase, boolean incluirSubcategorias) {
        if (categoriaBase == null) return new ArrayList<T>();
        List<Categoria> categorias = new ArrayList<Categoria>();
        categorias.add(categoriaBase);
        if (incluirSubcategorias) {
            Stack<Categoria> categoriasABuscarHijos = new Stack<Categoria>();
            // 1: Agrego la categoria base para procesar
            categoriasABuscarHijos.push(categoriaBase);
            while (categoriasABuscarHijos.size() > 0) {
                // 2: Tomar un elemento y buscar todos sus hijos.
                Categoria categoriaActual = categoriasABuscarHijos.pop();
                List<Categoria> hijos = ObjetoPersistente.encontrar(Categoria.class, "PADRE =?", categoriaActual.getId().toString());
                for (int i = 0; i < hijos.size(); i++) {
                    Categoria catHijo = hijos.get(i);
                    categoriasABuscarHijos.push(catHijo);
                    categorias.add(catHijo);
                }
            }
        }

        String whereClause = "";
        List<String> whereArgsList = new ArrayList<String>();
        for (int i = 0; i < categorias.size(); i++) {
            whereClause += "CATEGORIA =?";
            if (i < categorias.size() - 1) whereClause += " OR ";

            whereArgsList.add(categorias.get(i).getId().toString());
        }
        String[] whereArgs = whereArgsList.toArray(new String[whereArgsList.size()]);
        return ObjetoPersistente.encontrar(type, whereClause, whereArgs);
    }

    /**
     * Lista todos los objetos de una determinada clase y etiqueta
     * @param type clase de los objetos a listar. Debe ser/extender {@link ItemLista}
     * @param etiqueta etiqueta de los objetos a buscar
     * @param <T>  tipo de los objetos a listar. Debe ser/extender {@link ItemLista}
     * @return Lista con todos los objetos encontrados
     */
    public static <T extends ItemLista> List<T> listarPorEtiqueta(Class<T> type, Etiqueta etiqueta) {
        if (etiqueta == null) return new ArrayList<T>();

        // 1: Obtener todos los "pares"
        String nombreAtributoEtiqueta = "ETIQUETA";
        String nombreAtributoItemType = "ITEM_TYPE_NAME";

        String idEtiqueta = etiqueta.getId().toString();
        String itemTypeName = type.getName();

        String whereClause = nombreAtributoEtiqueta + "=? AND " + nombreAtributoItemType + "=?";
        List<ParEtiquetaItem> list = ObjetoPersistente.encontrar(ParEtiquetaItem.class, whereClause, idEtiqueta, itemTypeName);

        // 2: Recorrer todos los pares para obtener los items a devolver
        List<T> items = new ArrayList<T>();
        for (int i = 0; i < list.size(); i++) {
            items.add((T) list.get(i).item);
        }
        return items;
    }

    /**
     * Lista todos los objetos de una determinada clase y conjunto de etiquetas
     * @param type clase de los objetos a listar. Debe ser/extender {@link ItemLista}
     * @param etiquetas etiquetas de los objetos a buscar
     * @soloDevolverSiCumpleTodasLasEtiquetas si true, se incluyen sólo los objetos que tienen todas las etiquetas asociadas ("AND"). Si false, se incluyen todos los objetos que tengal al menos una etiqueta asociada ("OR").
     * @param <T>  tipo de los objetos a listar. Debe ser/extender {@link ItemLista}
     * @return Lista con todos los objetos encontrados
     */
    public static <T extends ItemLista> List<T> listarPorEtiquetas(Class<T> type, Etiqueta[] etiquetas, boolean soloDevolverSiCumpleTodasLasEtiquetas) {
        if (etiquetas == null || etiquetas.length == 0) return new ArrayList<T>();
        // 1: Obtener todas los "pares"
        List<String> queryArgsList = new ArrayList<String>();
        // 1.a: Armar where clause con todas las etiquetas
        String nombreAtributoEtiqueta = "ETIQUETA";
        String etiquetasWhereClause = "";
        Set<Long> idsEtiquetas = new HashSet<Long>();
        for (int i = 0; i < etiquetas.length; i++) {
            etiquetasWhereClause += nombreAtributoEtiqueta + " =?";
            if (i < etiquetas.length - 1) etiquetasWhereClause += " OR ";

            queryArgsList.add(etiquetas[i].getId().toString());
            idsEtiquetas.add(etiquetas[i].getId());
        }
        String nombreAtributoItemType = "ITEM_TYPE_NAME";
        String itemTypeName = type.getName();

        queryArgsList.add(itemTypeName);

        String whereClause = "(" + etiquetasWhereClause + ") AND " + nombreAtributoItemType + "=?";
        String[] whereArgs = queryArgsList.toArray(new String[queryArgsList.size()]);
        List<ParEtiquetaItem> list = ObjetoPersistente.encontrar(ParEtiquetaItem.class, whereClause, whereArgs);

        // 2: Recorrer todos los pares para obtener los items a devolver
        Set<Long> itemIdsProcesados = new HashSet<Long>();
        List<T> items = new ArrayList<T>();
        for (int i = 0; i < list.size(); i++) {
            // Evitar duplicados
            ItemLista itemProcesado = list.get(i).item;
            if (itemIdsProcesados.contains(itemProcesado.getId())) {
            } else {
                if (soloDevolverSiCumpleTodasLasEtiquetas) {
                    Set<Long> etiquetasAVerificar = new HashSet<Long>();
                    // TODO: Verificar que este ítem cumpla con TODAS las etiquetas
                    etiquetasAVerificar.add(list.get(i).etiqueta.getId()); // Agrego la etiqueta del par actual y evalúo los siguientes
                    for (int j = i + 1; j < list.size(); j++) {
                        etiquetasAVerificar.add(list.get(j).etiqueta.getId());
                    }
                    if (etiquetasAVerificar.containsAll(idsEtiquetas)) {
                        items.add((T) itemProcesado);
                    } else {
                    }
                } else {
                    // Con que cumpla con 1 sola se devuelve
                    items.add((T) itemProcesado);
                }
                itemIdsProcesados.add(itemProcesado.getId());
            }
        }
        return items;
    }

    /**
     * Lista todos los objetos de una determinada clase, categoria y conjunto de etiquetas
     * @param type clase de los objetos a listar. Debe ser/extender {@link ItemLista}
     * @param categoriaBase categoría base de los objetos a buscar
     * @param incluirSubcategorias si se incluyen objetos pertenecientes a subcategorías de la categoría pasada
     * @param etiquetas etiquetas de los objetos a buscar
     * @soloDevolverSiCumpleTodasLasEtiquetas si true, se incluyen sólo los objetos que tienen todas las etiquetas asociadas ("AND"). Si false, se incluyen todos los objetos que tengal al menos una etiqueta asociada ("OR").
     * @param <T>  tipo de los objetos a listar. Debe ser/extender {@link ItemLista}
     * @return Lista con todos los objetos encontrados
     */
    public static <T extends ItemLista> List<T> listarPorCategoriaYEtiquetas(Class<T> type, Categoria categoriaBase, boolean incluirSubcategorias, Etiqueta[] etiquetas, boolean soloDevolverSiCumpleTodasLasEtiquetas) {
        // TODO: Optimizar!
        List<T> itemsPorCategoria = listarPorCategoria(type, categoriaBase, incluirSubcategorias);
        List<T> itemsPorEtiquetas = listarPorEtiquetas(type, etiquetas, soloDevolverSiCumpleTodasLasEtiquetas);

        Set<Long> itemIdsPorCat = new HashSet<Long>();
        for (int i = 0; i < itemsPorCategoria.size(); i++) {
            itemIdsPorCat.add(itemsPorCategoria.get(i).getId());
        }

        List<T> itemsEnAmbasListas = new ArrayList<T>();
        for (int i = 0; i < itemsPorEtiquetas.size(); i++) {
            Long itemId = itemsPorEtiquetas.get(i).getId();
            if (itemIdsPorCat.contains(itemId)) {
                // En ambas! Agregarlo a la lista a devolver y quitarlo del 1er hash para no procesarlo de nuevo
                itemsEnAmbasListas.add(itemsPorEtiquetas.get(i));
                itemIdsPorCat.remove(itemId);
            }
        }

        return itemsEnAmbasListas;
    }
}
