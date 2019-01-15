package es.iessanvicente.eventos.myeventslistapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.MediaStore;

import java.nio.charset.StandardCharsets;

import static es.iessanvicente.eventos.myeventslistapp.utils.*;

public class PerfilActivity extends AppCompatActivity {

    // Variables
    static final int REQUEST_IMAGE_CAPTURE = 1;
    User user;
    ImageView btnEdit;
    ImageView avatar;
    EditText email;
    EditText psw;
    EditText name;
    EditText phone;
    TextView txtName;
    SQLiteDatabase db;
    String rutaApp;
    String rutaDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getInfoUser();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveDataUser();
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePicture();
            }
        });

    }

    @Override
    public void onResume()
    {
        super.onResume();

        getInfoUser();
    }

    private void getInfoUser(){
        // DB Connection
        rutaApp = Environment.getExternalStorageDirectory()+"/Android/Data/es.miseventos.iessanvicente/";
        rutaDB = rutaApp + "eventosDB";
        db = openOrCreateDatabase(rutaDB, MODE_PRIVATE, null);

        String sql = "SELECT ID, avatar, email, password, name, phone FROM usuarios";
        Cursor resultSet = db.rawQuery(sql, null);
        resultSet.moveToFirst();
        user = new User(
                resultSet.getInt(0),
                resultSet.getString(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getString(4),
                resultSet.getString(5)
        );
        db.close();
        // Data
        btnEdit = (ImageView) findViewById(R.id.edit);
        avatar = (ImageView) findViewById(R.id.profile);
        email = (EditText) findViewById(R.id.editTextEmail);
        psw = (EditText) findViewById(R.id.editTextPws);
        name = (EditText) findViewById(R.id.editTextName);
        phone = (EditText) findViewById(R.id.editTextPhone);
        txtName = (TextView) findViewById(R.id.name);

        if( !user.toString().equals( "" ) ) {
            email.setText(user.getEmail());
            name.setText(user.getName());
            txtName.setText(user.getName());
            phone.setText(user.getPhone());

            if (user.getAvatar() != null && !user.getAvatar().equals("")) {
                avatar.setImageBitmap(Base642Bitmap(user.getAvatar()));
            }
        }
    }

    private void takePicture() {
        Intent takePictureIntent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get( "data" );
            avatar.setImageBitmap( imageBitmap );
            user.setAvatar( Bitmap2Base64( imageBitmap ) );
        }
    }

    private boolean saveDataUser(){
        boolean result = false;
        user.setEmail( String.valueOf( email.getText() ) );
        user.setName( String.valueOf( name.getText() ) );
        user.setPhone( String.valueOf( phone.getText() ) );

        try {
            db.execSQL("UPDATE usuarios SET email = '" + user.getEmail() + "', name = '" + user.getName() + "', phone = '" + user.getPhone() + "'");
            if (!String.valueOf(psw.getText()).equals("")) {
                byte[] data = String.valueOf(psw.getText()).getBytes(StandardCharsets.UTF_8);
                user.setPsw(Base64.encodeToString(data, Base64.DEFAULT));
                db.execSQL("UPDATE usuarios SET password = '" + user.getPsw() + "'");
            }
            if ( user.getAvatar() != null && !user.getAvatar().equals( "" )) {
                db.execSQL("UPDATE usuarios SET avatar = '" + user.getAvatar() + "'");
            }
            result = true;
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("¡Enhorabuena!");
            dialogo1.setMessage("El usuario se ha actualizado correctamente.");
            dialogo1.show();
            getInfoUser();

        }catch(Exception e){
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("¡Error!");
            dialogo1.setMessage("Ha ocurrido un error y no se han podido actualziar los datos.");
            dialogo1.show();
            e.printStackTrace();
        }
        return result;
    }
}
