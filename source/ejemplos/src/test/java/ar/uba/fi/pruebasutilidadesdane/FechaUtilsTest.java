package ar.uba.fi.pruebasutilidadesdane;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import ar.uba.fi.utilidadesdane.utils.FechaUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FechaUtilsTest {

    String fechaSql = "2018-10-01";
    String horaSql = "13:00:00";
    String mesTexto = "OCTOBER 2018";
    String fechaTexto = "01/10/2018";
    long fechaYHoraLong = 1538409600000L;
    Calendar calendar;
    Date fechaDate;

    @Before
    public void setUp() {
        calendar = Calendar.getInstance();
        calendar.set(2018, 9, 1, 13, 0, 0);
        fechaDate = calendar.getTime();
    }

    @Test
    public void testConvertirLongAFechaSQL() {
        String resultado = FechaUtils.convertirLongAFechaSQL(fechaYHoraLong);
        assertEquals(fechaSql, resultado);
    }

    @Test
    public void testConvertirLongAHoraSQL() {
        String resultado = FechaUtils.convertirLongAHoraSQL(fechaYHoraLong);
        assertEquals(horaSql, resultado);
    }

    @Test
    public void testConvertirDateALong() {
        long resultado = FechaUtils.convertirDateALong(fechaDate);
        assertTrue(resultado >= fechaYHoraLong - 1000 && resultado <= fechaYHoraLong + 1000);
    }

    @Test
    public void testConvertirDateNullALong() {
        long resultado = FechaUtils.convertirDateALong(null);
        assertEquals(-1, resultado);
    }

    @Test
    public void testConvertirDateAFechaSQL() {
        String resultado = FechaUtils.convertirDateAFechaSQL(fechaDate);
        assertEquals(fechaSql, resultado);
    }

    @Test
    public void testConvertirDateAFechaSQLDevuelveNull() {
        String resultado = FechaUtils.convertirDateAFechaSQL(null);
        assertNull(resultado);
    }

    @Test
    public void testConvertirDateAHoraSQL() {
        String resultado = FechaUtils.convertirDateAHoraSQL(fechaDate);
        assertEquals(horaSql, resultado);
    }

    @Test
    public void testConvertirDateAHoraSQLDevuelveNull() {
        String resultado = FechaUtils.convertirDateAHoraSQL(null);
        assertNull(resultado);
    }

    @Test
    public void testConvertirDateATexto() {
        String resultado = FechaUtils.convertirDateATexto(fechaDate);
        assertEquals(fechaTexto, resultado);
    }

    @Test
    public void testConvertirDateATextoDevuelveNull() {
        String resultado = FechaUtils.convertirDateATexto(null);
        assertNull(resultado);
    }

    @Test
    public void testConvertirFechaATextoMes() {
        Date date = calendar.getTime();
        String resultado = FechaUtils.convertirDateATextoMes(date);
        assertEquals(mesTexto, resultado);
    }

    @Test
    public void testConvertirFechaATextoMesDevuelveNull() {
        String resultado = FechaUtils.convertirDateATextoMes(null);
        assertNull(resultado);
    }

    @Test
    public void testConvertirStringADate() throws ParseException {
        Date resultado = FechaUtils.convertirStringADate(fechaSql, horaSql);
        assertTrue(resultado.getTime() >= fechaDate.getTime() - 1000 && resultado.getTime() <= fechaDate.getTime() + 1000);
    }

    @Test
    public void testConvertirStringADateDevuelveNull() throws ParseException {
        Date resultadoFechaNull = FechaUtils.convertirStringADate(null, horaSql);
        Date resultadoHoraNull = FechaUtils.convertirStringADate(fechaSql, null);

        assertNull(resultadoFechaNull);
        assertNull(resultadoHoraNull);
    }

    @Test
    public void testConvertirStringADateLanzaParseException() {
        try {
            FechaUtils.convertirStringADate("1234", "5678");
            fail("Excepción esperada no lanzada");
        } catch (ParseException exception) {
        }
    }

    @Test
    public void testConvertirStringATexto() {
        String resultado = FechaUtils.convertirStringATexto(fechaSql);
        assertEquals(fechaTexto, resultado);
    }

    @Test
    public void testConvertirStringATextoDevuelveNull() {
        String resultadoDosPartes = FechaUtils.convertirStringATexto("12-11");
        String resultadoLetras = FechaUtils.convertirStringATexto("aaaa-11-12");
        String resultadoMenosDigitos = FechaUtils.convertirStringATexto("2018-1-2");

        assertNull(resultadoDosPartes);
        assertNull(resultadoLetras);
        assertNull(resultadoMenosDigitos);
    }

    @Test
    public void testRestarTiempo() {
        long tiempoARestar = 3 * 3600000; //3 hs
        long resultado = FechaUtils.restarTiempo(fechaDate, tiempoARestar);
        assertTrue(resultado >= 1538398800000L - 1000 && resultado <= 1538398800000L + 1000);
    }

    @Test
    public void testRestarTiempoConFechaNull() {
        long tiempoARestar = 3 * 3600000; //3 hs
        long resultado = FechaUtils.restarTiempo(null, tiempoARestar);
        assertEquals(-1, resultado);
    }
}
