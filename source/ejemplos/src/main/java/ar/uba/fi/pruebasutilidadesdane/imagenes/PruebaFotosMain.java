package ar.uba.fi.pruebasutilidadesdane.imagenes;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import ar.uba.fi.pruebasutilidadesdane.R;
import ar.uba.fi.utilidadesdane.imagenes.AdministrarFotosActivity;
import ar.uba.fi.utilidadesdane.imagenes.SeleccionarFotoActivity;
import ar.uba.fi.utilidadesdane.imagenes.TomarFotoActivity;
import ar.uba.fi.utilidadesdane.imagenes.FileAndImageUtils;

public class PruebaFotosMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prueba_fotos_main);
        PackageManager pm = this.getPackageManager();

        Button botonSeleccionarFoto = findViewById(R.id.boton_seleccionar_foto);
        botonSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFotoGaleria();
            }
        });

        Button botonTomarFoto = findViewById(R.id.boton_tomar_foto);
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            //el dispositivo no cuenta con cámara, quito la opción de tomar foto
            botonTomarFoto.setVisibility(View.GONE);
        } else {
            botonTomarFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tomarFoto();
                }
            });
        }

        Button botonCargarFotos = findViewById(R.id.boton_cargar);
        botonCargarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cargarFotos();
            }
        });
    }

    private void cargarFotos() {
        File[] archivos = FileAndImageUtils.getArchivosAlmacenados(this, ".AppImages");
        TextView lista = findViewById(R.id.lista_archivos);
        String listaStr = "";
        if (archivos != null) {
            for (File archivo : archivos) {
                listaStr = listaStr + archivo.getName() + "\n";
            }
        } else {
            listaStr = "Carpeta vacía o inexistente";
        }
        lista.setText(listaStr);
        lista.setVisibility(View.VISIBLE);
    }

    private void tomarFoto() {
        Intent i = new Intent(this, TomarFotoActivity.class);
        i.putExtra(AdministrarFotosActivity.PARAM_SUBCARPETA, "ImagenesTomadas");
        startActivityForResult(i, 0);
    }

    private void seleccionarFotoGaleria() {
        Intent i = new Intent(this, SeleccionarFotoActivity.class);
        i.putExtra(AdministrarFotosActivity.PARAM_SUBCARPETA, "ImagenesGaleria");
        startActivityForResult(i, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Uri uri = Uri.parse(data.getExtras().getString(AdministrarFotosActivity.PARAM_URI_RESULTADO));
            setImagen(uri);
        }
    }

    private void setImagen(Uri contentUri) {
        ImageView vistaImagen = findViewById(R.id.imagenElegida);
        Picasso.with(this).load(contentUri).fit().centerCrop().into(vistaImagen);
        vistaImagen.setVisibility(View.VISIBLE);
    }

}
