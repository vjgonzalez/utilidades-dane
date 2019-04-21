package ar.uba.fi.utilidadesdane.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Clase para manejar solicitudes de permisos que la aplicación requiera para su funcionamiento
 *
 * @author Virginia González y Alfredo Hodes
 */
public class SolicitudPermisos {

    public static final int CODIGO_REQUEST_WRITE_STORAGE = 700;

    /**
     * Verifica o solicita permiso al usuario
     *
     * @param activity Activity donde se requiere el permiso
     * @param permiso  Permiso a verificar
     * @param codigo   Código del permiso solicitado
     * @return True si el usuario otorgó el permiso, false de lo contrario.
     */
    public static boolean checkPermission(Activity activity, String permiso, int codigo) {
        if (Build.VERSION.SDK_INT < 23) {
            //SDK menor a 23, no es necesario solicitar permiso
            return true;
        }
        //SDK mayor o igual a 23, se deben verificar los permisos
        int permissionCheck = ContextCompat.checkSelfPermission(activity, permiso);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Permisos otorgados
            return true;
        } else {
            //Solicitar permisos
            ActivityCompat.requestPermissions(activity, new String[]{permiso}, codigo);
            permissionCheck = ContextCompat.checkSelfPermission(activity, permiso);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                //Permisos otorgados
                return true;
            } else {
                //Permisos no otorgados
                return false;
            }
        }
    }
}
