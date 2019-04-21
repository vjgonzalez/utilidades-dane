package ar.uba.fi.pruebasutilidadesdane.audio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ar.uba.fi.pruebasutilidadesdane.R;
import ar.uba.fi.utilidadesdane.audio.AudioFondo;
import ar.uba.fi.utilidadesdane.audio.AudioNoExistenteException;

/**
 * Created by Vir on 24-Jul-17.
 */

public class PruebaAudioPantalla4 extends AppCompatActivity {

    private boolean continuarSonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba_audio_otra);
        continuarSonido = true;
        Button botonSiguiente = findViewById(R.id.boton_siguiente);
        botonSiguiente.setVisibility(View.GONE);

        TextView textView = findViewById(R.id.texto);
        textView.setText("Música de esta Activity: \"Times like these\"");
        Button botonMute = findViewById(R.id.boton_sonido);
        botonMute.setVisibility(View.GONE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!continuarSonido) {
            AudioFondo.pausar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        continuarSonido = false;
        try {
            AudioFondo.start(this, 2, true);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (AudioNoExistenteException e) {
            e.printStackTrace();
        }
    }

}
