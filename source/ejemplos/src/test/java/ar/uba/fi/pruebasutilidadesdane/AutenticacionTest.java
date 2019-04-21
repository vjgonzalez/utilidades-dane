package ar.uba.fi.pruebasutilidadesdane;

import android.app.FragmentManager;
import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;

import ar.uba.fi.utilidadesdane.app.DaneApplication;
import ar.uba.fi.utilidadesdane.autenticacion.Autenticacion;
import ar.uba.fi.utilidadesdane.autenticacion.AutenticacionDialog;
import ar.uba.fi.utilidadesdane.autenticacion.ComandoAdministrador;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AutenticacionTest {

    private boolean ejecutado;

    @Mock
    AutenticacionDialog mockAutenticacionDialog;

    @Mock
    SharedPreferences mockSharedPreferences;

    @Mock
    DaneApplication mockDaneApplication;

    @Before
    public void setUp() {
        mockDaneApplication = mock(DaneApplication.class);
        setMock(mockDaneApplication);
        when(mockDaneApplication.obtenerSharedPreferences("AutenticacionPrefs")).thenReturn(mockSharedPreferences);

        doNothing().when(mockAutenticacionDialog).show(any(FragmentManager.class), anyString());
        when(mockSharedPreferences.contains("codigo_admin")).thenReturn(false);
        Autenticacion.instancia().setEsAdmin(false);
        Autenticacion.instancia().setMensajeDeCodigoIncorrecto(null);
        ejecutado = false;

    }

    private void setMock(DaneApplication mock) {
        try {
            Field instance = DaneApplication.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void comandoEsEjecutadoCuandoUsuarioEsAdmin() {
        Autenticacion.instancia().setEsAdmin(true);
        ComandoAdministrador cmd = new ComandoAdministrador() {
            @Override
            public void ejecutar() {
                ejecutado = true;
            }
        };
        Autenticacion.instancia().autenticarYEjecutar(cmd, null);
        assertTrue(ejecutado);
    }

}
