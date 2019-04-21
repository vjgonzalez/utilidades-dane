package ar.uba.fi.utilidadesdane.imagenes;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import ar.uba.fi.utilidadesdane.R;
import ar.uba.fi.utilidadesdane.utils.SolicitudPermisos;

/**
 * Clase de utilidades para operaciones sobre archivos
 *
 * @author Virginia González y Alfredo Hodes
 */
public class FileAndImageUtils {

    /**
     * Formato del timestamp a utilizar como nombre default de los archivos.
     */
    protected static final SimpleDateFormat formatoNombreArchivo = new SimpleDateFormat("yyyyMMdd_HHmmss");

    /**
     * Ruta del directorio donde se almacenarán los archivos.
     */
    protected static File rutaAlmacenamiento;

    /**
     * Crea un archivo en la carpeta de imágenes de la aplicación.
     *
     * @param context          Contexto de la aplicación
     * @param nombreSubcarpeta Nombre de la subcarpeta en la cual se desea guardar el archivo. Si no existe es creada.
     * @param nombreImagen     Nombre de la imagen.
     * @return Archivo creado
     */
    public static File crearArchivoImagen(Context context, String nombreSubcarpeta, String nombreImagen) {
        String imageFileName = crearNombreArchivoImagen(context, nombreImagen);

        determinarRutaAlmacenamiento(context);

        File storageDir = new File(rutaAlmacenamiento.getAbsolutePath() + "/" + nombreSubcarpeta);
        storageDir.mkdir();
        return new File(storageDir, imageFileName);
    }

    /**
     * Crea un archivo en la carpeta de imágenes de la aplicación.
     *
     * @param context          Contexto de la aplicación
     * @param nombreSubcarpeta Nombre de la subcarpeta en la cual se desea guardar el archivo. Si no existe es creada.
     * @return Archivo creado
     */
    public static File crearArchivoImagen(Context context, String nombreSubcarpeta) {
        return crearArchivoImagen(context, nombreSubcarpeta, "");
    }

