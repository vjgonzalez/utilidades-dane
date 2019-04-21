package ar.uba.fi.pruebasutilidadesdane.autenticacion;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import ar.uba.fi.pruebasutilidadesdane.R;
import ar.uba.fi.utilidadesdane.autenticacion.Autenticacion;
import ar.uba.fi.utilidadesdane.autenticacion.ComandoAdministrador;

/**
 * Created by Alfredo on 7/6/2017.
 */

public class PruebaAutenticacionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pruebas_autenticacion_main);
        Autenticacion.instancia().setMensajeDeCodigoIncorrecto("CÓDIGO INCORRECTO - INTENTA NUEVAMENTE");

        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("DANE", " AUTENTIC - LOGIN");
                Autenticacion.instancia().autenticar(getFragmentManager());
            }
        });

        findViewById(R.id.logout_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("DANE", " AUTENTIC - LOGOUT");
                Autenticacion.instancia().setEsAdmin(false);
            }
        });

        findViewById(R.id.set_pin_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("DANE", " AUTENTIC - SETPIN");
                Autenticacion.instancia().solicitarNuevoCodigoAdmin(getFragmentManager());
            }
        });

        findViewById(R.id.ejecutar_sin_loguear_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("DANE", " AUTENTIC - EJECUTAR SIN LOGUEAR");
                if (Autenticacion.instancia().esAdmin()) {
                    mostrarToastEjecucionOK();
                } else {
                    mostrarToastEjecucionError();
                }
            }
        });

        findViewById(R.id.ejecutar_btn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("DANE", " AUTENTIC - EJECUTAR CON LOGUEO");
                ComandoAdministrador ejecutarSiAdmin = new ComandoAdministrador() {
                    @Override
                    public void ejecutar() {
                        Log.d("DANE", " AUTENTIC - EJECUTAR OK!");
                        mostrarToastEjecucionOK();
                    }
                };
                ComandoAdministrador ejecutarSiNoAdmin = new ComandoAdministrador() {
                    @Override
                    public void ejecutar() {
                        Log.d("DANE", " AUTENTIC - EJECUTAR ERROR!");
                        mostrarToastEjecucionError();
                    }
                };
                Autenticacion.instancia().autenticarYEjecutar(ejecutarSiAdmin, ejecutarSiNoAdmin, getFragmentManager());
            }
        });

    }

    private void mostrarToastEjecucionOK() {
        Log.d("DANE", " AUTENTIC - mostrarToastEjecucionOK");
        mostrarToastGenerico("EJECUCIÓN OK");
    }

    private void mostrarToastEjecucionError() {
        Log.d("DANE", " AUTENTIC - mostrarToastEjecucionError");
        mostrarToastGenerico("EJECUCIÓN ERROR");
    }

    private void mostrarToastGenerico(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }
}
