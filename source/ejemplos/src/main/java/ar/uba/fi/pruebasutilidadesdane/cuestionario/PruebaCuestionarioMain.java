package ar.uba.fi.pruebasutilidadesdane.cuestionario;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import ar.uba.fi.pruebasutilidadesdane.R;
import ar.uba.fi.utilidadesdane.cuestionario.AdministradorCuestionario;
import ar.uba.fi.utilidadesdane.cuestionario.Cuestionario;
import ar.uba.fi.utilidadesdane.cuestionario.CuestionarioBaseActivity;

/**
 * Created by Alfredo on 9/3/2017.
 */

public class PruebaCuestionarioMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pruebas_cuestionario_main);


        findViewById(R.id.iniciar_cuestionario_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = AdministradorCuestionario.obtenerIntent(getApplicationContext(), R.xml.cuestionario_test, CuestionarioBaseActivity.class, 2);
                startActivity(intent);
            }
        });

        findViewById(R.id.iniciar_cuestionario_custom_layout_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = AdministradorCuestionario.obtenerIntent(getApplicationContext(), R.xml.cuestionario_test, CuestionarioCustomActivity.class, 2,
                        R.layout.pregunta_cuestionario_custom_layout, R.layout.item_respuesta_custom_layout);
                startActivity(intent);
            }
        });
    }
}
