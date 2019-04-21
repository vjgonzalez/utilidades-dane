package ar.uba.fi.utilidadesdane.cuestionario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import ar.uba.fi.utilidadesdane.R;
import ar.uba.fi.utilidadesdane.utils.Ejecutable;

/**
 * Activity que muestra las preguntas de un cuestionario una por una y recibe el input del usuario como respuesta, lo procesa y calcula el puntaje.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class CuestionarioBaseActivity extends Activity {

    /**
     * Cuestionario con las preguntas a mostrar.
     */
    private Cuestionario cuestionario;

    /**
     * Vista para mostrar las opciones de cada pregunta.
     */
    private AbsListView opcionesListView;

    /**
     * Adaptador para mostrar los items {@link Opcion} asociados a cada pregunta.
     */
    private AdaptadorOpcionesCuestionario adaptadorOpciones;

    /**
     * Creación de la activity.
     * El intent debe recibir el parámetro PARAM_CUSTOM_LAYOUT_PREGUNTA_CUESTIONARIO_ID con el ID del archivo XML que contiene las preguntas.
     *
     * @see Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int layoutId = intent.getIntExtra(AdministradorCuestionario.PARAM_CUSTOM_LAYOUT_PREGUNTA_CUESTIONARIO_ID, R.layout.pregunta_cuestionario_por_defecto_layout);

        setContentView(layoutId);

        crearCuestionario();
    }

    /**
     * Crea el cuestionario a partir del contenido del archivo XML indicado.
     */
    private void crearCuestionario() {
        Intent intent = getIntent();

        int cuestionarioId = intent.getIntExtra(AdministradorCuestionario.PARAM_CUESTIONARIO_ID, 0);
        int numPreguntas = intent.getIntExtra(AdministradorCuestionario.PARAM_NUM_PREGUNTAS, -1);

        try {
            cuestionario = Cuestionario.crear(cuestionarioId, numPreguntas, getApplicationContext(), true);
        } catch (Exception exception) {
            Toast toast = Toast.makeText(this, R.string.error_parseo_cuestionario, Toast.LENGTH_LONG);
            toast.show();
        }

        opcionesListView = findViewById(R.id.opciones);

        mostrarSiguientePregunta();
        for (int i = 0; i < cuestionario.cantidadPreguntas(); i++) {
            Pregunta p = cuestionario.obtenerPregunta(i);
            String respuestasIncorrectasStr = "";
            for (int j = 0; j < p.getRespuestasIncorrectas().size(); j++) {
                respuestasIncorrectasStr = respuestasIncorrectasStr.concat(p.getRespuestasIncorrectas().get(j));
                if (j < p.getRespuestasIncorrectas().size() - 1)
                    respuestasIncorrectasStr = respuestasIncorrectasStr.concat(", ");
            }
        }
    }

    /**
     * Muestra la siguiente pregunta del cuestionario.
     */
    private void mostrarSiguientePregunta() {
        int idxPreguntaActual = cuestionario.cantidadPreguntasEvaluadas();
        int cantPreguntasTotal = cuestionario.cantidadPreguntas();

        ((TextView) findViewById(R.id.numero_pregunta)).setText(getString(R.string.cuestionario_pregunta_x_de_y, (idxPreguntaActual + 1), cantPreguntasTotal));

        Pregunta p = cuestionario.obtenerPregunta(idxPreguntaActual);
        ((TextView) findViewById(R.id.pregunta)).setText(p.textoPregunta);

        Intent intent = getIntent();
        int layoutItemRespuestaId = intent.getIntExtra(AdministradorCuestionario.PARAM_CUSTOM_LAYOUT_ITEM_RESPUESTA_ID, R.layout.item_respuesta_por_defecto_layout);
        adaptadorOpciones = new AdaptadorOpcionesCuestionario(p.opciones, this, layoutItemRespuestaId);

        opcionesListView.setAdapter(adaptadorOpciones);

        final Pregunta preguntaActual = p;
        opcionesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int resultado = preguntaActual.evaluar(position);
                Opcion opcionElegida = (Opcion) adaptadorOpciones.getItem(position);
                if (resultado == Pregunta.RESULTADO_CORRECTO) {
                    onOpcionCorrecta(view);
                } else {
                    onOpcionIncorrecta(view);
                }
            }
        });
    }

    /**
     * Acción a ejecutar cuando la opción elegida sea correcta.
     *
     * @param opcionElegidaView vista de la opción seleccionada
     */
    private void onOpcionCorrecta(final View opcionElegidaView) {
        mostrarFeedbackOpcionCorrecta(opcionElegidaView, new Ejecutable() {
            @Override
            public void ejecutar() {
                if (cuestionario.cantidadPreguntasEvaluadas() == cuestionario.cantidadPreguntas()) {
                    cuestionarioCompleto(opcionElegidaView);
                } else {
                    mostrarSiguientePregunta();
                }
            }
        });
    }

    /**
     * Acción a ejecutar cuando la opción elegida sea incorrecta.
     *
     * @param opcionElegidaView vista de la opción seleccionada
     */
    private void onOpcionIncorrecta(View opcionElegidaView) {
        mostrarFeedbackOpcionIncorrecta(opcionElegidaView, null);
    }

    /**
     * Muestra una animación que indica al usuario que la opción seleccionada es correcta.
     *
     * @param opcionElegidaView Vista de la opción seleccionada
     * @param onComplete        Acción a ejecutar al finalizar la animación
     */
    protected void mostrarFeedbackOpcionCorrecta(View opcionElegidaView, final Ejecutable onComplete) {
        animarVista(findViewById(R.id.resultado_correcto), AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in), onComplete);
    }

    /**
     * Muestra una animación que indica al usuario que la opción seleccionada es incorrecta.
     *
     * @param opcionElegidaView Vista de la opción seleccionada
     * @param onComplete        Acción a ejecutar al finalizar la animación
     */
    protected void mostrarFeedbackOpcionIncorrecta(View opcionElegidaView, final Ejecutable onComplete) {
        animarVista(findViewById(R.id.resultado_incorrecto), AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in), onComplete);
    }

    /**
     * Anima la vista recibida en la forma indicada y ejecuta una acción al finalizar.
     *
     * @param vistaResultado     Vista a animar
     * @param animacionResultado Animación a aplicar
     * @param onComplete         Acción a ejecutar al finalizar la animación
     */
    protected void animarVista(final View vistaResultado, Animation animacionResultado, final Ejecutable onComplete) {
        findViewById(R.id.bloqueador).setVisibility(View.VISIBLE);

        animacionResultado.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                findViewById(R.id.bloqueador).setVisibility(View.INVISIBLE);
                vistaResultado.setVisibility(View.INVISIBLE);

                if (onComplete != null) {
                    onComplete.ejecutar();
                }
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        vistaResultado.startAnimation(animacionResultado);
        vistaResultado.setVisibility(View.VISIBLE);
    }

    /**
     * Esta función debe encargarse de avanzar a la siguiente Activity cuando el cuestionario finaliza.
     *
     * @param opcionElegidaView Última opción seleccionada
     */
    protected void cuestionarioCompleto(View opcionElegidaView) {
        animarVista(findViewById(R.id.cuestionario_completo), AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in), new Ejecutable() {
            @Override
            public void ejecutar() {
                finish();
            }
        });
    }

}
