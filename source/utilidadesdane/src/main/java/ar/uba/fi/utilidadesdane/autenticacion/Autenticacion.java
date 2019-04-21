package ar.uba.fi.utilidadesdane.autenticacion;

import android.app.FragmentManager;
import android.content.SharedPreferences;

import ar.uba.fi.utilidadesdane.app.DaneApplication;

/**
 * Clase estática que maneja la protección de ciertas acciones a través de un código de seguridad.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class Autenticacion {

    private static Autenticacion instancia = null;

    private static final String AUTENTICACION_PREFS = "AutenticacionPrefs";
    private static final String CODIGO_ADMIN_KEY = "codigo_admin";
    private static final String TAG_DIALOGO_AUTENTICACION = "AutenticacionDialog";
    private static final int CODIGO_MAGICO = 3263; //"DANE" en teclado telefónico

    private int cantidadCaracteresPin = 4;
    private int codigoAdmin = CODIGO_MAGICO;

    private boolean esAdmin = false;
    private boolean autenticacionPermanente = false;

    private ComandoAdministrador ejecutarSiAdminAutenticacionActual;
    private ComandoAdministrador ejecutarSiNoAdminAutenticacionActual;

    private String mensajeCodigoIncorrecto = null;

    /**
     * obtiene la instancia del singleton Autenticacion
     * @return
     */
    public static Autenticacion instancia() {
        if (instancia == null)
            instancia = new Autenticacion();
        return instancia;
    }

    /**
     * constructor
     */
    private Autenticacion() {
        // Se verifica si ya existe un código personalizado
        if (DaneApplication.obtenerSharedPreferences(AUTENTICACION_PREFS).contains(CODIGO_ADMIN_KEY))
            setCodigoAdmin(DaneApplication.obtenerSharedPreferences(AUTENTICACION_PREFS).getInt(CODIGO_ADMIN_KEY, CODIGO_MAGICO));
    }

    /**
     * Indica si el usuario está autenticado como administrador
     *
     * @return True si el usuario está autenticado, false de lo contrario
     */
    public boolean esAdmin() {
        return esAdmin;
    }

    /**
     * Establece un nuevo código de autenticación, reemplazando al anterior.
     * El código default nunca es sobreescrito, quedando siempre como alternativa de autenticación.
     *
     * @param nuevoCodigo Nuevo código a establecer
     */
    public void setCodigoAdmin(String nuevoCodigo) {
        if (nuevoCodigo.isEmpty())
            return;
        this.setCodigoAdmin(Integer.parseInt(nuevoCodigo));
    }

    /**
     * Establece un nuevo código de autenticación, reemplazando al anterior.
     * El código default nunca es sobreescrito, quedando siempre como alternativa de autenticación.
     *
     * @param nuevoCodigo Nuevo código a establecer
     */
    private void setCodigoAdmin(int nuevoCodigo) {
        this.codigoAdmin = nuevoCodigo;

        SharedPreferences autenticacionPrefs = DaneApplication.obtenerSharedPreferences(AUTENTICACION_PREFS);
        SharedPreferences.Editor autenticacionPrefsEditor = autenticacionPrefs.edit();
        autenticacionPrefsEditor.putInt(CODIGO_ADMIN_KEY, nuevoCodigo);
        autenticacionPrefsEditor.apply();
    }

    /**
     * Ejecuta comando en modo administrador, mostrando pantalla de ingreso de PIN si no se está logueado. No hace nada en caso de no ser admin y no ingresar PIN correcto.
     * @see #autenticarYEjecutar(ComandoAdministrador, ComandoAdministrador, FragmentManager)
     */
    public void autenticarYEjecutar(ComandoAdministrador ejecutarSiAdmin, FragmentManager fragmentManager) {
        autenticarYEjecutar(ejecutarSiAdmin, null, fragmentManager);
    }

    /**
     * Ejecuta comando en modo administrador, mostrando pantalla de ingreso de PIN si no se está logueado.Ejecuta otro comando en caso de no ser admin y no ingresar PIN correcto.
     * @param ejecutarSiAdmin comando a ejecutar si está logueado como admin o ingresa el PIN correcto
     * @param ejecutarSiNoAdmin comando a ejecutar si no está logueado como admin y no ingresa correctamente el PIN
     * @param fragmentManager donde se muesta el diálogo de ingreso de PIN si no se está logueado como admin inicialmente.
     */
    public void autenticarYEjecutar(ComandoAdministrador ejecutarSiAdmin, ComandoAdministrador ejecutarSiNoAdmin, FragmentManager fragmentManager) {
        ejecutarSiAdminAutenticacionActual = ejecutarSiAdmin;
        ejecutarSiNoAdminAutenticacionActual = ejecutarSiNoAdmin;
        autenticacionPermanente = false;

        if (esAdmin()) {
            if (ejecutarSiAdminAutenticacionActual != null)
                ejecutarSiAdminAutenticacionActual.ejecutar();
            ejecutarSiAdminAutenticacionActual = ejecutarSiNoAdminAutenticacionActual = null;
        } else {
            AutenticacionDialog autenticacionDialog = new AutenticacionDialog();
            if (mensajeCodigoIncorrecto != null)
                autenticacionDialog.setMensajeCodigoIncorrecto(mensajeCodigoIncorrecto);
            autenticacionDialog.show(fragmentManager, TAG_DIALOGO_AUTENTICACION);
        }
    }

    /**
     * Muestra el diálogo para ingresar PIN y deja logueado al usuario como admin hasta que se llame a {@link #setEsAdmin(boolean)}
     * @param fragmentManager donde se muesta el diálogo de ingreso de PIN si no se está logueado como admin inicialmente.
     */
    public void autenticar(FragmentManager fragmentManager) {
        if (!esAdmin()) {
            autenticacionPermanente = true;
            ejecutarSiAdminAutenticacionActual = ejecutarSiNoAdminAutenticacionActual = null;
            AutenticacionDialog autDialog = new AutenticacionDialog();
            if (mensajeCodigoIncorrecto != null)
                autDialog.setMensajeCodigoIncorrecto(mensajeCodigoIncorrecto);
            autDialog.show(fragmentManager, TAG_DIALOGO_AUTENTICACION);
        }
    }

    /**
     * Indica si el código consultado es válido para autenticar como admin
     * @param codigo PIN a validar (formato numérico)
     * @return true si es válido, false en caso contrario
     */
    private boolean esCodigoValido(int codigo) {
        return (codigo == CODIGO_MAGICO || codigo == codigoAdmin);
    }

    /**
     * Indica si el código consultado es válido para autenticar como admin
     * @param codigo PIN a validar (formato texto)
     * @return true si es válido, false en caso contrario
     */
    protected boolean esCodigoValido(String codigo) {
        if (codigo.isEmpty()) return false;
        return esCodigoValido(Integer.parseInt(codigo));
    }

    /**
     * Otorga/quita el rol de admin a la sesión actual
     * @param esAdmin Otorga rol de admin si es true, lo quita en caso contrario.
     */
    public void setEsAdmin(boolean esAdmin) {
        this.esAdmin = esAdmin;
    }

    /**
     * método llamado al pasar satisfactoriamente la autenticación
     */
    protected void resultadoAutenticacionOk() {
        if (autenticacionPermanente) setEsAdmin(true);
        if (ejecutarSiAdminAutenticacionActual != null)
            ejecutarSiAdminAutenticacionActual.ejecutar();
        ejecutarSiAdminAutenticacionActual = ejecutarSiNoAdminAutenticacionActual = null;
    }

    /**
     * método llamado al cerrar el diálogo de autenticación sin haber ingresado el código correcto
     */
    protected void resultadoAutenticacionError() {
        setEsAdmin(false);
        if (ejecutarSiNoAdminAutenticacionActual != null)
            ejecutarSiNoAdminAutenticacionActual.ejecutar();
        ejecutarSiAdminAutenticacionActual = ejecutarSiNoAdminAutenticacionActual = null;
    }

    /**
     * Solicita en una nueva ventana el código de autenticación
     * @param fragmentManager Fragment manager
     */
    public void solicitarNuevoCodigoAdmin(final FragmentManager fragmentManager) {
        setEsAdmin(false);
        ComandoAdministrador comandoMostrarDialogoSetearCodigo = new ComandoAdministrador() {
            @Override
            public void ejecutar() {
                mostrarDialogoSetearCodigo(fragmentManager);
            }
        };
        autenticarYEjecutar(comandoMostrarDialogoSetearCodigo, fragmentManager);
    }

    /**
     * Muestra la ventana de diálogo para establecer un nuevo código de autenticación
     * @param fragmentManager Fragment manager
     */
    private void mostrarDialogoSetearCodigo(FragmentManager fragmentManager) {
        NuevoPinDialog nuevoPinDialog = new NuevoPinDialog();
        nuevoPinDialog.show(fragmentManager, TAG_DIALOGO_AUTENTICACION);
    }

    /**
     * Modifica el mensaje mostrado al ingresar un código incorrecto
     * @param mensajeCodigoIncorrecto mensaje a mostrar al ingresar un código incorrecto
     */
    public void setMensajeDeCodigoIncorrecto(String mensajeCodigoIncorrecto) {
        this.mensajeCodigoIncorrecto = mensajeCodigoIncorrecto;
    }

    /**
     * Modifica la cantidad de caracteres utilizados para el código de seguridad
     * @param cantidadCaracteresPin cantidad de caracteres utilizados para el código de seguridad
     */
    public void setCantidadCaracteresPin(int cantidadCaracteresPin) {
        this.cantidadCaracteresPin = cantidadCaracteresPin;
    }

    /**
     * Devuelve la cantidad de caracteres utilizados para el código de seguridad
     * @return cantidad de caracteres utilizados para el código de seguridad
     */
    public int getCantidadCaracteresPin() {
        return cantidadCaracteresPin;
    }
}
