package ar.uba.fi.utilidadesdane.imagenes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

import ar.uba.fi.utilidadesdane.R;


/**
 * Activity que gestiona la edición de una fotografía y su almacenamiento en memoria de la aplicación.
 * Subclases: {@link TomarFotoActivity}, para tomar la fotografía con la cámara del dispositivo,
 * y {@link SeleccionarFotoActivity}, para seleccionar la imagen de la galería del dispositivo.
 *
 * @author Virginia González y Alfredo Hodes
 */
public abstract class AdministrarFotosActivity extends AppCompatActivity {

    protected static final int COD_SELECCIONAR_IMAGEN = 201;
    protected static final int COD_CAPTURAR_IMAGEN = 202;

    /**
     * Parámetro a añadir al {@link Intent} que inicie las sub-activities.
     * Su valor debe indicar la carpeta donde se almacenará el archivo de imagen.
     */
    public static final String PARAM_SUBCARPETA = "subcarpeta";

    /**
     * Parámetro en el cual se guarda la uri de la imagen guardada.
     */
    public static final String PARAM_URI_RESULTADO = "uriResultado";

    /**
     * Vista de imagen para recortar.
     */
    protected CropImageView vistaImagen;

    /**
     * Botón para finalizar recorte de la imagen.
     */
    protected FloatingActionButton botonOk;

    /**
     * Imagen recortada.
     */
    protected Bitmap cropped;

    /**
     * Archivo donde se guardará la imagen tomada/seleccionada.
     */
    protected File archivo;

    /**
     * Uri del archivo de imagen.
     */
    protected Uri uriResultado;

    /**
     * @see AppCompatActivity#onCreate(Bundle, PersistableBundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.administrarfotos_layout);

        vistaImagen = findViewById(R.id.cropImageView);

        botonOk = findViewById(R.id.guardar_recorte);
        botonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickOk();
            }
        });
    }

    /**
     * Crea un archivo dentro de la memoria de la aplicación para guardar la imagen.
     * El intent debe contener el parámetro {@value PARAM_SUBCARPETA} indicando la subcarpeta para almacenar las imágenes.
     *
     * @param intent Intent utilizado para llamar a la subclase de AdministrarFotosActivity
     */
    protected void crearArchivo(Intent intent) {
        String subcarpeta = intent.getStringExtra(PARAM_SUBCARPETA);
        archivo = FileAndImageUtils.crearArchivoImagen(this, subcarpeta);
    }

    /**
     * Acción ejecutada al finalizar la edición de la imagen. La almacena en el archivo generado previamente.
     * Finaliza la actividad devolviendo el resultado en el parámetro {@value PARAM_URI_RESULTADO} del intent.
     * Si finalizó correctamente devuelve el resultCode {@value RESULT_OK}, de lo contrario {@value RESULT_CANCELED}.
     * Si hay errores muestra un mensaje.
     */
    protected void onClickOk() {
        cropped = vistaImagen.getCroppedImage();
        Intent resultado = new Intent();

        try {
            FileAndImageUtils.guardarBitmapEnFile(cropped, archivo);
            uriResultado = Uri.fromFile(archivo);
            resultado.putExtra(PARAM_URI_RESULTADO, uriResultado.toString());
            setResult(RESULT_OK, resultado);
            archivo = null;
        } catch (IOException e) {
            mostrarError();
            setResult(RESULT_CANCELED, resultado);
        }
        finish();
    }


    /**
     * Muestra un {@link Toast} con un mensaje de error.
     */
    protected void mostrarError() {
        Toast toast = Toast.makeText(this, R.string.mensaje_error, Toast.LENGTH_LONG);
        toast.show();
    }

    /**
     * Llamado cuando la selección o captura de la imagen finaliza o es cancelada.
     * Habilita la edición de la imagen permitiendo recortarla antes de almacenarla.
     *
     * @param requestCode Código de solicitud ingresado al llamar al método startActivityForResult, para identificar de dónde proviene el resultado.
     * @param resultCode  Código resultado devuelto por la a través de setResult().
     * @param data        Contiene datos de resultado, en este caso la uri de la imagen seleccionada.
     * @see AppCompatActivity#onActivityResult(int, int, Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
            if ((requestCode == COD_SELECCIONAR_IMAGEN || requestCode == COD_CAPTURAR_IMAGEN)) {
                vistaImagen.setImageUriAsync(uriResultado);
            }
    }

}