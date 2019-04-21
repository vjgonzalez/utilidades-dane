package ar.uba.fi.pruebasutilidadesdane.audio;

import android.app.Activity;
import android.os.Bundle;

import ar.uba.fi.pruebasutilidadesdane.R;
import ar.uba.fi.utilidadesdane.audio.BotonConSonido;

public class PruebaBotonConSonidoMain extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba_sonido_main);

        BotonConSonido boton = findViewById(R.id.botonConSonido);
        boton.setResource(R.raw.minisonido);
    }

}