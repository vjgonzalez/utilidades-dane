package ar.uba.fi.utilidadesdane.cuestionario;

import android.content.Context;
import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Representa un cuestionario con preguntas y opciones múltiples.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class Cuestionario {

    /**
     * Preguntas a mostrar.
     */
    private Vector<Pregunta> preguntas;

    /**
     * Cantidad de preguntas del cuestionario a mostrar.
     * Si es mayor a la cantidad de preguntas del cuestionario, se muestran todas las preguntas.
     */
    int cantPreguntasAMostrar;

    /**
     * Crea un nuevo cuestionario con todas las preguntas a partir de un archivo XML.
     *
     * @param id        ID del recurso XML que contiene las preguntas
     * @param context   Contexto donde se utilizará el cuestionario
     * @param aleatorio True si las preguntas deben mostrarse en orden aleatorio, false de lo contrario
     * @return Un nuevo cuestionario
     * @throws IOException            Error de lectura del archivo XML
     * @throws XmlPullParserException Error de formato en el archivo XML
     */
    public static Cuestionario crear(int id, Context context, Boolean aleatorio) throws IOException, XmlPullParserException {
        return crear(id, -1, context, aleatorio);
    }

    /**
     * Crea un nuevo cuestionario a partir de un archivo XML.
     *
     * @param id           ID del recurso XML que contiene las preguntas
     * @param numPreguntas Cantidad de preguntas a mostrar. Si es menor o igual a cero o mayor a la cantidad de preguntas del cuestionario, se muestran todas
     * @param context      Contexto donde se utilizará el cuestionario
     * @param aleatorio    True si las preguntas deben mostrarse en orden aleatorio, false de lo contrario
     * @return Un nuevo cuestionario
     * @throws IOException            Error de lectura del archivo XML
     * @throws XmlPullParserException Error de formato en el archivo XML
     */
    public static Cuestionario crear(int id, int numPreguntas, Context context, Boolean aleatorio) throws XmlPullParserException, IOException {
        Cuestionario cuestionario = new Cuestionario(numPreguntas);
        XmlResourceParser xrp = context.getResources().getXml(id);

        String textoPregunta = "";
        Vector<String> respuestasCorrectasPosibles = new Vector<>();
        Vector<String> respuestasIncorrectasPosibles = new Vector<>();
        int puntaje = 0;

        int eventType = xrp.getEventType();
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlResourceParser.START_DOCUMENT:
                    break;
                case XmlResourceParser.START_TAG:
                    switch (xrp.getName()) {
                        case "pregunta":
                            puntaje = xrp.getAttributeValue(null, "puntaje") != null ? Integer.parseInt(xrp.getAttributeValue(null, "puntaje")) : 1;
                            textoPregunta = xrp.getAttributeValue(null, "texto");
                            respuestasCorrectasPosibles = new Vector<>();
                            respuestasIncorrectasPosibles = new Vector<>();
                            break;
                        case "respuestaCorrecta":
                            respuestasCorrectasPosibles.add(xrp.getAttributeValue(null, "texto"));
                            break;
                        case "respuestaInCorrecta":
                            respuestasIncorrectasPosibles.add(xrp.getAttributeValue(null, "texto"));
                            break;
                    }
                    break;
                case XmlResourceParser.END_TAG:
                    if (xrp.getName().equals("pregunta")) {
                        cuestionario.agregarPregunta(crearPregunta(textoPregunta, respuestasCorrectasPosibles, respuestasIncorrectasPosibles, puntaje));
                    }
                    break;
                case XmlResourceParser.TEXT:
                    break;
            }
            eventType = xrp.next();
        }

        if (aleatorio) Collections.shuffle(cuestionario.preguntas);
        return cuestionario;
    }

    /**
     * Constructor
     *
     * @param cantPreguntasAMostrar Cantidad de preguntas a mostrar. Si es menor o igual a cero o mayor a la cantidad de preguntas del cuestionario, se muestran todas
     */
    private Cuestionario(int cantPreguntasAMostrar) {
        this.preguntas = new Vector<>();
        this.cantPreguntasAMostrar = cantPreguntasAMostrar;
    }

    /**
     * Crea una pregunta con sólo una de las respuestas correctas posibles como opción.
     *
     * @param textoPregunta                 Texto de la pregunta
     * @param respuestasCorrectasPosibles   Respuestas correctas a la pregunta
     * @param respuestasIncorrectasPosibles Respuestas incorrectas a la pregunta
     * @param puntaje                       Puntaje que otorga responder correctamente a la pregunta
     * @return Una nueva pregunta
     */
    private static Pregunta crearPregunta(String textoPregunta, Vector<String> respuestasCorrectasPosibles, Vector<String> respuestasIncorrectasPosibles, int puntaje) {
        // Elige una respuesta correcta entre todas las posibles y la suma a las opciones de la pregunta
        String respuestaCorrecta = respuestasCorrectasPosibles.get(new Random().nextInt(respuestasCorrectasPosibles.size()));
        return new Pregunta(textoPregunta, respuestaCorrecta, respuestasIncorrectasPosibles, puntaje);
    }

    /**
     * Devuelve una {@link Pregunta} a partir de su ID
     *
     * @param id ID de la pregunta a obtener
     * @return La pregunta buscada, o null si no se encuentra.
     */
    public Pregunta obtenerPregunta(int id) {
        if (preguntas != null && preguntas.size() > id)
            return preguntas.get(id);
        return null;
    }

    /**
     * Agrega una pregunta al cuestionario.
     *
     * @param nuevaPregunta
     */
    private void agregarPregunta(Pregunta nuevaPregunta) {
        this.preguntas.add(nuevaPregunta);
    }

    /**
     * Devuelve la cantidad de preguntas a mostrar.
     *
     * @return Cantidad de preguntas a mostrar
     */
    public int cantidadPreguntas() {
        if (cantPreguntasAMostrar > 0 && cantPreguntasAMostrar < preguntas.size())
            return cantPreguntasAMostrar;
        return preguntas.size();
    }

    /**
     * Devuelve la cantidad de preguntas del cuestionario que ya fueron realizadas.
     *
     * @return Cantidad de preguntas ya realizadas
     */
    public int cantidadPreguntasEvaluadas() {
        int cant = 0;
        for (int i = 0; i < cantidadPreguntas(); i++) {
            if (obtenerPregunta(i).getResultado() != Pregunta.RESULTADO_NO_EVALUADA) cant++;
        }
        return cant;
    }

    /**
     * Devuelve la cantidad de preguntas del cuestionario que fueron respondidas correctamente.
     *
     * @return Cantidad de preguntas respondidas correctamente
     */
    public int cantidadPreguntasCorrectas() {
        int cant = 0;
        for (int i = 0; i < cantidadPreguntas(); i++) {
            if (obtenerPregunta(i).getResultado() == Pregunta.RESULTADO_CORRECTO) cant++;
        }
        return cant;
    }

    /**
     * Devuelve el puntaje obtenido.
     *
     * @return Puntaje obtenido
     */
    public int puntajeObtenido() {
        int puntaje = 0;
        for (int i = 0; i < cantidadPreguntas(); i++) {
            puntaje += obtenerPregunta(i).getPuntajeObtenido();
        }
        return puntaje;
    }

    /**
     * Devuelve el puntaje total del cuestionario (la suma de los puntajes asignados a cada pregunta).
     *
     * @return Puntaje total del cuestionario
     */
    public int puntajeTotalCuestionario() {
        int puntaje = 0;
        for (int i = 0; i < cantidadPreguntas(); i++) {
            puntaje += obtenerPregunta(i).getPuntaje();
        }
        return puntaje;
    }

}
