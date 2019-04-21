package ar.uba.fi.utilidadesdane.persistencia;

import android.database.Cursor;

import com.orm.SugarRecord;
import com.orm.util.NamingHelper;
import com.orm.util.QueryBuilder;

import java.util.ArrayList;
import java.util.List;


public class ObjetoPersistente extends SugarRecord {

    public ObjetoPersistente() {
        super();
    }

    /**
     * método auxiliar usado para inicializar este objeto después ser creado por el ORM mediante una query
     */
    public void inicializarLuegoDeCargado() {
    }


    /**
     * obtiene un listado de objetos persistidos con determinadas condiciones
     * @param type clase de los objetos a devolver. Debe extender {@link ObjetoPersistente}
     * @param whereClause sentencia "WHERE" en formato SQL usando "?" para args
     * @param whereArgs listado de args para reemplazar las instancias de "?" en el parámetro anterior
     * @param groupBy atributo mediante el cual agrupar los objetos en la query
     * @param orderBy atributo usado para ordenar los resultados
     * @param limit cantidad máxima de objetos a devolver
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return lista de objetos que cumplen las condiciones pedidas
     */
    public static <T> List<T> encontrar(Class<T> type, String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit) {

        Cursor cursor = DBUtils.getBD().query(NamingHelper.toSQLName(type), null, whereClause, whereArgs,
                groupBy, null, orderBy, limit);

        List<T> entities = getEntitiesFromCursor(cursor, type);

        if (entities != null) {
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i) instanceof ObjetoPersistente) {
                    ObjetoPersistente objPers = ObjetoPersistente.class.cast(entities.get(i));
                    objPers.inicializarLuegoDeCargado();
                }
            }
        }
        return entities;
    }

    /**
     * obtiene todos los objetos persistidos de una clase
     * @param type clase de los objetos a devolver. Debe extender {@link ObjetoPersistente}
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return lista con todos los objetos de la clase pedida
     */
    public static <T> List<T> listarTodos(Class<T> type) {
        return encontrar(type, null, null, null, null, null);
    }

    /**
     * obtiene todos los objetos persistidos de una clase, ordenados por un atributo
     * @param type clase de los objetos a devolver. Debe extender {@link ObjetoPersistente}
     * @param orderBy atributo usado para ordenar los resultados
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return lista con todos los objetos de la clase pedida
     */
    public static <T> List<T> listarTodos(Class<T> type, String orderBy) {
        return encontrar(type, null, null, null, orderBy, null);
    }

    /**
     * obtiene un objeto persistido buscándolo por clase y ID
     * @param type clase del objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @param id id del objeto persistido, asignada por el ORM en su creación. @see {@link SugarRecord#id}
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return el objeto de la clase y con ID pedidos si existe, null en caso contrario.
     */
    public static <T> T encontrarPorId(Class<T> type, Long id) {
        List<T> list = encontrar(type, "id=?", new String[]{String.valueOf(id)}, null, null, "1");
        if (list.isEmpty()) return null;
        return list.get(0);
    }

    /**
     * obtiene un objeto persistido buscándolo por clase y ID
     * @param type clase del objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @param id id del objeto persistido, asignada por el ORM en su creación. @see {@link SugarRecord#id}
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return el objeto de la clase y con ID pedidos si existe, null en caso contrario.
     */
    public static <T> T encontrarPorId(Class<T> type, Integer id) {
        return encontrarPorId(type, Long.valueOf(id));
    }

    /**
     * obtiene una lista de objetos persistidos buscándolos por clase y ID
     * @param type clase de los objetos a devolver. Debe extender {@link ObjetoPersistente}
     * @param ids ids de los objetos persistidos (formato texto), asignada por el ORM en su creación. @see {@link SugarRecord#id}
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return lista con todos los objetos de la clase pedida con los IDs pasados.
     */
    public static <T> List<T> encontrarPorId(Class<T> type, String[] ids) {
        String whereClause = "id IN (" + QueryBuilder.generatePlaceholders(ids.length) + ")";
        return encontrar(type, whereClause, ids);
    }

    /**
     * obtiene el primer objeto persistido de una determinada clase
     * @param type clase del objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return el primer objeto persistido de la clase pedida si existe, null en caso contrario
     */
    public static <T> T primero(Class<T> type) {
        List<T> list = encontrarConQuery(type,
                "SELECT * FROM " + NamingHelper.toSQLName(type) + " ORDER BY ID ASC LIMIT 1");
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * obtiene el último objeto persistido de una determinada clase
     * @param type clase del objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return el último objeto persistido de la clase pedida si existe, null en caso contrario
     */
    public static <T> T ultimo(Class<T> type) {
        List<T> list = encontrarConQuery(type,
                "SELECT * FROM " + NamingHelper.toSQLName(type) + " ORDER BY ID DESC LIMIT 1");
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * obtiene un listado de objetos persistidos con determinadas condiciones
     * @param type clase de los objetos a devolver. Debe extender {@link ObjetoPersistente}
     * @param whereClause sentencia "WHERE" en formato SQL usando "?" para args
     * @param whereArgs listado de args para reemplazar las instancias de "?" en el parámetro anterior
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return lista de objetos que cumplen las condiciones pedidas
     */
    public static <T> List<T> encontrar(Class<T> type, String whereClause, String... whereArgs) {
        return encontrar(type, whereClause, whereArgs, null, null, null);
    }

    /**
     * obtiene el primer objeto persistido con determinadas condiciones
     * @param type clase del objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @param whereClause sentencia "WHERE" en formato SQL usando "?" para args
     * @param whereArgs listado de args para reemplazar las instancias de "?" en el parámetro anterior
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return primer objeto que cumple las condiciones pedidas si existe, null en caso contrario
     */
    public static <T> T encontrarPrimero(Class<T> type, String whereClause, String... whereArgs) {
        List<T> list = encontrar(type, whereClause, whereArgs, null, null, null);
        if (list != null && list.size() > 0) return list.get(0);
        return null;
    }

    /**
     * obtiene un listado de objetos persistidos con determinadas condiciones mediante una query SQL
     * @param type clase de los objetos a devolver. Debe extender {@link ObjetoPersistente}
     * @param query sentencia en formato SQL usando "?" para args
     * @param arguments listado de args para reemplazar las instancias de "?" en el parámetro anterior
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return lista de objetos que cumplen las condiciones pedidas
     */
    public static <T> List<T> encontrarConQuery(Class<T> type, String query, String... arguments) {
        Cursor cursor = DBUtils.getBD().rawQuery(query, arguments);


        List<T> entities = getEntitiesFromCursor(cursor, type);

        if (entities != null) {
            for (int i = 0; i < entities.size(); i++) {
                if (entities.get(i) instanceof ObjetoPersistente) {
                    ObjetoPersistente objPers = ObjetoPersistente.class.cast(entities.get(i));
                    objPers.inicializarLuegoDeCargado();
                }
            }
        }
        return entities;
    }


    /**
     * obtiene un atributo de tipo ObjetoPersistente perteneciente a otro ObjetoPersistente
     * @param objeto ObjetoPersistente que tiene el atributo que se desea obtener
     * @param nombreAtributo nombre del atributo a obtener
     * @param tipoAtributo clase del atributo a obtener. Debe extender {@link ObjetoPersistente}
     * @param <T> tipo de objeto a devolver. Debe extender {@link ObjetoPersistente}
     * @return atributo buscado
     */
    public static <T> T encontrarAtributo(ObjetoPersistente objeto, String nombreAtributo, Class tipoAtributo) {
        String tablaObjeto = NamingHelper.toSQLName(objeto.getClass());
        String columnaAtributo = nombreAtributo;
        String query = "SELECT " + columnaAtributo + " FROM " + tablaObjeto + " WHERE ID = ? LIMIT 1";
        String[] selectionArgs = {Long.toString(objeto.getId())};
        Cursor cursor = DBUtils.getBD().rawQuery(query, selectionArgs);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            long atributoId = cursor.getLong(0);
            return (T) encontrarPorId(tipoAtributo, atributoId);
        } else {
            return null;
        }
    }

    /**
     * Elimina de la base de datos todos los objetos de la clase indicada
     *
     * @param clase Clase cuyos objetos se desea eliminar.
     * @param <T>   Clase
     * @return 0
     */
    public static <T> int borrarTodos(Class<T> clase) {
        return deleteAll(clase);
    }

    /**
     * Busca en la base de datos todas las relaciones del objeto.
     *
     * @return Lista de {@link ParObjetoPersistente} que contienen las relaciones del objeto
     */
    public List<ParObjetoPersistente> encontrarRelaciones() {
        return ParObjetoPersistente.encontrarRelacionesParaObjeto(this);
    }

    /**
     * Crea una relación con el objeto indicado.
     *
     * @param objetoARelacionar Objeto relacionado
     */
    public void agregarRelacionConObjeto(ObjetoPersistente objetoARelacionar) {
        agregarRelacionConObjeto(objetoARelacionar, false);
    }

    /**
     * Crea una relación con el objeto indicado.
     *
     * @param objetoARelacionar               Objeto relacionado
     * @param eliminarSiEsteObjetoEsEliminado True si objetoARelacionar debe eliminarse al eliminar la relación, false de lo contrario
     */
    public void agregarRelacionConObjeto(ObjetoPersistente objetoARelacionar, boolean eliminarSiEsteObjetoEsEliminado) {
        if (encontrarRelacionConObjeto(objetoARelacionar) == null) {
            ParObjetoPersistente parObjetoPersistente = new ParObjetoPersistente(this, objetoARelacionar, eliminarSiEsteObjetoEsEliminado);
            parObjetoPersistente.save();
        }
    }

    /**
     * Elimina la relación con el objeto indicado, si existe.
     *
     * @param objetoPersistente Objeto cuya relación desea eliminarse
     */
    public void eliminarRelacion(ObjetoPersistente objetoPersistente) {
        ParObjetoPersistente.eliminarPar(this, objetoPersistente);
    }

    /**
     * Indica si dos objetos persistentes tienen el mismo ID y la misma clase.
     *
     * @param otroObjeto Objeto con el cual se desea comparar
     * @return True si son iguales, false de lo contrario
     */
    public boolean equals(Object otroObjeto) {
        ObjetoPersistente otroObjetoPersistente = (ObjetoPersistente) otroObjeto;
        return (getId() != null && otroObjetoPersistente.getId() != null && getId().equals(otroObjetoPersistente.getId()))
                && getClass().equals(otroObjeto.getClass());
    }

    /**
     * Busca una relación con un objeto determinado.
     *
     * @param objetoPersistente Objeto con el cual se busca la relación
     * @return El {@link ParObjetoPersistente} que relaciona a ambos objetos, o null si no se encontró relación
     */
    private ParObjetoPersistente encontrarRelacionConObjeto(ObjetoPersistente objetoPersistente) {
        return ParObjetoPersistente.encontrarPar(this, objetoPersistente);
    }

    /**
     * Elimina el objeto de la base de datos, incluyendo todas sus relaciones.
     *
     * @return True si fue borrado, falso de lo contrario
     * @see SugarRecord#delete()
     */
    @Override
    public boolean delete() {
        // Antes de eliminar, elmina las relaciones Many-to-Many
        ParObjetoPersistente.eliminarRelacionesParaObjeto(this);
        return super.delete();
    }

    /**
     * Busca todos los objetos de una clase que tienen relación con el objeto actual.
     *
     * @param claseObjetosAObtener Clase de los objetos con que se busca relación
     * @param <T>                  Clase de los objetos con que se busca relación
     * @return Listado de objetos relacionados
     */
    public <T extends ObjetoPersistente> List<T> obtenerRelaciones(Class<T> claseObjetosAObtener) {
        List<ParObjetoPersistente> lista = ParObjetoPersistente.encontrarRelacionesConClase(this, claseObjetosAObtener);
        List<T> objetosObtenidos = new ArrayList<T>();
        ParObjetoPersistente parActual = null;
        for (int i = 0; i < lista.size(); i++) {
            parActual = lista.get(i);
            if (parActual.getObjeto1().getId().equals(this.getId()) && parActual.getObjeto1Tipo().equals(this.getClass())) {
                //objeto1 es "this", extraigo el objeto2 como objetoAObtener
                objetosObtenidos.add((claseObjetosAObtener.cast(parActual.getObjeto2())));
            } else {
                objetosObtenidos.add((claseObjetosAObtener.cast(parActual.getObjeto1())));
            }
        }
        return objetosObtenidos;
    }
}
