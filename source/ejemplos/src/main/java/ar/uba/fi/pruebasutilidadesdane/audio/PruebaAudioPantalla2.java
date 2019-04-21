package ar.uba.fi.pruebasutilidadesdane.audio;

import android.content.Intent;
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

public class PruebaAudioPantalla2 extends AppCompatActivity {

    private boolean continuarSonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba_audio_otra);
        continuarSonido = true;

        Button botonSiguiente = findViewById(R.id.boton_siguiente);
        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguiente();
            }
        });


        Button botonMute = findViewById(R.id.boton_sonido);
        botonMute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarSonido();
            }
        });
        cambiarEstadoBoton();


        TextView textView = findViewById(R.id.texto);
        textView.setText("Música de esta Activity: \"My hero\"");
    }

    public void modificarSonido() {
        if (AudioFondo.getSilencio())
            AudioFondo.setSilencio(false);
        else
            AudioFondo.setSilencio(true);
        cambiarEstadoBoton();
    }

    private void cambiarEstadoBoton() {
        boolean silencio = AudioFondo.getSilencio();
        Button botonMute = findViewById(R.id.boton_sonido);
        if (silencio)
            botonMute.setText("Unmute");
        else
            botonMute.setText("Mute");
    }


    public void siguiente() {
        Intent intent = new Intent(this, PruebaAudioPantalla3.class);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!continuarSonido) {
            AudioFondo.pausar();
        }
        cambiarEstadoBoton();

    }

    @Override
    protected void onResume() {
        super.onResume();
        continuarSonido = false;
        try {
            AudioFondo.start(this, 1, true);
            cambiarEstadoBoton();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (AudioNoExistenteException e) {
            e.printStackTrace();
        }
    }
}
