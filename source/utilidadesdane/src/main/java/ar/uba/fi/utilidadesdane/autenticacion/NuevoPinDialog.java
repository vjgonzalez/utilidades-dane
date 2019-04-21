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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ar.uba.fi.utilidadesdane.R;

/**
 * Cuadro de diálogo para modificar el código de seguridad.
 *
 * @author Virginia González y Alfredo Hodes
 */
public class NuevoPinDialog extends DialogFragment {

    private String primerCodigo = "";

    /**
     * @return ventada de diálogo creada
     * @see android.app.DialogFragment#onCreateDialog(Bundle)
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialogo_autenticacion, null);

        builder.setView(view)
                .setTitle(R.string.dialogo_nuevo_pin_titulo)
                .setPositiveButton(R.string.dialogo_nuevo_pin_ok, null)
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
                            procesarPin();
                            return true;
                        }
                        return false;
                    }
                });

                Button botonAceptar = dialogoAutenticacion.getButton(AlertDialog.BUTTON_POSITIVE);
                botonAceptar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        procesarPin();
                    }
                });

                Button botonCancelar = dialogoAutenticacion.getButton(AlertDialog.BUTTON_NEGATIVE);
                botonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogoAutenticacion.dismiss();
                    }
                });

                pedirPrimerIntento();
            }

            private void pedirPrimerIntento() {
                dialogoAutenticacion.setTitle(R.string.dialogo_nuevo_pin_titulo);
                primerCodigo = "";
                EditText campoPassword = dialogoAutenticacion.findViewById(R.id.password);
                campoPassword.setText("");
                mostrarTeclado();
            }

            private void pedirSegundoIntento() {
                EditText campoPassword = dialogoAutenticacion.findViewById(R.id.password);
                dialogoAutenticacion.setTitle(R.string.dialogo_nuevo_pin_reingreso_titulo);
                campoPassword.setText("");
                mostrarTeclado();
            }


            private void procesarPin() {
                EditText campoPassword = dialogoAutenticacion.findViewById(R.id.password);
                String password = campoPassword.getText().toString();

                if (password.length() != Autenticacion.instancia().getCantidadCaracteresPin()) {
                    mostrarToastErrorFormato();
                } else {
                    // Formato correcto, setear como 1er intento o comparar
                    if (primerCodigo.isEmpty()) {
                        primerCodigo = password;
                        pedirSegundoIntento();
                    } else {
                        if (password.equals(primerCodigo)) {
                            // Nuevo código OK
                            Autenticacion.instancia().setCodigoAdmin(password);
                            dialogoAutenticacion.dismiss();
                        } else {
                            // Codigos distintos. Repetir
                            mostrarToastErrorCodigosDistintos();
                            pedirPrimerIntento();
                        }
                    }
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
     * Muestra un toast con el mensaje que indica que el formato del código es incorrecto
     */
    private void mostrarToastErrorFormato() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_codigo_incorrecto, null);

        TextView text = layout.findViewById(R.id.text);
        text.setText(getResources().getString(R.string.error_formato_pin, Autenticacion.instancia().getCantidadCaracteresPin()));

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    /**
     * Muestra un toast con el mensaje que indica que el código repetido no coincide con el original
     */
    private void mostrarToastErrorCodigosDistintos() {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_codigo_incorrecto, null);

        TextView text = layout.findViewById(R.id.text);
        text.setText(getResources().getString(R.string.error_nuevo_pin_reingreso));

        Toast toast = new Toast(getActivity().getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
