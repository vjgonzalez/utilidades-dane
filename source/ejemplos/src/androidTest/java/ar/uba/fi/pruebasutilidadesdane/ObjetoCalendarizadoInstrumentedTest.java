package ar.uba.fi.pruebasutilidadesdane;

import android.support.test.runner.AndroidJUnit4;

import com.orm.SugarContext;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ar.uba.fi.pruebasutilidadesdane.calendario.PruebaObjetoCalendarizado;
import ar.uba.fi.utilidadesdane.calendario.ObjetoCalendarizado;
import ar.uba.fi.utilidadesdane.persistencia.DBUtils;
import ar.uba.fi.utilidadesdane.persistencia.ObjetoPersistente;
import ar.uba.fi.utilidadesdane.utils.FechaUtils;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ObjetoCalendarizadoInstrumentedTest {

    private PruebaObjetoCalendarizado pruebaObjetoCalendarizado1;
    private PruebaObjetoCalendarizado pruebaObjetoCalendarizado2;
    private PruebaObjetoCalendarizado pruebaObjetoCalendarizado3;
    private PruebaObjetoCalendarizado pruebaObjetoCalendarizado4;
    private PruebaObjetoCalendarizado pruebaObjetoCalendarizado5;

    @Before
    public void setUp() throws Exception {
        SugarContext.init(getTargetContext());

        DBUtils.inicializar(getTargetContext(), true);

        ObjetoPersistente.deleteAll(ObjetoPersistente.class);
        crearObjetosBD();
    }

    public void crearObjetosBD() throws ParseException {
        DateFormat df = new SimpleDateFormat("hhmmss_yyyy.MM.dd");

        Date date1 = df.parse("200000_2017.08.15");
        pruebaObjetoCalendarizado1 = new PruebaObjetoCalendarizado(date1.getTime(), "Valor1");

        Date date2 = df.parse("001015_2017.07.02");
        pruebaObjetoCalendarizado2 = new PruebaObjetoCalendarizado(date2.getTime());

        DateFormat df2 = new SimpleDateFormat("dd.MM.yyyy");
        Date date3 = df2.parse("20.06.2017");
        pruebaObjetoCalendarizado3 = new PruebaObjetoCalendarizado(date3.getTime(), "Valor3");

        Date date4 = df2.parse("02.07.2017");
        pruebaObjetoCalendarizado4 = new PruebaObjetoCalendarizado(date4.getTime());

        Date date5 = df2.parse("02.07.2017");
        pruebaObjetoCalendarizado5 = new PruebaObjetoCalendarizado(date5.getTime(), "Valor5");
        pruebaObjetoCalendarizado5.setIdNotificacion(1000);
    }

    @Test
    public void testObtencionDeDatos() {
        assertEquals(pruebaObjetoCalendarizado1.getFecha(), "2017-08-15");
        assertEquals(pruebaObjetoCalendarizado2.getFecha(), "2017-07-02");
        assertEquals(pruebaObjetoCalendarizado3.getFecha(), "2017-06-20");
        assertEquals(pruebaObjetoCalendarizado4.getFecha(), "2017-07-02");
        assertEquals(pruebaObjetoCalendarizado5.getFecha(), "2017-07-02");

        assertEquals(pruebaObjetoCalendarizado1.getHora(), "20:00:00");
        assertEquals(pruebaObjetoCalendarizado2.getHora(), "00:10:15");
        assertEquals(pruebaObjetoCalendarizado3.getHora(), "00:00:00");
        assertEquals(pruebaObjetoCalendarizado4.getHora(), "00:00:00");
        assertEquals(pruebaObjetoCalendarizado5.getHora(), "00:00:00");

        assertEquals(pruebaObjetoCalendarizado1.getHoraSinSegundos(), "20:00");
        assertEquals(pruebaObjetoCalendarizado2.getHoraSinSegundos(), "00:10");
        assertEquals(pruebaObjetoCalendarizado3.getHoraSinSegundos(), "00:00");
        assertEquals(pruebaObjetoCalendarizado4.getHoraSinSegundos(), "00:00");
        assertEquals(pruebaObjetoCalendarizado5.getHoraSinSegundos(), "00:00");

        assertEquals(pruebaObjetoCalendarizado1.getOtroDato(), "Valor1");
        assertEquals(pruebaObjetoCalendarizado2.getOtroDato(), PruebaObjetoCalendarizado.datoDefault);
        assertEquals(pruebaObjetoCalendarizado3.getOtroDato(), "Valor3");
        assertEquals(pruebaObjetoCalendarizado4.getOtroDato(), PruebaObjetoCalendarizado.datoDefault);
        assertEquals(pruebaObjetoCalendarizado5.getOtroDato(), "Valor5");
    }

    @Test
    public void verificarIDNotificaciones() {
        assertEquals(pruebaObjetoCalendarizado1.getIdNotificacion(), -1);
        assertEquals(pruebaObjetoCalendarizado2.getIdNotificacion(), -1);
        assertEquals(pruebaObjetoCalendarizado3.getIdNotificacion(), -1);
        assertEquals(pruebaObjetoCalendarizado4.getIdNotificacion(), -1);
        assertEquals(pruebaObjetoCalendarizado5.getIdNotificacion(), 1000);

        assertFalse(pruebaObjetoCalendarizado1.tieneNotificacion());
        assertFalse(pruebaObjetoCalendarizado2.tieneNotificacion());
        assertFalse(pruebaObjetoCalendarizado3.tieneNotificacion());
        assertFalse(pruebaObjetoCalendarizado4.tieneNotificacion());
        assertTrue(pruebaObjetoCalendarizado5.tieneNotificacion());
    }

    @Test
    public void testObtenerPorFechaYHora() {
        List<PruebaObjetoCalendarizado> resultados15Agosto = ObjetoCalendarizado.obtenerPorFechayHora(PruebaObjetoCalendarizado.class, "2017-08-15", "20:00:00");
        List<PruebaObjetoCalendarizado> resultados2Julio = ObjetoCalendarizado.obtenerPorFechayHora(PruebaObjetoCalendarizado.class, "2017-07-02", "00:10:15");

        assertEquals(1, resultados15Agosto.size());
        assertEquals(1, resultados2Julio.size());
    }

    @Test
    public void testObtenerPorFecha() throws ParseException {
        List<PruebaObjetoCalendarizado> resultadosConStr = ObjetoCalendarizado.obtenerPorFecha(PruebaObjetoCalendarizado.class, "2017-07-02");

        Date fecha = FechaUtils.convertirStringADate("2017-07-02", "00:00:00");
        List<PruebaObjetoCalendarizado> resultadosConDate = ObjetoCalendarizado.obtenerPorFecha(PruebaObjetoCalendarizado.class, fecha);

        assertEquals(3, resultadosConStr.size());
        assertEquals(3, resultadosConDate.size());
        assertEquals(resultadosConDate.get(0).getFecha(), pruebaObjetoCalendarizado4.getFecha());
        assertEquals(resultadosConDate.get(0).getHora(), pruebaObjetoCalendarizado4.getHora());
    }

    @Test
    public void testObtenerPorRangoDeFechas() throws ParseException {
        List<PruebaObjetoCalendarizado> resultados;

        Date fecha1 = FechaUtils.convertirStringADate("2017-07-02", "00:00:00");
        Date fecha2 = FechaUtils.convertirStringADate("2017-08-15", "23:59:59");
        resultados = ObjetoCalendarizado.obtenerRangoFecha(PruebaObjetoCalendarizado.class, fecha1, fecha2);

        fecha1 = FechaUtils.convertirStringADate("2017-08-15", "00:00:00");
        fecha2 = FechaUtils.convertirStringADate("2017-07-02", "00:00:00");
        List<PruebaObjetoCalendarizado> resultadosInversos = ObjetoCalendarizado.obtenerRangoFecha(PruebaObjetoCalendarizado.class, fecha1, fecha2);

        assertEquals(4, resultados.size());
        assertEquals(resultados.get(0).getFecha(), pruebaObjetoCalendarizado4.getFecha());
        assertEquals(resultados.get(0).getHora(), pruebaObjetoCalendarizado4.getHora());
        assertEquals(resultados.get(3).getFecha(), pruebaObjetoCalendarizado1.getFecha());
        assertEquals(resultados.get(3).getHora(), pruebaObjetoCalendarizado1.getHora());
        assertEquals(4, resultadosInversos.size());

    }

    @Test
    public void testListarTodos() {

        List<PruebaObjetoCalendarizado> resultados = ObjetoCalendarizado.listarTodos(PruebaObjetoCalendarizado.class);
        assertEquals(5, resultados.size());
    }

    @Test
    public void testObtenerEventosPosteriores() throws ParseException {
        Date fecha1 = FechaUtils.convertirStringADate("2017-07-02", "00:00:00");
        List<PruebaObjetoCalendarizado> objetos2Julio = ObjetoCalendarizado.obtenerEventosPosteriores(PruebaObjetoCalendarizado.class, FechaUtils.convertirDateAFechaSQL(fecha1));
        Date fecha2 = FechaUtils.convertirStringADate("2017-08-15", "23:59:59");
        List<PruebaObjetoCalendarizado> objetos15Agosto = ObjetoCalendarizado.obtenerEventosPosteriores(PruebaObjetoCalendarizado.class, fecha2);

        assertEquals(4, objetos2Julio.size());
        assertEquals(1, objetos15Agosto.size());

    }

    @After
    public void tearDown() {
        ObjetoCalendarizado.borrarTodos(PruebaObjetoCalendarizado.class);
        SugarContext.terminate();

    }
}
