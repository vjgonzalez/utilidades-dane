package ar.uba.fi.utilidadesdane.app;

import android.app.Application;
import android.content.SharedPreferences;

import com.orm.SugarContext;

import ar.uba.fi.utilidadesdane.persistencia.DBUtils;

/**
 * Clase base para mantener el estado global de la aplicación.
 * Inicializa la base de datos y las preferencias
 *
 * @author Virginia González y Alfredo Hodes
 */
public class DaneApplication extends Application {
    private static DaneApplication instance;

    /**
     * @see Application#onCreate()
     */
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SugarContext.init(this);

        DBUtils.inicializar(getApplicationContext(), false);
    }

    /**
     * @see Application#onTerminate()
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    /**
     * Devuelve un objeto SharedPreferences a través del cual se puede leer y/o modificar el valor de un archivo.
     *
     * @param archivoDePreferencias Archivo de preferencias deseado.
     * @return archivo de preferencias
     */
    public static SharedPreferences obtenerSharedPreferences(String archivoDePreferencias) {
        return instance.getSharedPreferences(archivoDePreferencias, 0);
    }
}
