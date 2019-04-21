package ar.uba.fi.utilidadesdane.autenticacion;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ar.uba.fi.utilidadesdane.R;

/**
 * Cuadro de diálogo para autenticarse al ejecutar acciones protegidas con un código de seguridad.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class AutenticacionDialog extends DialogFragment {

    private String mensajeCodigoIncorrecto = "CÓDIGO INCORRECTO";

    /**
     * @return ventana de diálogo creada
     * @see android.app.DialogFragment#onCreateDialog(Bundle)
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialogo_autenticacion, null);

        builder.setView(view)
                .setTitle(R.string.dialogo_autenticar_titulo)
                .setPositiveButton(R.string.dialogo_autenticar_ok, null)
                .setNegativeButton(R.string.dialogo_cancelar, null)
                .create();

        final AlertDialog dialogoAutenticacion = builder.create();

        dialogoAutenticacion.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {
                mostrarTeclado();

                (view.findViewById(R.id.password)).setOnKeyListener(new View.OnKeyListener() {
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            autenticar();
                            return true;
                        }
                        return false;
                    }
                });

                dialogoAutenticacion.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        autenticar();
                    }

                });

                dialogoAutenticacion.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Autenticacion.instancia().resultadoAutenticacionError();
                        dialogoAutenticacion.dismiss();
                    }
                });
            }


            private void autenticar() {
                EditText passwordField = dialogoAutenticacion.findViewById(R.id.password);
                String password = passwordField.getText().toString();

                if (Autenticacion.instancia().esCodigoValido(password)) {
                    Autenticacion.instancia().resultadoAutenticacionOk();
                    dialogoAutenticacion.dismiss();
                } else {
                    mostrarToastErrorCodigoIncorrecto();
                    passwordField.setText("");
                    mostrarTeclado();
                }
            }

            private void mostrarTeclado() {
                EditText textoPwd = view.findViewById(R.id.password);
                textoPwd.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(textoPwd, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        return dialogoAutenticacion;
    }

    /**
     * Modifica el mensaje mostrado al ingresar un código incorrecto
     * @param mensajeCodigoIncorrecto mensaje a mostrar al ingresar un código incorrecto
     */
    public void setMensajeCodigoIncorrecto(String mensajeCodigoIncorrecto) {
        this.mensajeCodigoIncorrecto = mensajeCodigoIncorrecto;
    }

    /**
     * Muestra un toast con el mensaje que indica que el código ingresado es incorrecto
     */
    private void mostrarToastErrorCodigoIncorrecto() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_codigo_incorrecto, null);

        TextView text = layout.findViewById(R.id.text);
        text.setText(mensajeCodigoIncorrecto);

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
