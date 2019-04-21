package ar.uba.fi.utilidadesdane.cuestionario;

import android.content.Context;
import android.content.Intent;

import ar.uba.fi.utilidadesdane.R;

/**
 *
 *
 * @author Virginia González y Alfredo Hodes
 */
public class AdministradorCuestionario {

    public static final String PARAM_CUESTIONARIO_ID = "cuestionario_id";
    public static final String PARAM_NUM_PREGUNTAS = "num_preguntas";
    public static final String PARAM_CUSTOM_LAYOUT_PREGUNTA_CUESTIONARIO_ID = "custom_layout_pregunta_cuestionario_id";
    public static final String PARAM_CUSTOM_LAYOUT_ITEM_RESPUESTA_ID = "custom_layout_item_respuesta_id";

    /**
     * Obtiene el Intent para iniciar la Activity que presenta el cuestionario, usando vistas por defecto
     * @see AdministradorCuestionario#obtenerIntent(Context, int, Class, int, int, int)
     */
    public static Intent obtenerIntent(Context applicationContext, int idCuestionario, Class cuestionarioActivityClass, int numPreguntas) {
        return obtenerIntent(applicationContext, idCuestionario, cuestionarioActivityClass, numPreguntas, R.layout.pregunta_cuestionario_por_defecto_layout, R.layout.item_respuesta_por_defecto_layout);
    }

    /**
     * Obtiene el Intent para iniciar la Activity que presenta el cuestionario.
     *
     * @param applicationContext contexto
     * @param idCuestionario resource id del XML con la definición del cuestionario
     * @param cuestionarioActivityClass clase custom que presenta el cuestionario. Debe extender {@link CuestionarioBaseActivity}
     * @param numPreguntas cantidad de preguntas a mostrar
     * @param customLayoutPreguntaCuestionarioId resource id del layout para la vista de cada pregunta
     * @param customLayoutItemRespuestaId resource id del layout para la vista de cada opción/respuesta
     * @return intent con la activity configurada, listo para ser pasado como parámetro a startActivity(...)
     */
    public static Intent obtenerIntent(Context applicationContext, int idCuestionario, Class cuestionarioActivityClass, int numPreguntas,
                                       int customLayoutPreguntaCuestionarioId, int customLayoutItemRespuestaId) {
        Intent intent = new Intent(applicationContext, cuestionarioActivityClass);
        intent.putExtra(AdministradorCuestionario.PARAM_CUESTIONARIO_ID, idCuestionario);
        intent.putExtra(AdministradorCuestionario.PARAM_NUM_PREGUNTAS, numPreguntas);
        intent.putExtra(AdministradorCuestionario.PARAM_CUSTOM_LAYOUT_PREGUNTA_CUESTIONARIO_ID, customLayoutPreguntaCuestionarioId);
        intent.putExtra(AdministradorCuestionario.PARAM_CUSTOM_LAYOUT_ITEM_RESPUESTA_ID, customLayoutItemRespuestaId);
        return intent;
    }
}
