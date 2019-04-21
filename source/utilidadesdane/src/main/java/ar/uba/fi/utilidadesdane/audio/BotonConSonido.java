package ar.uba.fi.utilidadesdane.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;

/**
 * Vista de botón con un sonido corto asociado que se reproduce al hacer click sobre él.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class BotonConSonido extends AppCompatButton {

    /**
     * Contexto donde se utiliza el botón
     */
    private Context context;

    /**
     * Reproductor del sonido
     */
    private SoundPool soundPool;

    /**
     * True si el sonido está listo para reproducirse, false de lo contrario
     */
    private boolean cargado = false;

    /**
     * Identificador asignado al sonido por el reproductor
     */
    private int soundId;

    /**
     * Recurso asociado al sonido a reproducir al seleccionar el botón
     */
    private int resource = -1;

    /**
     * Constructor requerido por herencia
     */
    public BotonConSonido(Context context) {
        super(context);
        this.context = context;
        setUp();
    }

    /**
     * Constructor requerido por herencia
     */
    public BotonConSonido(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setUp();
    }

    /**
     * Constructor requerido por herencia
     */
    public BotonConSonido(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setUp();
    }

    /**
     * Establece el sonido a reproducir cuando se selecciona el botón
     *
     * @param resource recurso (R.raw...) asociado al archivo de sonido
     */
    public void setResource(int resource) {
        this.resource = resource;
        soundId = soundPool.load(context, resource, 1);
    }

    /**
     * Inicialización del botón. No debe llamarse directamente, lo hace el constructor.
     */
    protected void setUp() {
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                cargado = true;
            }
        });
        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener();
            }
        });
    }

    /**
     * Acción al realizar cuando el botón es seleccionado. Se asocia en el método {@link #setUp}
     */
    protected void listener() {
        if (resource != -1) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            float actualVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            float volume = actualVolume / maxVolume;
            if (cargado) {
                soundPool.play(soundId, volume, volume, 1, 0, 1f);
            }
        }
    }

}