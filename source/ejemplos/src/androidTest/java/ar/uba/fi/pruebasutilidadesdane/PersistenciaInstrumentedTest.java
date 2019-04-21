package ar.uba.fi.pruebasutilidadesdane;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.orm.SugarContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import ar.uba.fi.pruebasutilidadesdane.listasypersistencia.Prenda;
import ar.uba.fi.utilidadesdane.listas.Categoria;
import ar.uba.fi.utilidadesdane.listas.Etiqueta;
import ar.uba.fi.utilidadesdane.listas.ItemLista;
import ar.uba.fi.utilidadesdane.listas.ListaUtils;
import ar.uba.fi.utilidadesdane.listas.ParEtiquetaItem;
import ar.uba.fi.utilidadesdane.persistencia.ParObjetoPersistente;
import ar.uba.fi.utilidadesdane.persistencia.DBUtils;
import ar.uba.fi.utilidadesdane.persistencia.ObjetoPersistente;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

@RunWith(AndroidJUnit4.class)
public class PersistenciaInstrumentedTest {

    @Before
    public void setUp() {
        Log.d("DANE","setUp...");

        Log.d("DANE","SugarContext.init");
        SugarContext.init(getTargetContext());

        Log.d("DANE","DBUtils.inicializar");
        DBUtils.inicializar(getTargetContext(), true);

        VaciarBD();
    }

    @After
    public void tearDown() {
        Log.d("DANE","...tearDown");
        Log.d("DANE","SugarContext.terminate");
        SugarContext.terminate();
//        database.close();
    }

    private void VaciarBD()
    {
        ObjetoPersistente.borrarTodos(ObjetoPersistente.class);
        ObjetoPersistente.borrarTodos(Categoria.class);
        ObjetoPersistente.borrarTodos(Etiqueta.class);
        ObjetoPersistente.borrarTodos(Prenda.class);
        ObjetoPersistente.borrarTodos(ParEtiquetaItem.class);
        ObjetoPersistente.borrarTodos(ItemLista.class);
        ObjetoPersistente.borrarTodos(ParObjetoPersistente.class);

    }

    @Test
    public void agregarYRecuperarObjetoPersistente()
    {
        assertEquals(0, ObjetoPersistente.listarTodos(ObjetoPersistente.class).size());

        ObjetoPersistente obj = new ObjetoPersistente();
        obj.save();

        Long id = obj.getId();

        ObjetoPersistente objRecup = ObjetoPersistente.encontrarPorId(ObjetoPersistente.class, id);
        assertNotNull(objRecup);

        objRecup.delete();

        objRecup = ObjetoPersistente.encontrarPorId(ObjetoPersistente.class, id);
        assertNull(objRecup);
    }


    @Test
    public void crearYRecuperarCategoriaYSubcategoria()
    {
        assertEquals(0, ObjetoPersistente.listarTodos(Categoria.class).size());

        Categoria.obtenerOCrear("prenda", null);
        assertEquals(1, ObjetoPersistente.listarTodos(Categoria.class).size());

        // La nueva categoria "superior" es subcategoria de "prenda" (que ya existe, no debería crearse duplicada)
        Categoria.obtenerOCrear("superior", Categoria.obtener("prenda", null));
        assertEquals(2, ObjetoPersistente.listarTodos(Categoria.class).size());

        Categoria prenda = Categoria.obtener("prenda", null);
        assertNotNull(prenda);
        assertNull(prenda.getPadre());

        Categoria superior = Categoria.obtenerOCrear("superior", prenda);
        assertNotNull(superior);
        assertNotNull(superior.getPadre());

        assertEquals(prenda.getId(),superior.getPadre().getId());


        // La segunda categoria "superior" no tiene padre -> Es distinta a la anterior, debería crearse una nueva
        Categoria.obtenerOCrear("superior", null);
        assertEquals(3, ObjetoPersistente.listarTodos(Categoria.class).size());
        assertNull(Categoria.obtener("superior", null).getPadre());

        Categoria prendaSuperior = Categoria.obtenerOCrear("superior", prenda);
        Categoria soloSuperior = Categoria.obtenerOCrear("superior", null);
        assertNotEquals(prendaSuperior.getId(),soloSuperior.getId());

        assertNull(Categoria.obtener("inexistente",null));

        soloSuperior.delete();
        assertNull(Categoria.obtener("superior", null));

        // Sólo debería quedar "superior" de "prenda"
        Categoria.obtenerDeCualquierPadre("superior").renombrar("superiornuevo");
        assertNull(Categoria.obtener("superior", null));
        assertNotNull(Categoria.obtenerDeCualquierPadre("superiornuevo"));
        assertEquals(prenda.getId(),Categoria.obtenerDeCualquierPadre("superiornuevo").getPadre().getId());
    }


