package ar.uba.fi.pruebasutilidadesdane.video;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;

import ar.uba.fi.pruebasutilidadesdane.R;
import ar.uba.fi.utilidadesdane.video.VideoFragment;

public class PruebaVideoMain extends Activity {

    VideoFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba_video_main);

        // Elemento de la vista al cual se añade el video
        if (findViewById(R.id.vistaRaiz) != null) {
            if (savedInstanceState == null) {
                String uriPath = "android.resource://" + getPackageName() + "/" + R.raw.video;
                fragment = VideoFragment.crearNuevaInstancia(uriPath);
                getFragmentManager().beginTransaction().replace(R.id.vistaRaiz, fragment, VideoFragment.FRAGMENT_TAG).commit();
            } else {
                fragment = (VideoFragment) getFragmentManager().findFragmentByTag(VideoFragment.FRAGMENT_TAG);
            }
            addListener();
        }
    }

    protected void addListener() {
        MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        };
        fragment.addCallbackAction(listener);
    }

}
