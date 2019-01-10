package es.iessanvicente.eventos.myeventslistapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.util.Log;
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
    String[] permissions;
    String rutaApp;
    String rutaDB;
    Integer activaRegistro;
    SharedPreferences shared;
    Boolean isLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        permissions = new String[]
        {
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.VIBRATE,
                Manifest.permission.RECORD_AUDIO,
        };

        checkPermissions();

        email = (EditText)findViewById(R.id.tEmail);
        password = (EditText) findViewById(R.id.tPassword);
        rePassword =(EditText) findViewById(R.id.tRePassword);
        nombreCompleto = (EditText) findViewById(R.id.tNombreCompleto);
        realizaLogin = (Button) findViewById(R.id.btLogin);
        realizaRegistro = (Button) findViewById(R.id.btRegister);
        activaRegistro = 0;
        rutaApp = Environment.getExternalStorageDirectory()+"/Android/Data/es.miseventos.iessanvicente/";
        rutaDB = rutaApp + "eventosDB";

        shared = PreferenceManager.getDefaultSharedPreferences(this);
        isLogged = shared.getBoolean( "isLogged", false );

        if (isLogged)
        {
            Intent MenuPrincipal = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(MenuPrincipal);
        }
        else
        {
            db = openOrCreateDatabase(rutaDB, MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS usuarios(email VARCHAR, password VARCHAR, nombre VARCHAR);");

            realizaLogin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CompruebaLogin();
                }
            });

            realizaRegistro.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (activaRegistro == 0) {
                        realizaLogin.setVisibility(View.INVISIBLE);
                        rePassword.setVisibility(View.VISIBLE);
                        nombreCompleto.setVisibility(View.VISIBLE);
                        activaRegistro = 1;
                    } else {
                        RealizaRegistro();
                    }
                }
            });
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (isLogged)
        {
            Intent MenuPrincipal = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(MenuPrincipal);
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            }
            return;
        }
    }


    //Funcion para activar el modo registro y así dar de alta el nuevo user en la bd
    private void RealizaRegistro()
    {
        try
        {
            if (RealizaInsertUserBD(String.valueOf(email.getText()), String.valueOf(password.getText()), String.valueOf(rePassword.getText()), String.valueOf(nombreCompleto.getText())))
            {
                realizaLogin.setVisibility(View.VISIBLE);
                rePassword.setVisibility(View.INVISIBLE);
                nombreCompleto.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception ex)
        {
            String e = ex.getMessage();
        }
    }

    //Funcion para comprobar que ambos datos se han rellenado y si son válidos entramos al main
    private void CompruebaLogin()
    {
        if (!this.validarEmail(String.valueOf(email.getText())) && !this.validarPassword(String.valueOf(password.getText()), String.valueOf(rePassword.getText())))
        {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Mensaje de ayuda");
            dialogo1.setMessage("No ha escrito una dirección de email válida o bien ha escrito mal la contraseña.");
            dialogo1.show();
        }
        else
        {

            Cursor resultSet = db.rawQuery("Select email, password from usuarios where email = '"+String.valueOf(email.getText())+"'",null);
            resultSet.moveToFirst();
            String nombreUsuarioBD = resultSet.getString(0);
            String password = resultSet.getString(1);

            if (String.valueOf(email.getText()).equals(nombreUsuarioBD) && String.valueOf(email.getText()).equals(nombreUsuarioBD))
            {
                SharedPreferences.Editor editor = shared.edit();
                editor.putBoolean("isLogged", true);
                editor.putString("email", nombreUsuarioBD);
                editor.commit();
                Intent MenuPrincipal = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MenuPrincipal);
            }
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

        if (password.equals(Repassword))
        {
            if (password.length() >= 8)
            {
                passCorrecto = true;
            }
        }
        return passCorrecto;
    }

    //Funcion para realizar el insertado en sqlite del usuario
    private boolean RealizaInsertUserBD(String email, String password, String rePassword, String nombreCompleto)
    {
        boolean insertOk = false;
        try
        {
            if (validarEmail(email) && validarPassword(password, rePassword) && !nombreCompleto.isEmpty())
            {
                db.execSQL("INSERT INTO usuarios VALUES('"+email+"','"+password+"','"+nombreCompleto+"');");
                insertOk = true;
                CompruebaLogin();
            }
            else
            {
                insertOk = false;
            }
        }
        catch (Exception ex)
        {
            insertOk = false;
        }
        return insertOk;
    }
}

