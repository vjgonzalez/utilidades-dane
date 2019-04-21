package ar.uba.fi.utilidadesdane.imagenes;

import android.content.Intent;
import android.os.Bundle;

/**
 * Activity que gestiona la selección de una fotografía de la galería y su almacenamiento en memoria de la aplicación.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class SeleccionarFotoActivity extends AdministrarFotosActivity {

    /**
     * Inicialización de la activity. Crea un archivo para que la imagen seleccionada sea almacenada allí.
     * @see AdministrarFotosActivity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        crearArchivo(getIntent());

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, COD_SELECCIONAR_IMAGEN);
    }

    /**
     * Llamado cuando la selección de imagen de la galería finaliza o es cancelada.
     * Si se seleccionó una imagen, se habilita la edición de la misma permitiendo recortarla antes de almacenarla.
     *
     * @param requestCode Código de solicitud ingresado al llamar al método startActivityForResult, para identificar de dónde proviene el resultado.
     * @param resultCode  Código resultado de la acción.
     * @param data        Contiene datos de resultado, en este caso la uri de la imagen seleccionada.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == COD_SELECCIONAR_IMAGEN && data != null) {
                uriResultado = data.getData();
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            finish();
        }
    }

}
