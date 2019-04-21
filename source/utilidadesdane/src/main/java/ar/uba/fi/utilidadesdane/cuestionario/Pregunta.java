package ar.uba.fi.utilidadesdane.cuestionario;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;

/**
 * Representa una pregunta con opciones múltiples.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class Pregunta {

    public static final int RESULTADO_NO_EVALUADA = 0;
    public static final int RESULTADO_CORRECTO = 1;
    public static final int RESULTADO_INCORRECTO = 2;

    String textoPregunta;
    Vector<Opcion> opciones;
    int cantOpcionesAMostrar;

    private String respuestaCorrecta;
    private Vector<String> respuestasIncorrectas;
    private int resultado = RESULTADO_NO_EVALUADA;

    private int puntaje = 0;
    private int puntajeObtenido = 0;

    private int idxOpcionCorrecta;

    /**
     * Constructor.
     *
     * @param textoPregunta         Texto de la pregunta
     * @param respuestaCorrecta     Respuesta correcta
     * @param respuestasIncorrectas Respuestas incorrectas
     * @param puntaje               Puntaje asignado a la pregunta
     */
    public Pregunta(String textoPregunta, String respuestaCorrecta, Vector<String> respuestasIncorrectas, int puntaje) {
        this(textoPregunta, respuestaCorrecta, respuestasIncorrectas, puntaje, respuestasIncorrectas.size() + 1);
    }

    /**
     * Constructor.
     *
     * @param textoPregunta         Texto de la pregunta
     * @param respuestaCorrecta     Respuesta correcta
     * @param respuestasIncorrectas Respuestas incorrectas
     * @param puntaje               Puntaje asignado a la pregunta
     * @param cantOpcionesAMostrar  Cantidad de opciones a mostrar. Si es menor o igual a cero o mayor a la cantidad de opciones del cuestionario, se muestran todas
     */
    public Pregunta(String textoPregunta, String respuestaCorrecta, Vector<String> respuestasIncorrectas, int puntaje, int cantOpcionesAMostrar) {
        this.textoPregunta = textoPregunta;
        this.respuestaCorrecta = respuestaCorrecta;
        this.respuestasIncorrectas = respuestasIncorrectas;
        this.resultado = RESULTADO_NO_EVALUADA;
        this.puntaje = puntaje;
        this.puntajeObtenido = 0;
        if (cantOpcionesAMostrar > 0 && cantOpcionesAMostrar < respuestasIncorrectas.size() + 1)
            this.cantOpcionesAMostrar = cantOpcionesAMostrar;
        else
            this.cantOpcionesAMostrar = respuestasIncorrectas.size() + 1;
        armarListaOpciones();
    }

    /**
     * Arma la lista de opciones incluyendo una opción correcta ubicada en una posición aleatoria.
     */
    private void armarListaOpciones() {
        // 1: Desordenar las respuestas incorrectas
        String[] rtasIncorrectasDesordenadas = respuestasIncorrectas.toArray(new String[respuestasIncorrectas.size()]);
        Collections.shuffle(Arrays.asList(rtasIncorrectasDesordenadas));

        // 2: Determinar el idx donde va a estar la rta correcta entre las opciones
        idxOpcionCorrecta = new Random().nextInt(cantOpcionesAMostrar);

        // 3: Ir populando el vector de opciones con las incorrectas, salvo en el idx
        opciones = new Vector<>();
        int idxIncorrectas = 0;
        for (int i = 0; i < cantOpcionesAMostrar; i++) {
            if (i == idxOpcionCorrecta) {
                opciones.add(new Opcion(respuestaCorrecta));
            } else {
                opciones.add(new Opcion(rtasIncorrectasDesordenadas[idxIncorrectas]));
                idxIncorrectas++;
                if (idxIncorrectas >= rtasIncorrectasDesordenadas.length) {
                    idxIncorrectas = 0;
                }
            }
        }
    }

    /**
     * Procesa la respuesta seleccionada para una pregunta, calcula el puntaje obtenido y devuelve el resultado
     *
     * @param respuestaSeleccionada Texto de la opción elegida
     * @return {@value RESULTADO_CORRECTO} si la respuesta es correcta, {@value RESULTADO_INCORRECTO} si es incorrecta,
     */
    public int evaluar(String respuestaSeleccionada) {
        if (respuestaSeleccionada.equals(respuestaCorrecta)) {
            resultado = RESULTADO_CORRECTO;
            puntajeObtenido = puntaje;
        } else {
            resultado = RESULTADO_INCORRECTO;
            puntajeObtenido = 0;
        }
        return resultado;
    }

    /**
     * Procesa la respuesta seleccionada para una pregunta, calcula el puntaje obtenido y devuelve el resultado
     *
     * @param idxRespuestaSeleccionada Indice de la opción elegida
     * @return {@value RESULTADO_CORRECTO} si la respuesta es correcta, {@value RESULTADO_INCORRECTO} si es incorrecta,
     */
    public int evaluar(int idxRespuestaSeleccionada) {
        return evaluar(opciones.get(idxRespuestaSeleccionada).getTextoOpcion());
    }

    /**
     * Procesa la respuesta seleccionada para una pregunta, calcula el puntaje obtenido y devuelve el resultado
     *
     * @param opcion {@link Opcion} elegida
     * @return {@value RESULTADO_CORRECTO} si la respuesta es correcta, {@value RESULTADO_INCORRECTO} si es incorrecta,
     */
    public int evaluar(Opcion opcion) {
        return evaluar(opcion.getTextoOpcion());
    }

    /**
     * Devuelve el resultado de la pregunta.
     *
     * @return {@value RESULTADO_NO_EVALUADA} si la pregunta no fue evaluada. {@value RESULTADO_CORRECTO} si la respuesta es correcta. {@value RESULTADO_INCORRECTO} si es incorrecta,
     */
    public int getResultado() {
        return resultado;
    }

    /**
     * Devuelve el puntaje asignado a la pregunta.
     *
     * @return Puntaje asignado a la pregunta
     */
    public int getPuntaje() {
        return puntaje;
    }

    /**
     * Devuelve el puntaje obtenido al responder la pregunta.
     *
     * @return Puntaje obtenido al responder la pregunta.
     */
    public int getPuntajeObtenido() {
        return puntajeObtenido;
    }

    /**
     * Devuelve las respuestas incorrectas.
     *
     * @return Respuestas incorrectas
     */
    public Vector<String> getRespuestasIncorrectas() {
        return respuestasIncorrectas;
    }

    /**
     * Devuelve el texto de la pregunta.
     *
     * @return Texto de la pregunta
     */
    public String getTextoPregunta() {
        return textoPregunta;
    }

    /**
     * Devuelve las opciones de respuesta a la pregunta.
     *
     * @return Opciones de respuesta a la pregunta
     */
    public Vector<Opcion> getOpciones() {
        return opciones;
    }

    /**
     * Devuelve la respuesta correcta a la pregunta.
     *
     * @return Respuesta correcta a la pregunta
     */
    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    /**
     * Devuelve el índice de la respuesta correcta en el listado de opciones.
     *
     * @return Indice de la respuesta correcta
     */
    public int getIdxOpcionCorrecta() {
        return idxOpcionCorrecta;
    }

    /**
     * Devuelve la cantidad de opciones a mostrar.
     *
     * @return Cantidad de opciones a mostrar
     */
    public int getCantOpcionesAMostrar() {
        return cantOpcionesAMostrar;
    }
}