    @Test
    public void crearYRecuperarEtiquetas()
    {
        assertEquals(0, ObjetoPersistente.listarTodos(Etiqueta.class).size());
        assertEquals(0, ObjetoPersistente.listarTodos(ItemLista.class).size());
        assertEquals(0, ObjetoPersistente.listarTodos(ParEtiquetaItem.class).size());

        Etiqueta.obtenerOCrear("calor");
        assertEquals(1, ObjetoPersistente.listarTodos(Etiqueta.class).size());

        Etiqueta.obtenerOCrear("frio");
        Etiqueta.obtenerOCrear("calor");
        assertEquals(2, ObjetoPersistente.listarTodos(Etiqueta.class).size());

        assertNotNull(Etiqueta.obtener("calor"));

        Etiqueta.obtener("calor").delete();
        assertNull(Etiqueta.obtener("calor"));

        // Crear etiqueta desde método etiquetar
        ItemLista itemLista = new ItemLista();
        itemLista.save();
        itemLista.agregarEtiqueta("etiquetaagregada_1");
        itemLista.agregarEtiqueta("etiquetaagregada_2");
        assertEquals(3, ObjetoPersistente.listarTodos(Etiqueta.class).size());
        assertEquals(2, itemLista.obtenerEtiquetas().size());
        itemLista.quitarEtiqueta("etiquetaagregada_2");
        assertEquals(1, itemLista.obtenerEtiquetas().size());

        // Verificar etiquetas con items de clases distintas e igual ID
        Prenda prenda = new Prenda();
        prenda.setId(itemLista.getId());
        prenda.save();
        assertEquals(itemLista.getId(), prenda.getId()); // Ambos deberían ser 1
        prenda.agregarEtiqueta("etiquetaprenda_1");
        prenda.agregarEtiqueta("etiquetaprenda_2");
        assertEquals(1, itemLista.obtenerEtiquetas().size());
        assertEquals(2, prenda.obtenerEtiquetas().size());
    }

