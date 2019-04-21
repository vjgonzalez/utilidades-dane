package ar.uba.fi.pruebasutilidadesdane.listasypersistencia;

import ar.uba.fi.utilidadesdane.listas.ItemLista;

/**
 * Created by Alfredo on 6/4/2017.
 */

public class Prenda extends ItemLista {

    public String nombre;
    public String rutaImagen;

    public Prenda(){super();
//        Log.d("DANE","nueva Prenda");
    }

    public Prenda(String nombre, String rutaImagen)
    {
        super();
        this.nombre = nombre;
        this.rutaImagen = rutaImagen;
        save();
//        Log.d("DANE","nueva Prenda(" + nombre + ")");
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String toString()
    {
        String categoriaStr = (getCategoria() != null)?"Categoria: " + getCategoria().getNombre():"Sin Categoria";
        return "{Prenda(" + getId() + ") - Nombre: " + nombre + " - rutaImagen: " + rutaImagen + " - " + categoriaStr + "}";
    }
}
