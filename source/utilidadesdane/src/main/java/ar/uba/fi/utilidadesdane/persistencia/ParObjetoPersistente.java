package ar.uba.fi.utilidadesdane.persistencia;

import com.orm.dsl.Ignore;

import java.util.List;

/**
 * Representa una relación entre dos objetos persistentes.
 * Utilizado para modelar las relaciones many-to-many.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class ParObjetoPersistente extends ObjetoPersistente {

    /**
     * Objeto 1 del par.
     */
    private ObjetoPersistente objeto1;

    /**
     * Objeto 2 del par.
     */
    private ObjetoPersistente objeto2;

    /**
     * Indica si el objeto2 debe ser eliminado cuando se elimine la relación.
     */
    private boolean eliminarObjeto2SiSeEliminaLaRelacion;

    /**
     * Clase del objeto1.
     */
    @Ignore
    private Class objeto1Tipo;

    /**
     * Clase del objeto2.
     */
    @Ignore
    private Class objeto2Tipo;

    /**
     * Nombre de la clase del objeto1.
     */
    private String objeto1NombreTipo;

    /**
     * Nombre de la clase del objeto2.
     */
    private String objeto2NombreTipo;


    @Ignore
    private static final String NOMBRE_ATRIBUTO_OBJETO1 = "OBJETO1";
    @Ignore
    private static final String NOMBRE_ATRIBUTO_OBJETO2 = "OBJETO2";
    @Ignore
    private static final String NOMBRE_ATRIBUTO_OBJETO1TIPO = "OBJETO1_NOMBRE_TIPO";
    private static final String NOMBRE_ATRIBUTO_OBJETO2TIPO = "OBJETO2_NOMBRE_TIPO";

    /**
     * Constructor
     */
    public ParObjetoPersistente() {
        super();
    }

    /**
     * Constructor.
     *
     * @param objeto1                              Objeto 1 de la relación
     * @param objeto2                              Objeto 2 de la relación
     * @param eliminarObjeto2SiSeEliminaLaRelacion True si el objeto2 debe ser eliminado cuando se elimine la relación, false de lo contrario.
     */
    public ParObjetoPersistente(ObjetoPersistente objeto1, ObjetoPersistente objeto2, boolean eliminarObjeto2SiSeEliminaLaRelacion) {
        this.objeto1 = objeto1;
        this.objeto2 = objeto2;

        objeto1Tipo = objeto1.getClass();
        objeto2Tipo = objeto2.getClass();

        objeto1NombreTipo = objeto1Tipo.getName();
        objeto2NombreTipo = objeto2Tipo.getName();

        this.eliminarObjeto2SiSeEliminaLaRelacion = eliminarObjeto2SiSeEliminaLaRelacion;
        save();
    }

    /**
     * Imprime el objeto como string
     *
     * @return Datos del objeto en formato String
     * @see Object#toString()
     */
    public String toString() {
        return "{ParObjetoPersistente(" + getId() + ") objeto1: " + objeto1 + " - objeto2: " + objeto2 + "}";
    }

    /**
     * Devuelve el objeto 1 de la relación.
     *
     * @return Objeto 1 de la relación
     */
    public ObjetoPersistente getObjeto1() {
        return objeto1;
    }

    /**
     * Devuelve el objeto 2 de la relación.
     *
     * @return Objeto 2 de la relación
     */
    public ObjetoPersistente getObjeto2() {
        return objeto2;
    }

    /**
     * Devuelve la clase del objeto 1 de la relación.
     *
     * @return Clase del objeto 1 de la relación
     */
    public Class getObjeto1Tipo() {
        return objeto1Tipo;
    }

    /**
     * Devuelve la clase del objeto 2 de la relación.
     *
     * @return Clase del objeto 2 de la relación
     */
    public Class getObjeto2Tipo() {
        return objeto2Tipo;
    }

    /**
     * Devuelve el nombre de la clase del objeto 1 de la relación.
     *
     * @return Nombre de la clase del objeto 1 de la relación
     */
    public String getObjeto1NombreTipo() {
        return objeto1NombreTipo;
    }

    /**
     * Devuelve el nombre de la clase del objeto 2 de la relación.
     *
     * @return Nombre de la clase del objeto 2 de la relación
     */
    public String getObjeto2NombreTipo() {
        return objeto2NombreTipo;
    }

    /**
     * Método heredado de {@link ObjetoPersistente}. Inicializa los atributos objetos y tipos
     * @see ObjetoPersistente#inicializarLuegoDeCargado()
     */
    @Override
    public void inicializarLuegoDeCargado() {
        super.inicializarLuegoDeCargado();
        if (objeto1 == null || !objeto1.getClass().toString().equals(objeto1NombreTipo)) {
            try {
                objeto1Tipo = Class.forName(objeto1NombreTipo);
            } catch (Exception exception) {
            }
            if (objeto1Tipo != null) {
                objeto1 = ObjetoPersistente.encontrarAtributo(this, NOMBRE_ATRIBUTO_OBJETO1, objeto1Tipo);
            }
        }
        if (objeto2 == null || !objeto2.getClass().toString().equals(objeto2NombreTipo)) {
            try {
                objeto2Tipo = Class.forName(objeto2NombreTipo);
            } catch (Exception exception) {
            }
            if (objeto2Tipo != null) {
                objeto2 = ObjetoPersistente.encontrarAtributo(this, NOMBRE_ATRIBUTO_OBJETO2, objeto2Tipo);
            }
        }
        save();
    }

    /**
     * Busca en la base de datos una relación entre los objetos recibidos.
     *
     * @param objeto1Buscar Objeto 1 de la relación
     * @param objeto2Buscar Objeto 2 de la relación
     * @return El {@link ParObjetoPersistente} que relaciona a ambos objetos, o null si no se encontró relación
     */
    public static ParObjetoPersistente encontrarPar(ObjetoPersistente objeto1Buscar, ObjetoPersistente objeto2Buscar) {
        if (objeto1Buscar == null || objeto2Buscar == null)
            return null;

        String idObjeto1 = objeto1Buscar.getId().toString();
        String idObjeto2 = objeto2Buscar.getId().toString();
        String valorObjeto1Tipo = objeto1Buscar.getClass().getName();
        String valorObjeto2Tipo = objeto2Buscar.getClass().getName();

        String whereClause = NOMBRE_ATRIBUTO_OBJETO1 + "=? AND " + NOMBRE_ATRIBUTO_OBJETO2 + "=? AND " + NOMBRE_ATRIBUTO_OBJETO1TIPO + "=? AND " + NOMBRE_ATRIBUTO_OBJETO2TIPO + "=?";

        ParObjetoPersistente par = ObjetoPersistente.encontrarPrimero(ParObjetoPersistente.class, whereClause, idObjeto1, idObjeto2, valorObjeto1Tipo, valorObjeto2Tipo);
        if (par == null) {
            par = ObjetoPersistente.encontrarPrimero(ParObjetoPersistente.class, whereClause, idObjeto2, idObjeto1, valorObjeto2Tipo, valorObjeto1Tipo);
        }
        return par;
    }

    /**
     * Busca en la base de datos todas las relaciones del objeto recibido.
     *
     * @param objeto Objeto cuyas relaciones se buscan
     * @return Lista de {@link ParObjetoPersistente} que contienen las relaciones del objeto
     */
    public static List<ParObjetoPersistente> encontrarRelacionesParaObjeto(ObjetoPersistente objeto) {
        if (objeto == null)
            return null;
        String idObjeto = objeto.getId().toString();
        String valorObjetoType = objeto.getClass().getName();

        String whereClause = "(" + NOMBRE_ATRIBUTO_OBJETO1 + "=? AND " + NOMBRE_ATRIBUTO_OBJETO1TIPO + "=?) OR ("
                + NOMBRE_ATRIBUTO_OBJETO2 + "=? AND " + NOMBRE_ATRIBUTO_OBJETO2TIPO + "=?)";
        return ObjetoPersistente.encontrar(ParObjetoPersistente.class, whereClause, idObjeto, valorObjetoType, idObjeto, valorObjetoType);
    }

    /**
     * Busca las relaciones del objeto con objetos de una clase determinada.
     *
     * @param objeto                   Objeto cuyas relaciones se buscan
     * @param claseObjetosRelacionados Clase de los objetos con los que se busca relación
     * @return Lista de {@link ParObjetoPersistente} que contienen las relaciones del objeto
     */
    public static List<ParObjetoPersistente> encontrarRelacionesConClase(ObjetoPersistente objeto, Class claseObjetosRelacionados) {
        if (objeto == null || claseObjetosRelacionados == null)
            return null;
        String idObjeto = objeto.getId().toString();
        String valorObjetoType = objeto.getClass().getName();

        String nombreClaseRelacionada = claseObjetosRelacionados.getName();

        String whereClause = "(" + NOMBRE_ATRIBUTO_OBJETO1 + "=? AND " + NOMBRE_ATRIBUTO_OBJETO1TIPO + "=? AND " + NOMBRE_ATRIBUTO_OBJETO2TIPO +
                "=?) OR (" + NOMBRE_ATRIBUTO_OBJETO2 + "=? AND " + NOMBRE_ATRIBUTO_OBJETO2TIPO + "=? AND " + NOMBRE_ATRIBUTO_OBJETO1TIPO + "=?)";
        return ObjetoPersistente.encontrar(ParObjetoPersistente.class, whereClause, idObjeto, valorObjetoType, nombreClaseRelacionada, idObjeto, valorObjetoType, nombreClaseRelacionada);
    }

    /**
     * Elimina de la base de datos la relación entre los objetos recibidos, si existe.
     *
     * @param objeto1Eliminar Objeto 1 de la relación
     * @param objeto2Eliminar Objeto 2 de la relación
     */
    public static void eliminarPar(ObjetoPersistente objeto1Eliminar, ObjetoPersistente objeto2Eliminar) {
        ParObjetoPersistente par = encontrarPar(objeto1Eliminar, objeto2Eliminar);
        if (par != null) {
            par.delete();
            if (par.eliminarObjeto2SiSeEliminaLaRelacion)
                par.objeto2.delete();
        }
    }

    /**
     * Elimina todas las relaciones del objeto recibido con otros objetos.
     *
     * @param objeto Objeto cuyas relaciones se deben eliminar
     */
    public static void eliminarRelacionesParaObjeto(ObjetoPersistente objeto) {
        if (!objeto.getClass().equals(ParObjetoPersistente.class)) {
            List<ParObjetoPersistente> lista = encontrarRelacionesParaObjeto(objeto);
            if (lista == null)
                return;
            for (int i = 0; i < lista.size(); i++) {
                lista.get(i).delete();
                if (lista.get(i).eliminarObjeto2SiSeEliminaLaRelacion) {
                    lista.get(i).objeto2.delete();
                }
            }
        }
    }

}