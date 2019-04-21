package ar.uba.fi.pruebasutilidadesdane.cuestionario;

import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import ar.uba.fi.utilidadesdane.cuestionario.CuestionarioBaseActivity;
import ar.uba.fi.utilidadesdane.utils.Ejecutable;

/**
 * Created by Alfredo on 11/5/2017.
 */

public class CuestionarioCustomActivity extends CuestionarioBaseActivity {

    @Override
    protected void mostrarFeedbackOpcionCorrecta(View opcionElegidaView, final Ejecutable onComplete) {

        Toast toast = Toast.makeText(this, "CUSTOM FEEDBACK OPCION CORRECTA", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        // Para continuar con un delay y darle tiempo a la animación
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (onComplete != null) {
                    onComplete.ejecutar();
                }
            }
        }, 3000);
    }

    @Override
    protected void mostrarFeedbackOpcionIncorrecta(View opcionElegidaView, final Ejecutable onComplete) {

        Toast toast = Toast.makeText(this, "CUSTOM FEEDBACK OPCION INCORRECTA", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        // Para continuar con un delay y darle tiempo a la animación
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (onComplete != null) {
                    onComplete.ejecutar();
                }
            }
        }, 3000);
    }

    @Override
    protected void cuestionarioCompleto(View opcionElegidaView) {
        Toast toast = Toast.makeText(this, "CUSTOM FEEDBACK CUESTIONARIO COMPLETO", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();

        // Para continuar con un delay y darle tiempo a la animación
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                finish();
            }
        }, 3000);
    }
}