    /**
     * Determina la ruta de almacenamiento de archivos de la aplicación
     *
     * @param context Contexto de la aplicación
     */
    protected static void determinarRutaAlmacenamiento(Context context) {
        if (rutaAlmacenamiento == null) {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                rutaAlmacenamiento = context.getExternalFilesDir(null);
            } else {
                rutaAlmacenamiento = context.getFilesDir();
            }
        }
    }

    /**
     * Crea el nombre del nuevo archivo de Imagen (no incluye la ruta completa)
     *
     * @param context      Contexto de la aplicación
     * @param nombreImagen Nombre de la imagen.
     * @return Nombre del archivo
     */
    public static String crearNombreArchivoImagen(Context context, String nombreImagen) {

        String imageFileName;
        String extension = context.getResources().getString(R.string.extension_archivo_imagen);

        if (nombreImagen.isEmpty()) {
            String comienzoNombre = context.getResources().getString(R.string.nombre_archivo_imagen);
            String timeStampActual = formatoNombreArchivo.format(new Date());
            imageFileName = comienzoNombre + timeStampActual + extension;
        } else {
            imageFileName = nombreImagen + extension;
        }
        return imageFileName;
    }

    /**
     * Crea el nombre del nuevo archivo de Imagen (no incluye la ruta completa)
     *
     * @param context Contexto de la aplicación
     * @return Nombre del archivo
     */
    public static String crearNombreArchivoImagen(Context context) {
        return crearNombreArchivoImagen(context, "");
    }

    /**
     * Obtiene un listado de todos los archivos almacenados dentro de una subcarpeta del almacenamiento de la aplicación
     *
     * @param context          Contexto de la aplicación
     * @param nombreSubcarpeta Nombre de la subcarpeta dentro del almacenamiento de la aplicación. Si es vacío se toma el directorio raíz.
     * @return Listado de archivos. Null si la subcarpeta no existe o está vacía
     */
    public static File[] getArchivosAlmacenados(Context context, String nombreSubcarpeta) {
        determinarRutaAlmacenamiento(context);

        File directorio = new File(rutaAlmacenamiento.getAbsolutePath() + "/" + nombreSubcarpeta);
        File[] archivos = null;
        if (directorio.exists())
            archivos = directorio.listFiles();
        return archivos;
    }

    /**
     * Guarda una imagen en bitmap dentro de un archivo
     *
     * @param bitmap Bitmap a guardar
     * @param file   Archivo a utilizar
     * @throws IOException Excepción lanzada si hay problemas en el acceso del objeto {@link File}
     */
    public static void guardarBitmapEnFile(Bitmap bitmap, File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        if (out != null) {
            out.close();
        }
    }

    /**
     * Copia el contenido de un archivo a otro
     *
     * @param archivoOrigen  Archivo a copiar
     * @param archivoDestino Archivo destino
     * @param context        Contexto de la aplicación
     * @param activity       Actividad desde la cual se llama el método
     * @return True si pudo copiar el archivo, false de lo contrario.
     * @throws IOException Excepción lanzada si hay problemas en el acceso a los objetos {@link File}
     */
    public static boolean copiarArchivo(File archivoOrigen, File archivoDestino, Context context, Activity activity) throws IOException {
        if (!archivoOrigen.exists()) {
            return false;
        }
        if (!SolicitudPermisos.checkPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, SolicitudPermisos.CODIGO_REQUEST_WRITE_STORAGE)) {
            return false;
        }
        FileChannel source = new FileInputStream(archivoOrigen).getChannel();
        FileChannel destination = new FileOutputStream(archivoDestino).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
        return true;
    }

    /**
     * Copia el contenido de un archivo a otro
     *
     * @param uriOrigen      Uri del archivo a copiar
     * @param archivoDestino Archivo destino
     * @param context        Contexto de la aplicación
     * @param activity       Actividad desde la cual se llama el método
     * @throws IOException Excepción lanzada si hay problemas en el acceso a los objetos {@link File}
     */
    public static boolean copiarArchivo(Uri uriOrigen, Context context, Activity activity, File archivoDestino) throws IOException {
        ContentResolver contentResolver = context.getContentResolver();
        File origen = new File(getRealPathFromURI(uriOrigen, contentResolver));
        if (origen != null) {
            return copiarArchivo(origen, archivoDestino, context, activity);
        } else {
            throw new IOException();
        }
    }

    /**
     * Devuelve el path del archivo a partir de la uri
     *
     * @param uri             Uri del archivo
     * @param contentResolver ContentResolver de la aplicación
     * @return Path del archivo
     */
    private static String getRealPathFromURI(Uri uri, ContentResolver contentResolver) {
        String result;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    /**
     * Elimina un archivo de imagen.
     *
     * @param context             Contexto de la aplicación
     * @param nombreSubcarpeta    Nombre de la subcarpeta donde se encuentra el archivo. Si no existe es creada.
     * @param nombreArchivoImagen Nombre del archivo de imagen.
     * @return True si fue borrado, false si no.
     */
    public static boolean eliminarArchivo(Context context, String nombreSubcarpeta, String nombreArchivoImagen) {
        String imageFileName = crearNombreArchivoImagen(context, nombreArchivoImagen);

        determinarRutaAlmacenamiento(context);

        File storageDir = new File(rutaAlmacenamiento.getAbsolutePath() + "/" + nombreSubcarpeta);
        if (storageDir.exists() && storageDir.isDirectory()) {
            File archivo = new File(storageDir, imageFileName);
            if (archivo.exists()) {
                return archivo.delete();
            }
        }
        return false;
    }

    /**
     * Elimina un archivo.
     *
     * @param rutaArchivo Ruta del archivo a borrar.
     * @return True si fue borrado, false si no.
     */
    public static boolean eliminarArchivo(String rutaArchivo) {
        File archivo = new File(rutaArchivo);
        if (archivo.exists()) {
            return archivo.delete();
        }
        return false;
    }

    /**
     * Elimina un archivo.
     *
     * @param uriArchivo Uri del archivo a borrar.
     * @return True si fue borrado, false si no.
     */
    public static boolean eliminarArchivo(URI uriArchivo) {
        File archivo = new File(uriArchivo);
        if (archivo.exists()) {
            return archivo.delete();
        }
        return false;
    }
}