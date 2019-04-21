package ar.uba.fi.utilidadesdane.imagenes;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

/**
 * Activity que gestiona la captura de una fotografía utilizando la cámara del dispositivo, y su almacenamiento en memoria de la aplicación.
 * Antes de utilizarla debe verificarse si el dispositivo cuenta con una cámara para tomar fotografías.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class TomarFotoActivity extends AdministrarFotosActivity {

    /**
     * Inicialización de la activity. Crea un archivo para que la imagen capturada sea almacenada allí.
     *
     * @param savedInstanceState Valores de instancia guardada. Null si no se guardó un estado previo.
     * @see AdministrarFotosActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        crearArchivo(getIntent());

        Uri uriFoto = CustomFileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".ar.uba.fi.utilidadesdane.provider", archivo);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto);

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            intent.setClipData(ClipData.newRawUri("", uriFoto));
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }

        startActivityForResult(intent, COD_CAPTURAR_IMAGEN);
    }

    /**
     * Llamado cuando la captura de fotografía finaliza o es cancelada.
     * Si se capturó una imagen, se habilita la edición de la misma permitiendo recortarla antes de almacenarla.
     *
     * @param requestCode Código de solicitud ingresado al llamar al método startActivityForResult, para identificar de dónde proviene el resultado.
     * @param resultCode  Código resultado devuelto por la a través de setResult().
     * @param data        Contiene datos de resultado, en este caso el objeto es null.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == COD_CAPTURAR_IMAGEN) {
            uriResultado = Uri.fromFile(archivo);
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            finish();
        }
    }
}