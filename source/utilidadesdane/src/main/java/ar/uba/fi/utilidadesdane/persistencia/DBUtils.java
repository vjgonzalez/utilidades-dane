package ar.uba.fi.utilidadesdane.persistencia;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.orm.SchemaGenerator;
import com.orm.SugarContext;
import com.orm.SugarDb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import ar.uba.fi.utilidadesdane.utils.SolicitudPermisos;

/**
 * Crea la base de datos de la aplicación y gestiona el acceso a ella.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class DBUtils {

    /**
     * Base de datos SQLite de la aplicación.
     */
    private static SQLiteDatabase baseDeDatos;

    /**
     * Devuelve la base de datos utilizada por la aplicación.
     *
     * @return base de datos de la aplicación
     */
    static SQLiteDatabase getBD() {
        return baseDeDatos;
    }

    /**
     * Inicializa la base de datos de la aplicación.
     *
     * @param context      Contexto
     * @param borrarTablas True si se desea borrar las tablas, false de lo contrario.
     */
    public static void inicializar(Context context, boolean borrarTablas) {
        SugarContext.terminate();

        SchemaGenerator schemaGenerator = new SchemaGenerator(context);

        if (borrarTablas)
            schemaGenerator.deleteTables(new SugarDb(context).getDB());

        SugarContext.init(context);
        SugarDb sugarDb = new SugarDb(context);
        baseDeDatos = sugarDb.getDB();
        schemaGenerator.createDatabase(baseDeDatos);
    }

    /**
     * Guarda el contenido de la base de datos de la aplicación en un archivo en la memoria del dispositivo.
     * Requiere permisos de WRITE_EXTERNAL_STORAGE.
     *
     * @param nombreBD       Nombre de la base de datos
     * @param nombrePaquete  Nombre del paquete de la aplicación (generalmente obtenido con {@link Context#getPackageName()})
     * @param nombreBDSalida Nombre del archivo de BD a generar
     * @param activity       Activity que solicita el dump
     */
    public static void dump(String nombreBD, String nombrePaquete, String nombreBDSalida, Activity activity) {
        try {
            File sd = Environment.getExternalStorageDirectory();

            if (SolicitudPermisos.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, SolicitudPermisos.CODIGO_REQUEST_WRITE_STORAGE)) {
                String currentDBPath = "/data/data/" + nombrePaquete + "/databases/" + nombreBD;
                String backupDBPath = nombreBDSalida;
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
