package ar.uba.fi.pruebasutilidadesdane;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import ar.uba.fi.utilidadesdane.cuestionario.Cuestionario;
import ar.uba.fi.utilidadesdane.cuestionario.Pregunta;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class CuestionarioTest {

    public Cuestionario cuestionario;


    @Before
    public void setUp() throws IOException, XmlPullParserException {
        crearCuestionario();
    }

    public void crearCuestionario() throws IOException, XmlPullParserException {
        cuestionario = Cuestionario.crear(R.xml.cuestionario_test, InstrumentationRegistry.getTargetContext(), false);
    }

    @Test
    public void valoresInicialesCuestionario() {
        assertEquals(3, cuestionario.cantidadPreguntas());

        for (int i = 0; i < cuestionario.cantidadPreguntas(); i++) {
            Pregunta p = cuestionario.obtenerPregunta(i);
            assertEquals(Pregunta.RESULTADO_NO_EVALUADA, p.getResultado());
        }

        // Verificar textos en primera pregunta
        Pregunta p0 = cuestionario.obtenerPregunta(0);
        assertEquals("¿DE QUÉ COLOR ES EL CIELO?", p0.getTextoPregunta());
        assertEquals("CELESTE", p0.getRespuestaCorrecta());
        assertEquals(4, p0.getRespuestasIncorrectas().size());
        assertEquals("ROJO", p0.getRespuestasIncorrectas().get(0));
    }

    @Test
    public void obtenerPuntaje() {
        // Segunda pregunta: Puntaje 10 seteado en XML
        assertEquals(10, cuestionario.obtenerPregunta(1).getPuntaje());

        // Tercera pregunta: Puntaje 2 asignado por defecto
        assertEquals(1, cuestionario.obtenerPregunta(2).getPuntaje());
    }

    @Test
    public void evaluarPregunta() {
        // Primera pregunta: "¿DE QUÉ COLOR ES EL CIELO?". Respuesta correcta: "CELESTE"
        Pregunta p0 = cuestionario.obtenerPregunta(0);

        // Antes de responder
        assertEquals(Pregunta.RESULTADO_NO_EVALUADA, p0.getResultado());
        assertEquals(0, p0.getPuntajeObtenido());

        // Responder incorrectamente
        assertEquals(Pregunta.RESULTADO_INCORRECTO, p0.evaluar("ROJO"));
        assertEquals(Pregunta.RESULTADO_INCORRECTO, p0.getResultado());
        assertEquals(0, p0.getPuntajeObtenido());

        // Responder correctamente
        assertEquals(Pregunta.RESULTADO_CORRECTO, p0.evaluar("CELESTE"));
        assertEquals(Pregunta.RESULTADO_CORRECTO, p0.getResultado());
        assertEquals(p0.getPuntaje(), p0.getPuntajeObtenido());
        assertEquals(p0.getPuntaje(), 1);
    }

    @Test
    public void responderCuestionarioCompleto() {
        // Al iniciar el cuestionario
        assertEquals(0, cuestionario.cantidadPreguntasEvaluadas());
        assertEquals(0, cuestionario.cantidadPreguntasCorrectas());
        assertEquals(0, cuestionario.puntajeObtenido());
        assertEquals(12, cuestionario.puntajeTotalCuestionario());


        // Responder incorrectamente la primera pregunta
        cuestionario.obtenerPregunta(0).evaluar(cuestionario.obtenerPregunta(0).getRespuestasIncorrectas().firstElement());
        assertEquals(1, cuestionario.cantidadPreguntasEvaluadas());
        assertEquals(0, cuestionario.cantidadPreguntasCorrectas());
        assertEquals(0, cuestionario.puntajeObtenido());
        assertEquals(12, cuestionario.puntajeTotalCuestionario());

        // Responder correctamente la primera pregunta
        cuestionario.obtenerPregunta(0).evaluar(cuestionario.obtenerPregunta(0).getRespuestaCorrecta());
        assertEquals(1, cuestionario.cantidadPreguntasEvaluadas());
        assertEquals(1, cuestionario.cantidadPreguntasCorrectas());
        assertEquals(1, cuestionario.puntajeObtenido());
        assertEquals(12, cuestionario.puntajeTotalCuestionario());


        // Responder incorrectamente la segunda pregunta
        cuestionario.obtenerPregunta(1).evaluar(cuestionario.obtenerPregunta(1).getRespuestasIncorrectas().firstElement());
        assertEquals(2, cuestionario.cantidadPreguntasEvaluadas());
        assertEquals(1, cuestionario.cantidadPreguntasCorrectas());
        assertEquals(1, cuestionario.puntajeObtenido());
        assertEquals(12, cuestionario.puntajeTotalCuestionario());

        // Responder correctamente la segunda pregunta
        cuestionario.obtenerPregunta(1).evaluar(cuestionario.obtenerPregunta(1).getRespuestaCorrecta());
        assertEquals(2, cuestionario.cantidadPreguntasEvaluadas());
        assertEquals(2, cuestionario.cantidadPreguntasCorrectas());
        assertEquals(11, cuestionario.puntajeObtenido());
        assertEquals(12, cuestionario.puntajeTotalCuestionario());


        // Responder incorrectamente la tercera pregunta
        cuestionario.obtenerPregunta(2).evaluar(cuestionario.obtenerPregunta(2).getRespuestasIncorrectas().firstElement());
        assertEquals(3, cuestionario.cantidadPreguntasEvaluadas());
        assertEquals(2, cuestionario.cantidadPreguntasCorrectas());
        assertEquals(11, cuestionario.puntajeObtenido());
        assertEquals(12, cuestionario.puntajeTotalCuestionario());

        // Responder correctamente la tercera pregunta
        cuestionario.obtenerPregunta(2).evaluar(cuestionario.obtenerPregunta(2).getRespuestaCorrecta());
        assertEquals(3, cuestionario.cantidadPreguntasEvaluadas());
        assertEquals(3, cuestionario.cantidadPreguntasCorrectas());
        assertEquals(12, cuestionario.puntajeObtenido());
        assertEquals(12, cuestionario.puntajeTotalCuestionario());
    }


    @Test
    public void opciones() {
        for (int i = 0; i < cuestionario.cantidadPreguntas(); i++) {
            Pregunta p = cuestionario.obtenerPregunta(i);
            assertEquals(p.getRespuestaCorrecta(), p.getOpciones().get(p.getIdxOpcionCorrecta()).getTextoOpcion());

            for (int j = 0; j < p.getCantOpcionesAMostrar(); j++) {
                int res = (j == p.getIdxOpcionCorrecta()) ? Pregunta.RESULTADO_CORRECTO : Pregunta.RESULTADO_INCORRECTO;

                // evaluar por String
                assertEquals(res, p.evaluar(p.getOpciones().get(j)));

                // evaluar por idx
                assertEquals(res, p.evaluar(j));
            }
        }
    }
}
