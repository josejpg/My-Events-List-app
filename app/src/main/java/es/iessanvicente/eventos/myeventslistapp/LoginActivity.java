package es.iessanvicente.eventos.myeventslistapp;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteException;
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
import android.util.Base64;
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

import org.w3c.dom.Text;

import java.io.File;
import java.nio.charset.StandardCharsets;
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
    TextView muestraLabelRePass;
    TextView muestraLabelNombre;
    Button realizaRegistro;
    Button realizaLogin;
    Button cancelaRegistro;
    SQLiteDatabase db;
    String[] permissions;
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
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        checkPermissions();

        email = (EditText)findViewById(R.id.tEmail);
        password = (EditText) findViewById(R.id.tPassword);
        muestraLabelRePass = (TextView) findViewById(R.id.tvRepass);
        muestraLabelNombre = (TextView) findViewById(R.id.tvNombreApell);
        rePassword =(EditText) findViewById(R.id.tRePassword);
        nombreCompleto = (EditText) findViewById(R.id.tNombreCompleto);
        realizaLogin = (Button) findViewById(R.id.btLogin);
        realizaRegistro = (Button) findViewById(R.id.btRegister);
        cancelaRegistro = (Button) findViewById(R.id.btCancelarRegistro);
        activaRegistro = 0;

        shared = PreferenceManager.getDefaultSharedPreferences(this);
        isLogged = shared.getBoolean( "isLogged", false );

        if (isLogged)
        {
            Intent MenuPrincipal = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(MenuPrincipal);
            this.finish();
        }
        else
        {
            String rutaApp = Environment.getExternalStorageDirectory()+"/Android/Data/es.miseventos.iessanvicente/";
            String rutaDB = rutaApp + "eventosDB";
            try {
                System.out.println( "RUTA => " + rutaApp );
                System.out.println( "RUTA => ()" +  new File( rutaApp ).delete() );
                new File( rutaApp ).delete();
                db = openOrCreateDatabase( rutaApp, MODE_PRIVATE,null );
                db.execSQL("CREATE TABLE IF NOT EXISTS usuarios(ID INTEGER PRIMARY KEY AUTOINCREMENT, email VARCHAR, password VARCHAR, name VARCHAR, phone VARCHAR, avatar TEXT);");

                realizaLogin.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CompruebaLogin();
                    }
                });

                realizaRegistro.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (activaRegistro == 0) {
                            muestraLabelRePass.setVisibility(View.VISIBLE);
                            muestraLabelNombre.setVisibility(View.VISIBLE);
                            cancelaRegistro.setVisibility(View.VISIBLE);
                            realizaLogin.setVisibility(View.INVISIBLE);
                            rePassword.setVisibility(View.VISIBLE);
                            nombreCompleto.setVisibility(View.VISIBLE);
                            activaRegistro = 1;
                        } else {
                            RealizaRegistro();
                        }
                    }
                });

                cancelaRegistro.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        muestraLabelRePass.setVisibility(View.INVISIBLE);
                        muestraLabelNombre.setVisibility(View.INVISIBLE);
                        cancelaRegistro.setVisibility(View.INVISIBLE);
                        realizaLogin.setVisibility(View.VISIBLE);
                        rePassword.setVisibility(View.INVISIBLE);
                        nombreCompleto.setVisibility(View.INVISIBLE);
                        activaRegistro = 0;
                    }
                });
            }catch(SQLiteCantOpenDatabaseException e){
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Error al conectar con la DB");
                dialogo1.setMessage( e.getMessage() );
                dialogo1.show();
                e.printStackTrace();
                new File( rutaDB ).delete();
            }
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
                muestraLabelRePass.setVisibility(View.INVISIBLE);
                muestraLabelNombre.setVisibility(View.INVISIBLE);
                cancelaRegistro.setVisibility(View.INVISIBLE);
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
            dialogo1.setMessage("Los datos introducidos son incorrectos.");
            dialogo1.show();
        }
        else
        {
            byte[] data = String.valueOf(password.getText()).getBytes(StandardCharsets.UTF_8);
            Cursor resultSet = db.rawQuery("SELECT ID, avatar, email, password, name, phone FROM usuarios WHERE email = '"+String.valueOf(email.getText())+"' AND password = '" + Base64.encodeToString(data, Base64.DEFAULT) + "'",null);resultSet.moveToFirst();
            if(resultSet.getCount() > 0) {
                resultSet.moveToFirst();
                User user = new User(
                        resultSet.getInt(0),
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                );

                SharedPreferences.Editor editor = shared.edit();
                editor.putBoolean("isLogged", true);
                editor.putString("email", user.getEmail());
                editor.commit();
                db.close();
                Intent MenuPrincipal = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(MenuPrincipal);
                this.finish();
            }else{
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Mensaje de ayuda");
                dialogo1.setMessage("El usuario y/o contraseña sonincorrectos.");
                dialogo1.show();
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
            if(!validarEmail( email )){
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Email no válido");
                dialogo1.setMessage("El email " + email + " no es válido.");
                dialogo1.show();
            }else if(!validarPassword(password, rePassword)){
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Las contraseñas no son válidas");
                dialogo1.setMessage("Las contraseñas no coinciden o son inferior a 8 carácteres.");
                dialogo1.show();
            }else if(nombreCompleto.isEmpty()){
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
                dialogo1.setTitle("Nombre no válido");
                dialogo1.setMessage("El está vacío.");
                dialogo1.show();
            }
            else
            {
                byte[] data = password.getBytes(StandardCharsets.UTF_8);
                db.execSQL("INSERT INTO usuarios (email, password, name) VALUES('"+email+"','"+Base64.encodeToString(data, Base64.DEFAULT)+"','"+nombreCompleto+"');");
                insertOk = true;
                CompruebaLogin();
            }
        }
        catch (Exception ex)
        {
            insertOk = false;
            String exce = ex.getMessage();
        }
        return insertOk;
    }
}

