package ar.uba.fi.utilidadesdane.video;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import ar.uba.fi.utilidadesdane.R;

/**
 * Fragmento que contiene un reproductor de video.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class VideoFragment extends Fragment {

    /**
     * Nombre del tag del VideoView en el layout
     */
    public static final String FRAGMENT_TAG = "videoView";

    /**
     * Vista de video
     */
    protected VideoView videoView;

    /**
     * Posición actual de la reproducción del video
     */
    protected int posicionActual = 0;

    /**
     * Vista de controles del video
     */
    protected MediaController mediaController;

    /**
     * Ruta del archivo de video a reproducir
     */
    protected String pathVideo;

    /**
     * URI del archivo de video a reproducir
     */
    protected Uri uriVideo;

    /**
     * Acción a ejecutar cuando el video finaliza su reproducción. Seteado en {@link #addCallbackAction(MediaPlayer.OnCompletionListener)}
     */
    protected MediaPlayer.OnCompletionListener onComplListener;

    /**
     * Crea una nueva instancia.
     *
     * @param pathVideo Path del archivo de video a reproducir
     * @return Fragmento de video a utilizar dentro de una Activity
     */
    public static VideoFragment crearNuevaInstancia(String pathVideo) {
        VideoFragment f = new VideoFragment();
        f.setPathVideo(pathVideo);
        return f;
    }

    /**
     * @param inflater           Inflater de la vista
     * @param container          Vista "madre" del fragmento
     * @param savedInstanceState Si no es nulo, es una reconstrucción de un estado guardado del fragmento
     * @return View
     * @see Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            pathVideo = savedInstanceState.getString("Path");
        }

        uriVideo = Uri.parse(pathVideo);

        View rootView = inflater.inflate(R.layout.video_fragment, container, false);

        if (mediaController == null) {
            mediaController = new MediaController(getActivity());
        }

        videoView = rootView.findViewById(R.id.video_view);

        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);

        if (onComplListener != null)
            videoView.setOnCompletionListener(onComplListener);

        videoView.setVideoURI(uriVideo);

        videoView.requestFocus();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                    @Override
                    public void onSeekComplete(MediaPlayer mp) {
                        videoView.pause();
                        mediaController.show();
                    }
                });

                videoView.seekTo(posicionActual);
                if (posicionActual == 0) {
                    mediaController.show();
                    videoView.start();
                } else {
                    mediaController.show();
                    videoView.pause();
                }
            }
        });
        return rootView;
    }

    /**
     * Guarda el estado actual del fragmento (ruta y posición del video) para reconstruirlo luego.
     *
     * @param outState Bundle donde guardar el estado guardado
     * @see Fragment#onSaveInstanceState(Bundle)
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        posicionActual = videoView.getCurrentPosition();
        outState.putInt("Posicion", posicionActual);
        outState.putString("Path", pathVideo);
        videoView.pause();
    }

    /**
     * Ejecutado luego de que se encuentra creada la {@link android.app.Activity} contenedora e instanciada la vista del fragmento.
     * Restaura el estado guardado en caso de que exista.
     *
     * @param savedInstanceState Estado guardado del objeto. Si no es null, se restaura el archivo de video y la posición.
     * @see Fragment#onActivityCreated(Bundle)
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            posicionActual = savedInstanceState.getInt("Posicion");
            videoView.seekTo(posicionActual);
        }
    }

    /**
     * Modifica la acción a ejecutar cuando finaliza la reproducción del video.
     *
     * @param listener Acción a ejecutar al finalizar la reproducción del video.
     */
    public void addCallbackAction(MediaPlayer.OnCompletionListener listener) {
        onComplListener = listener;
    }

    /**
     * Modifica el archivo de video a reproducir
     *
     * @param pathVideo Ruta del archivo de video
     */
    public void setPathVideo(String pathVideo) {
        this.pathVideo = pathVideo;
    }

    /**
     * Pausa la reproducción del video.
     */
    public void pausarVideo() {
        if (videoPlaying())
            videoView.pause();
    }

    /**
     * Reproduce el video desde el comienzo o a partir de la última posición pausada si existiera.
     */
    public void comenzarVideo() {
        if (!videoPlaying())
            videoView.start();
    }

    /**
     * Indica si el video se está reproduciendo.
     *
     * @return True si el video está reproduciéndose, false de lo contrario
     */
    public boolean videoPlaying() {
        return videoView.isPlaying();
    }
}