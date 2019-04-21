package ar.uba.fi.utilidadesdane.listas;

import ar.uba.fi.utilidadesdane.persistencia.ObjetoPersistente;

/**
 * @author Virginia González y Alfredo Hodes
 */
public class Categoria extends ObjetoPersistente {

    /**
     * Nombre de la categoría. Debe ser único dentro de las categorías con una misma Categoría padre
     */
    String nombre;

    /**
     * Categoría padre. La categoría actual pasa a ser subcategoría de la cat. padre
     */
    Categoria padre;

    /**
     * Constructor
     */
    public Categoria() {
        super();
    }

    /**
     * Constructor
     * @param nombre nombre de la categoría
     */
    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Representación como String de la categoría
     * @return string que incluye el id, el nombre y el padre de la categoría.
     */
    public String toString() {
        return "{Categoria(" + getId() + ") - Nombre: " + nombre + " - padre: " + padre + "}";
    }

    /**
     * Cambia el nombre
     * @param nuevoNombre nuevo nombre a setear. Debe ser único dentro de las categorías con una misma Categoría padre
     */
    public void renombrar(String nuevoNombre) {
        this.nombre = nuevoNombre;
        save();
    }

    /**
     * Setea/cambia el padre. La categoría actual pasa a ser subcategoría de la cat. padre
     * @param padre nueva categoría padre. Puede ser null
     */
    public void setPadre(Categoria padre) {
        this.padre = padre;
        save();
    }

    /**
     * crea e inicializa una categoría. Si ya existe una con el nombre y el padre pasados, la devuelve sin crear una nueva
     * @param nombreCategoria nombre
     * @param padre categoría padre. Puede ser null
     * @return categoría creada e inicializada (si ya existe no es creada nuevamente)
     */
    private static Categoria crear(String nombreCategoria, Categoria padre) {
        Categoria categoria = obtener(nombreCategoria, padre);
        if (categoria == null) {
            categoria = new Categoria(nombreCategoria);
            categoria.padre = padre;
            categoria.save();
        }
        return categoria;
    }

    /**
     * Busca una categoría por nombre y padre
     * @param nombreCategoria nombre
     * @param padre categoría padre. Puede ser null
     * @return categoría encontrada si existe, null en caso contrario
     */
    public static Categoria obtener(String nombreCategoria, Categoria padre) {
        String padreId = (padre != null) ? padre.getId().toString() : "0";
        return ObjetoPersistente.encontrarPrimero(Categoria.class, "nombre = ? AND padre=?", nombreCategoria, padreId);
    }

    /**
     *busca una categoría por nombre, independientemente de su categoría padre
     * @param nombreCategoria nombre de la categoría a buscar
     * @return categoría encontrada si existe, null en caso contrario
     */
    public static Categoria obtenerDeCualquierPadre(String nombreCategoria) {
        return ObjetoPersistente.encontrarPrimero(Categoria.class, "nombre = ?", nombreCategoria);
    }


    /**
     * busca una categoría por nombre y padre y, si no existe, la crea e inicializa
     * @param nombreCategoria nombre
     * @param padre categoría padre. Puede ser null
     * @return categoría inicializada (si no existe es creada en el momento)
     */
    public static Categoria obtenerOCrear(String nombreCategoria, Categoria padre) {
        Categoria categoria = obtener(nombreCategoria, padre);
        if (categoria == null) {
            categoria = crear(nombreCategoria, padre);
        }
        return categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public Categoria getPadre() {
        return padre;
    }
}
