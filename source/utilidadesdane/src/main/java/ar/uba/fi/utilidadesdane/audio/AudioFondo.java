package ar.uba.fi.utilidadesdane.audio;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * Maneja el audio de fondo en la app. Puede utilizarse para una única Activity o mantenerse en diferentes pantallas.
 * También es posible cambiar a diferentes audios en cada pantalla.
 *
 * @author Virginia González y Alfredo Hodes
 */
public abstract class AudioFondo {

    private static final int AUDIO_ANTERIOR = -1;

    private static String ERROR_INDICE_FUERA_DE_RANGO = "Falla al reproducir audio en posición %d. La cantidad de recursos de audio es de %d";

    /**
     * Listado de reproductores de audio a utilizar en las diferentes pantallas
     */
    private static ArrayList<MediaPlayer> listaMediaPlayers = new ArrayList<>();

    /**
     * índice que indica la posición del audio que se está reproduciendo en el array {@link #recursosAudio}
     */
    private static int audioActual = -1;

    /**
     * índice que indica la posición del último audio reproducido en el array {@link #recursosAudio}
     */
    private static int audioAnterior = -1;

    /**
     * Listado de recursos asociados a los audios a utilizar en las diferentes pantallas
     */
    private static int[] recursosAudio = null;

    /**
     * Indicador de sonido, true en caso de que esté silenciado, de lo contrario false
     */
    private static boolean audioSilenciado = false;


    /**
     * Establece los archivos de audio a utilizar dentro de toda la aplicación.
     * Es el primer método que debe llamarse de esta clase. Sólo debe llamarse una vez.
     *
     * @param conjuntoRecursosAudio recursos (R.raw...) asociados a los archivos de audio
     */
    public static void setAudios(int[] conjuntoRecursosAudio) {
        recursosAudio = conjuntoRecursosAudio;
    }

    /**
     * Permite silenciar o volver a habilitar el sonido
     *
     * @param silencio true si se desea silenciar, false si se desea habilitar el sonido
     */
    public static void setSilencio(boolean silencio) {
        if (listaMediaPlayers.size() == 0) {
            return; //Si se llama este método sin haber inicializado la música, no hace nada
        }
        audioSilenciado = silencio;
        if (silencio)
            pausar();
        else
            reanudar();
    }

    /**
     * @return true si el sonido está silenciada, falso de lo contrario
     */
    public static boolean getSilencio() {
        return audioSilenciado;
    }

    /**
     * Comienza a reproducir el audio seleccionado, a menos que el reproductor esté silenciado.
     * Invocar a {@link #setAudios(int[])} antes para añadir los recursos de audio.
     *
     * @param context     Activity desde la que se invoca el método
     * @param indiceAudio índice que indica la posición del audio a reproducir en el array {@link #recursosAudio}
     * @param loop        True para reproducir el sonido en loop, false de lo contrario
     * @throws InstantiationException excepción que indica que no se pudo realizar la reproducción del audio indicado
     */
    public static void start(Context context, int indiceAudio, boolean loop) throws InstantiationException, AudioNoExistenteException {
        if (listaMediaPlayers.size() == 0)
            setUpMediaPlayers(context, loop);

        if (indiceAudio == AUDIO_ANTERIOR) {
            indiceAudio = audioAnterior;
        }

        // No existe un recurso en la posición indicada
        if (indiceAudio >= listaMediaPlayers.size()) {
            String mensajeError = String.format(ERROR_INDICE_FUERA_DE_RANGO, indiceAudio, listaMediaPlayers.size());
            throw new AudioNoExistenteException(mensajeError);
        }

        if (audioActual == indiceAudio) {
            // ya se está reproduciendo el audio indicado, no es necesario hacer nada
            return;
        }

        if (audioActual != -1) {
            //se está reproduciendo otro audio, hay que pausar y cambiar al nuevo
            pausar();
        }

        audioActual = indiceAudio;

        if (audioSilenciado)
            return;

        MediaPlayer mediaPlayer = listaMediaPlayers.get(indiceAudio);

        if (mediaPlayer != null) {
            if (!mediaPlayer.isPlaying()) {
                mediaPlayer.start();
            }
        }
    }

    /**
     * Pausa todos los audios
     */
    public static void pausar() {
        Iterator iterator = listaMediaPlayers.iterator();
        MediaPlayer mediaPlayer;

        while (iterator.hasNext()) {
            mediaPlayer = (MediaPlayer) iterator.next();
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }

        if (audioActual != -1)
            audioAnterior = audioActual;

        audioActual = -1;
    }

    /**
     * Reanudar el último audio reproducido
     */
    protected static void reanudar() {
        int audioAReproducir = audioActual;
        if (audioAReproducir == -1)
            audioAReproducir = audioAnterior;
        MediaPlayer mp = listaMediaPlayers.get(audioAReproducir);
        if (mp != null) {
            if (!mp.isPlaying()) {
                mp.start();
            }
        }
    }


    /**
     * Libera los recursos utilizados.
     */
    public static void release() {
        Iterator iterator = listaMediaPlayers.iterator();
        MediaPlayer mediaPlayer;

        while (iterator.hasNext()) {
            mediaPlayer = (MediaPlayer) iterator.next();
            try {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    mediaPlayer.release();
                }
            } catch (Exception e) {
            }
        }

        listaMediaPlayers.clear();
        if (audioActual != -1) {
            audioAnterior = audioActual;
        }
        audioActual = -1;
    }


    /**
     * Inicializa los media players asociados a cada recurso de audio
     *
     * @param context Contexto
     * @param loop    True para reproducir el sonido en loop, false de lo contrario
     * @throws InstantiationException Lanzada cuando el reproductor no pudo crearse
     */
    protected static void setUpMediaPlayers(Context context, boolean loop) throws InstantiationException {
        for (int i = 0; i < recursosAudio.length; i++) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, recursosAudio[i]);
            if (mediaPlayer == null)
                throw new InstantiationException();
            listaMediaPlayers.add(i, mediaPlayer);
            mediaPlayer.setLooping(loop);
        }
    }

    /**
     * Devuelve los recursos de audio
     *
     * @return Recursos de audio
     */
    public static int[] getRecursosAudio() {
        return recursosAudio;
    }
}