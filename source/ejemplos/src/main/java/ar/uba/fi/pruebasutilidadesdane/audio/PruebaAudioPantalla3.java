package ar.uba.fi.pruebasutilidadesdane.audio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ar.uba.fi.pruebasutilidadesdane.R;

/**
 * Created by Vir on 24-Jul-17.
 */

public class PruebaAudioPantalla3 extends AppCompatActivity {

    private boolean continuarSonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba_audio_otra);
        continuarSonido = false;

        Button botonSiguiente = findViewById(R.id.boton_siguiente);
        botonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                siguiente();
            }
        });
        TextView textView = findViewById(R.id.texto);
        textView.setText("En esta pantalla no se reproduce música");


        Button botonMute = findViewById(R.id.boton_sonido);
        botonMute.setVisibility(View.GONE);
    }

    public void siguiente() {
        Intent intent = new Intent(this, PruebaAudioPantalla4.class);
        startActivity(intent);
    }


}
