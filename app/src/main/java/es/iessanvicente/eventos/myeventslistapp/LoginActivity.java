package es.iessanvicente.eventos.myeventslistapp;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{
    //Declaracion de variables
    EditText email;
    EditText password;
    EditText rePassword;
    EditText nombreCompleto;
    Button realizaRegistro;
    Button realizaLogin;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText)findViewById(R.id.tEmail);
        password = (EditText) findViewById(R.id.tPassword);
        rePassword =(EditText) findViewById(R.id.tRePassword);
        nombreCompleto = (EditText) findViewById(R.id.tNombreCompleto);
        realizaLogin = (Button) findViewById(R.id.btLogin);
        realizaRegistro = (Button) findViewById(R.id.btRegister);
        db = openOrCreateDatabase("eventos", MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS usuarios(email VARCHAR, password VARCHAR, nombre VARCHAR);");

        realizaLogin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CompruebaLogin();
            }
        });

        realizaRegistro.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                RealizaRegistro();
            }
        });
    }

    //Funcion para activar el modo registro y así dar de alta el nuevo user en la bd
    private void RealizaRegistro()
    {
        realizaLogin.setVisibility(View.INVISIBLE);
        rePassword.setVisibility(View.VISIBLE);
        nombreCompleto.setVisibility(View.VISIBLE);

        realizaRegistro.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(RealizaInsertUserBD(String.valueOf(email.getText()), String.valueOf(password.getText()), String.valueOf(rePassword.getText()), String.valueOf(nombreCompleto.getText())))
                {
                    realizaLogin.setVisibility(View.VISIBLE);
                    rePassword.setVisibility(View.INVISIBLE);
                    nombreCompleto.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    //Funcion para comprobar que ambos datos se han rellenado y si son válidos entramos al main
    private void CompruebaLogin()
    {
        if (!this.validarEmail(String.valueOf(email.getText())))
        {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Mensaje de ayuda");
            dialogo1.setMessage("No ha escrito una dirección de email válida.");
            dialogo1.show();
        }
        else
        {

            Cursor resultSet = db.rawQuery("Select email, password from usuarios where email ",null);
            resultSet.moveToFirst();
            String username = resultSet.getString(0);
            String password = resultSet.getString(1);
        }
    }

    //Funcion para validar que la dirección de email sea válida
    private boolean validarEmail(String email)
    {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    //Funcion para
    private boolean validarPassword(String password, String Repassword)
    {
        boolean passCorrecto = false;
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        if (password.equals(Repassword))
        {
            if (password.length() >= 8)
            {
                pattern = Pattern.compile(PASSWORD_PATTERN);
                matcher = pattern.matcher(password);

                if (matcher.matches())
                {
                    passCorrecto = true;
                }
            }
        }
        return passCorrecto;
    }

    //Funcion para realizar el insertado en sqlite del usuario
    private boolean RealizaInsertUserBD(String email, String password, String rePassword, String nombreCompleto)
    {
        try
        {
            if (validarEmail(email) && validarPassword(password, rePassword) && !nombreCompleto.isEmpty())
            {
                db.execSQL("INSERT INTO usuarios VALUES(email,password, nombreCompleto);");
                return true;
            }
            else
            {
                return false;
            }
        }
        catch (Exception ex)
        {
            return false;
        }
    }
}

