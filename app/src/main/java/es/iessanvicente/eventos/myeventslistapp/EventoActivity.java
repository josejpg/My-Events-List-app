package es.iessanvicente.eventos.myeventslistapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Jose Manuel Esparcia Cañizares
 * Jose J. Pardines Garcia
 */


public class EventoActivity extends AppCompatActivity {

    SQLiteDatabase db;
    evento datosEvento;
    Integer idEvent;
    EditText nameEvent;
    EditText address;
    Switch isActive;
    EditText date;
    EditText time;
    EditText description;
    Button btnUpdate;
    List<String> listValues;
    boolean isGPS;
    SharedPreferences preferenciasGPS;
    AlertDialog.Builder dialogo1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);
        preferenciasGPS = PreferenceManager.getDefaultSharedPreferences(this);
        dialogo1 = new AlertDialog.Builder(this);
        try {
            idEvent = getIntent().getExtras().getInt("idEvent");

            String rutaApp = Environment.getExternalStorageDirectory() + "/Android/Data/es.miseventos.iessanvicente/";
            String rutaDB = rutaApp + "eventosDB";
            db = openOrCreateDatabase(rutaDB, MODE_PRIVATE, null);
            Cursor row = db.rawQuery("SELECT ID, nombre, direccion, fecha_hora, descripcion, activo FROM eventos WHERE ID = '" + idEvent + "'", null);
            row.moveToFirst();
            datosEvento = new evento(
                    row.getInt( 0 ),
                    row.getString( 1 ),
                    row.getString( 2 ),
                    row.getString( 3 ),
                    row.getString( 4 ),
                    row.getInt( 5 )
            );
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            FloatingActionButton maps = (FloatingActionButton) findViewById(R.id.maps);
            maps.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isGPS = preferenciasGPS.getBoolean( "activeGPS", false );

                    if(isGPS)
                    {
                        Intent Map = new Intent(getApplicationContext(), MapsActivity.class);
                        Map.putExtra("parametro", idEvent.toString());
                        startActivity(Map);
                    }
                    else{
                        dialogo1.setTitle( "Error con GPS" );
                        dialogo1.setMessage( "El GPS está desactivado en las preferencias, por favor activelo y vuelva a intentarlo." );
                        dialogo1.show();
                    }
                }
            });

            listValues = new ArrayList<>();

            isActive = (Switch) findViewById(R.id.swActivo);
            nameEvent = (EditText) findViewById(R.id.editEvento);
            address = (EditText) findViewById(R.id.editDireccion);
            date = (EditText) findViewById(R.id.etFecha);
            time = (EditText) findViewById(R.id.etHora);
            description = (EditText) findViewById(R.id.editDescription);
            btnUpdate = (Button) findViewById(R.id.btnActualiza);

            nameEvent.setText( datosEvento.getNombre() );
            address.setText( datosEvento.getDireccion() );
            description.setText( datosEvento.getDescripcion() );
            isActive.setChecked( ( datosEvento.getActivo() == 1 ) ? true : false );
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "dd/MM/yyyy HH:mm" );
            Date dateTime = simpleDateFormat.parse( datosEvento.getFecha_hora() );
            android.text.format.DateFormat dateFormat = new android.text.format.DateFormat();
            System.out.println( "DATETIME => " + datosEvento.getFecha_hora() + " (DB) => " + dateTime.toString() );

            date.setText( dateFormat.format( "dd/MM/yyyy", dateTime ) );
            time.setText( dateFormat.format( "HH:mm", dateTime ) );

            date.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MustraDialogFecha();
                }
            });

            time.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    MustraDialogHora();
                }
            });

            btnUpdate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    listValues.add( String.valueOf( nameEvent.getText() ) );
                    listValues.add( String.valueOf( address.getText() ) );
                    listValues.add( String.valueOf( date.getText() ) );
                    listValues.add( String.valueOf( time.getText() ) );
                    listValues.add( String.valueOf( description.getText() ) );
                    updateEvent( checkField( listValues ) );
                }
            });
        }catch ( Exception e ){
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Error");
            dialogo1.setMessage( e.getMessage() );
            dialogo1.show();
            e.printStackTrace();
        }
    }

    private void MustraDialogFecha(){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show( getSupportFragmentManager(), "datePicker" );
    }

    private void MustraDialogHora(){
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show( getFragmentManager(),"TimePicker" );
    }

    private boolean checkField( List<String> listValues ){
        for (String value: listValues) {
            if ( value.isEmpty() ) {
                return false;
            }
        }
        return true;
    }

    private void updateEvent(boolean update)
    {
        if ( update ) {

            datosEvento.setActivo( ( isActive.isChecked() ) ? 1 : 0 );
            datosEvento.setNombre( String.valueOf( nameEvent.getText() ) );
            datosEvento.setDireccion( String.valueOf( address.getText() ) );
            datosEvento.setFecha_hora( String.valueOf( date.getText() ) + " " + String.valueOf( time.getText() ) );
            datosEvento.setDescripcion( String.valueOf( description.getText() ) );

            db.execSQL("UPDATE " +
                            "eventos " +
                        "SET nombre='" + datosEvento.getNombre() + "', " +
                            "direccion = '" + datosEvento.getDireccion() + "', " +
                            "fecha_hora = '" + datosEvento.getFecha_hora() + "', " +
                            "descripcion = '" + datosEvento.getDescripcion() + "', " +
                            "activo = '" + datosEvento.getActivo() + "' " +
                        "WHERE " +
                            "ID = '" + idEvent + "';");
            db.close();
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle( "¡Enhorabuena!" );
            dialogo1.setMessage( "Los datos del evento se han actualizado correctamente." );
            dialogo1.show();
        }
        else
        {
            AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
            dialogo1.setTitle("Mensaje de ayuda");
            dialogo1.setMessage("Todos los campos son obligatorios.");
            dialogo1.show();
        }
    }
}
