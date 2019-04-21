package ar.uba.fi.pruebasutilidadesdane;

import android.app.Activity;
import android.os.Bundle;
import ar.uba.fi.pruebasutilidadesdane.listasypersistencia.PruebaListasYPersistencia;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new PruebaListasYPersistencia(getApplicationContext(),this);
    }
}