    @Test
    public void relacionesParObjeto(){
        assertEquals(0,ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());

        Prenda prenda = new Prenda();
        prenda.setNombre("Zapatillas");
        prenda.save();
        Categoria categoria = new Categoria("cat");
        categoria.save();

        ItemLista itemLista = new ItemLista();
        itemLista.setCategoria(categoria);
        itemLista.save();

        ItemLista itemLista2 = new ItemLista();
        itemLista2.setCategoria(categoria);
        itemLista2.save();

        prenda.agregarRelacionConObjeto(itemLista);
        ParObjetoPersistente par = ParObjetoPersistente.encontrarPar(itemLista,prenda);
        ParObjetoPersistente parInverso = ParObjetoPersistente.encontrarPar(prenda,itemLista);
        List<ParObjetoPersistente> relaciones = ParObjetoPersistente.encontrarRelacionesParaObjeto(prenda);
        List<ParObjetoPersistente> relacionesConItemLista = ParObjetoPersistente.encontrarRelacionesConClase(prenda,ItemLista.class);
        List<ParObjetoPersistente> relacionesConPrenda = ParObjetoPersistente.encontrarRelacionesConClase(prenda,Prenda.class);

        List<ItemLista> relacionesDesdeObjPersistenteConItemLista = prenda.obtenerRelaciones(ItemLista.class);
        List<Prenda> relacionesDesdeObjPersistenteConPrenda = prenda.obtenerRelaciones(Prenda.class);
        List<ParObjetoPersistente> cantidadRelaciones = ObjetoPersistente.listarTodos(ParObjetoPersistente.class);

        assertNotNull(par);
        assertNotNull(parInverso);
        assertEquals(par.getId(),parInverso.getId());
        assertEquals(1,cantidadRelaciones.size());
        assertEquals(1,relaciones.size());
        assertEquals(1,relacionesConItemLista.size());
        assertEquals(0,relacionesConPrenda.size());
        assertEquals(1,relacionesDesdeObjPersistenteConItemLista.size());
        assertEquals(0,relacionesDesdeObjPersistenteConPrenda.size());

        prenda.agregarRelacionConObjeto(itemLista2);
        par = ParObjetoPersistente.encontrarPar(itemLista2,prenda);
        parInverso = ParObjetoPersistente.encontrarPar(prenda,itemLista2);
        relaciones = ParObjetoPersistente.encontrarRelacionesParaObjeto(prenda);
        relacionesConItemLista = ParObjetoPersistente.encontrarRelacionesConClase(prenda,ItemLista.class);
        relacionesConPrenda = ParObjetoPersistente.encontrarRelacionesConClase(prenda,Prenda.class);
        relacionesDesdeObjPersistenteConItemLista = prenda.obtenerRelaciones(ItemLista.class);
        relacionesDesdeObjPersistenteConPrenda = prenda.obtenerRelaciones(Prenda.class);
        cantidadRelaciones = ObjetoPersistente.listarTodos(ParObjetoPersistente.class);

        assertNotNull(par);
        assertNotNull(parInverso);
        assertEquals(par.getId(),parInverso.getId());
        assertEquals(2,cantidadRelaciones.size());
        assertEquals(2,relaciones.size());
        assertEquals(2,relacionesConItemLista.size());
        assertEquals(0,relacionesConPrenda.size());
        assertEquals(2,relacionesDesdeObjPersistenteConItemLista.size());
        assertEquals(0,relacionesDesdeObjPersistenteConPrenda.size());


        ParObjetoPersistente.eliminarRelacionesParaObjeto(prenda);
        assertEquals(0,ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        relaciones = ParObjetoPersistente.encontrarRelacionesParaObjeto(prenda);
        relacionesDesdeObjPersistenteConItemLista = prenda.obtenerRelaciones(ItemLista.class);
        relacionesDesdeObjPersistenteConPrenda = prenda.obtenerRelaciones(Prenda.class);
        assertEquals(0,relaciones.size());
        assertEquals(0,relacionesDesdeObjPersistenteConItemLista.size());
        assertEquals(0,relacionesDesdeObjPersistenteConPrenda.size());
        cantidadRelaciones = ObjetoPersistente.listarTodos(ParObjetoPersistente.class);
        assertEquals(0,cantidadRelaciones.size());


        prenda.agregarRelacionConObjeto(itemLista);
        cantidadRelaciones = ObjetoPersistente.listarTodos(ParObjetoPersistente.class);
        assertEquals(1,cantidadRelaciones.size());

    }


    @Test
    public void autoeliminadoDeRelacionesItemEtiqueta()
    {
        assertEquals(0, ObjetoPersistente.listarTodos(Etiqueta.class).size());
        assertEquals(0, ObjetoPersistente.listarTodos(ItemLista.class).size());
        assertEquals(0, ObjetoPersistente.listarTodos(ParEtiquetaItem.class).size());

        ItemLista item1 = new ItemLista();
        item1.save();
        item1.agregarEtiqueta("etiqueta1");
        item1.agregarEtiqueta("etiquetacompartida");

        ItemLista item2 = new ItemLista();
        item2.save();
        item2.agregarEtiqueta("etiqueta2");
        item2.agregarEtiqueta("etiquetacompartida");

        assertEquals(3, ObjetoPersistente.listarTodos(Etiqueta.class).size());
        assertEquals(2, ObjetoPersistente.listarTodos(ItemLista.class).size());
        assertEquals(4, ObjetoPersistente.listarTodos(ParEtiquetaItem.class).size());

        // Al eliminar item1 deberían eliminarse las relaciones item1-etiqueta1 e item1-etiquetacompartida
        item1.delete();
        assertEquals(3, ObjetoPersistente.listarTodos(Etiqueta.class).size());
        assertEquals(1, ObjetoPersistente.listarTodos(ItemLista.class).size());
        assertEquals(2, ObjetoPersistente.listarTodos(ParEtiquetaItem.class).size());

        // Al eliminar etiquetacompartida debería eliminarse la relación objeto2-etiquetacompartida
        Etiqueta.obtener("etiquetacompartida").delete();;
        assertEquals(2, ObjetoPersistente.listarTodos(Etiqueta.class).size());
        assertEquals(1, ObjetoPersistente.listarTodos(ItemLista.class).size());
        assertEquals(1, ObjetoPersistente.listarTodos(ParEtiquetaItem.class).size());
    }


    @Test
    public void autoeliminadoDeSegundoObjetoEnRelacionesMuchosAMuchosAlEliminarRelaciones()
    {
        assertEquals(0, ObjetoPersistente.listarTodos(Prenda.class).size());
        assertEquals(0, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());

        Prenda prendaPadre          = new Prenda("Padre","ruta/padre");
        Prenda prendaDependiente    = new Prenda("Dependiente","ruta/dependiente");
        Prenda prendaIndependiente  = new Prenda("Independiente","ruta/independiente");

        prendaPadre.agregarRelacionConObjeto(prendaDependiente, true);
        prendaPadre.agregarRelacionConObjeto(prendaIndependiente);

        assertEquals(2, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        assertEquals(3, ObjetoPersistente.listarTodos(Prenda.class).size());

        prendaPadre.eliminarRelacion(prendaDependiente);
        assertEquals(1, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        assertEquals(2, ObjetoPersistente.listarTodos(Prenda.class).size());

        prendaPadre.eliminarRelacion(prendaIndependiente);
        assertEquals(0, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        assertEquals(2, ObjetoPersistente.listarTodos(Prenda.class).size());

        // El borrado del objeto dependiente debe ocurrir sin importar sobre qué objeto se ejecute "eliminarRelacion"
        Prenda prendaDependienteABorrar    = new Prenda("DependienteABorrar","ruta/dependienteABorrar");
        prendaPadre.agregarRelacionConObjeto(prendaDependienteABorrar, true);

        assertEquals(1, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        assertEquals(3, ObjetoPersistente.listarTodos(Prenda.class).size());

        prendaDependienteABorrar.eliminarRelacion(prendaPadre);
        assertEquals(0, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        assertEquals(2, ObjetoPersistente.listarTodos(Prenda.class).size());
    }


    @Test
    public void autoeliminadoDeSegundoObjetoEnRelacionesMuchosAMuchosAlEliminarPadre()
    {
        assertEquals(0, ObjetoPersistente.listarTodos(Prenda.class).size());
        assertEquals(0, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());

        Prenda prendaPadre          = new Prenda("Padre","ruta/padre");
        Prenda prendaDependiente    = new Prenda("Dependiente","ruta/dependiente");
        Prenda prendaIndependiente  = new Prenda("Independiente","ruta/independiente");

        prendaPadre.agregarRelacionConObjeto(prendaDependiente, true);
        prendaPadre.agregarRelacionConObjeto(prendaIndependiente);

        assertEquals(2, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        assertEquals(3, ObjetoPersistente.listarTodos(Prenda.class).size());

        prendaPadre.delete();
        assertEquals(0, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        assertEquals(1, ObjetoPersistente.listarTodos(Prenda.class).size());
    }


    @Test
    public void autoeliminadoDeSegundoObjetoEnRelacionesMuchosAMuchosAlEliminarDependiente()
    {
        assertEquals(0, ObjetoPersistente.listarTodos(Prenda.class).size());
        assertEquals(0, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());

        Prenda prendaPadre          = new Prenda("Padre","ruta/padre");
        Prenda prendaDependiente    = new Prenda("Dependiente","ruta/dependiente");
        Prenda prendaIndependiente  = new Prenda("Independiente","ruta/independiente");

        prendaPadre.agregarRelacionConObjeto(prendaDependiente, true);
        prendaPadre.agregarRelacionConObjeto(prendaIndependiente);

        assertEquals(2, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        assertEquals(3, ObjetoPersistente.listarTodos(Prenda.class).size());

        prendaIndependiente.delete();
        assertEquals(1, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        assertEquals(2, ObjetoPersistente.listarTodos(Prenda.class).size());

        prendaDependiente.delete(); // No debe eliminar el padre
        assertEquals(0, ObjetoPersistente.listarTodos(ParObjetoPersistente.class).size());
        assertEquals(1, ObjetoPersistente.listarTodos(Prenda.class).size());
    }


    @Test
    public void filtrarPorCategoriaYEtiqueta()
    {
        assertEquals(0, ObjetoPersistente.listarTodos(Etiqueta.class).size());
        assertEquals(0, ObjetoPersistente.listarTodos(Prenda.class).size());
        assertEquals(0, ObjetoPersistente.listarTodos(Categoria.class).size());
        assertEquals(0, ObjetoPersistente.listarTodos(ParEtiquetaItem.class).size());


        // Crear categorías
        Categoria catPrenda = Categoria.obtenerOCrear("prenda", null);
        Categoria catPrendaSuperior = Categoria.obtenerOCrear("superior", catPrenda);
        Categoria catPrendaInferior =  Categoria.obtenerOCrear("inferior", catPrenda);
        Categoria catAccesorio = Categoria.obtenerOCrear("accesorio", catPrenda);
        Categoria.obtenerOCrear("remeras", catPrendaSuperior);
        Categoria.obtenerOCrear("camisas", catPrendaSuperior);
        Categoria.obtenerOCrear("camperas", catPrendaSuperior);
        Categoria.obtenerOCrear("pantalones", catPrendaInferior);
        Categoria.obtenerOCrear("guantes", catAccesorio);
        Categoria.obtenerOCrear("corbatas", catAccesorio);


        // Crear y categorizar/etiquetar prendas
        Prenda remera = new Prenda("Remera","ruta/remera");
        Prenda pantalon = new Prenda("Pantalon","ruta/pantalon");
        Prenda campera = new Prenda("Campera","ruta/campera");
        Prenda camisa = new Prenda("Camisa","ruta/camisa");
        Prenda corbata = new Prenda("Corbata","ruta/corbata");

        campera.agregarEtiqueta("frio");
        remera.agregarEtiqueta("calor");
        remera.agregarEtiqueta("informal");
        pantalon.agregarEtiqueta("informal");
        campera.agregarEtiqueta("informal");
        camisa.agregarEtiqueta("formal");
        corbata.agregarEtiqueta("formal");

        campera.setCategoria(Categoria.obtenerDeCualquierPadre("camperas"));
        pantalon.setCategoria(Categoria.obtenerDeCualquierPadre("pantalones"));
        remera.setCategoria(Categoria.obtenerDeCualquierPadre("remeras"));
        camisa.setCategoria(Categoria.obtenerDeCualquierPadre("camisas"));
        corbata.setCategoria(Categoria.obtenerDeCualquierPadre("corbatas"));


        // Buscar por categorías/subcategorías
        List<Prenda> remeras = ListaUtils.listarPorCategoria(Prenda.class, Categoria.obtenerDeCualquierPadre("remeras"), true);
        assertEquals(1, remeras.size()); // Remera

        List<Prenda> accesorios = ListaUtils.listarPorCategoria(Prenda.class, Categoria.obtenerDeCualquierPadre("accesorio"), true);
        assertEquals(1, accesorios.size()); // Corbata

        List<Prenda> superiores = ListaUtils.listarPorCategoria(Prenda.class, Categoria.obtenerDeCualquierPadre("superior"), true);
        assertEquals(3, superiores.size()); // Remera, Camisa, Campera

        List<Prenda> prendas = ListaUtils.listarPorCategoria(Prenda.class, Categoria.obtenerDeCualquierPadre("prenda"), true);
        assertEquals(5, prendas.size()); // Remera, Pantalon, Campera, Camisa, Corbata


        // Buscar por etiqueta
        List<Prenda> informales = ListaUtils.listarPorEtiqueta(Prenda.class, Etiqueta.obtener("informal"));
        assertEquals(3, informales.size()); // Remera, Pantalon, Campera

        List<Prenda> frios = ListaUtils.listarPorEtiqueta(Prenda.class, Etiqueta.obtener("frio"));
        assertEquals(1, frios.size()); // Campera

        List<Prenda> informalesOFrios = ListaUtils.listarPorEtiquetas(Prenda.class, new Etiqueta[]{Etiqueta.obtener("informal"), Etiqueta.obtener("frio")}, false);
        assertEquals(3, informalesOFrios.size()); // Campera, Remera, Pantalon

        List<Prenda> informalesYFrios = ListaUtils.listarPorEtiquetas(Prenda.class, new Etiqueta[]{Etiqueta.obtener("informal"), Etiqueta.obtener("frio")}, true);
        assertEquals(1, informalesYFrios.size()); // Campera


        // Buscar por etiqueta y categoría
        List<Prenda> informalesOFriosSuperiores = ListaUtils.listarPorCategoriaYEtiquetas(Prenda.class, Categoria.obtenerDeCualquierPadre("superior"), true, new Etiqueta[]{Etiqueta.obtener("informal"), Etiqueta.obtener("frio")}, false);
        assertEquals(2, informalesOFriosSuperiores.size()); // Remera, Campera

        List<Prenda> informalesOFriosInferiores = ListaUtils.listarPorCategoriaYEtiquetas(Prenda.class, Categoria.obtenerDeCualquierPadre("inferior"), true, new Etiqueta[]{Etiqueta.obtener("informal"), Etiqueta.obtener("frio")}, false);
        assertEquals(1, informalesOFriosInferiores.size()); // Pantalón

        List<Prenda> informalesYFriosSuperiores = ListaUtils.listarPorCategoriaYEtiquetas(Prenda.class, Categoria.obtenerDeCualquierPadre("superior"), true, new Etiqueta[]{Etiqueta.obtener("informal"), Etiqueta.obtener("frio")}, true);
        assertEquals(1, informalesYFriosSuperiores.size()); // Campera

        List<Prenda> informalesYFriosInferiores = ListaUtils.listarPorCategoriaYEtiquetas(Prenda.class, Categoria.obtenerDeCualquierPadre("inferior"), true, new Etiqueta[]{Etiqueta.obtener("informal"), Etiqueta.obtener("frio")}, true);
        assertEquals(0, informalesYFriosInferiores.size()); // -
    }
}
