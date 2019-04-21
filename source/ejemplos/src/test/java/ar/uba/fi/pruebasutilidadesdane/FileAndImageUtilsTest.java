package ar.uba.fi.pruebasutilidadesdane;

import android.content.Context;
import android.content.res.Resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ar.uba.fi.utilidadesdane.imagenes.FileAndImageUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FileAndImageUtilsTest {

    @Mock
    Context mockContext;
    @Mock
    Resources mockResources;

    @Before
    public void setUp() {
        when(mockContext.getResources()).thenReturn(mockResources);
        when(mockResources.getString(ar.uba.fi.utilidadesdane.R.string.nombre_archivo_imagen)).thenReturn("nombre_");
        when(mockResources.getString(ar.uba.fi.utilidadesdane.R.string.extension_archivo_imagen)).thenReturn(".jpg");
    }

    @Test
    public void testCrearNombreArchivoImagenPorDefecto() {
        String nombre = FileAndImageUtils.crearNombreArchivoImagen(mockContext);
        assertTrue(nombre.matches("nombre_\\d{8}_\\d{6}.jpg"));
    }

    @Test
    public void testCrearNombreArchivoImagen() {
        String nombre = FileAndImageUtils.crearNombreArchivoImagen(mockContext, "archivo");
        assertEquals("archivo.jpg", nombre);
    }
}
