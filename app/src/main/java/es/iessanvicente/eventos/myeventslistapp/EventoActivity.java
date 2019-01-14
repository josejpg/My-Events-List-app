package es.iessanvicente.eventos.myeventslistapp;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class EventoActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Integer idEvent;
    EditText nameEvent;
    EditText address;
    EditText cp;
    Switch isActive;
    EditText date;
    EditText time;
    EditText description;
    Button btnUpdate;
    List<String> listValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        String rutaApp = Environment.getExternalStorageDirectory()+"/Android/Data/es.miseventos.iessanvicente/";
        String rutaDB = rutaApp + "eventosDB";
        db = openOrCreateDatabase(rutaDB, MODE_PRIVATE, null);

        idEvent = getIntent().getExtras().getInt("idEvent" );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton maps = (FloatingActionButton) findViewById(R.id.maps);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        listValues = new ArrayList<>();

        isActive = (Switch) findViewById(R.id.swActivo);
        nameEvent = (EditText) findViewById(R.id.editEvento);
        address = (EditText) findViewById(R.id.editDireccion);
        cp = (EditText) findViewById(R.id.editCP);
        date = (EditText) findViewById(R.id.editFecha);
        time = (EditText) findViewById(R.id.editHora);
        description = (EditText) findViewById(R.id.editDescription);

        date.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                MustraDialogFecha();
            }
        });

        time.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                MustraDialogHora();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                listValues.add( String.valueOf( nameEvent.getText() ) );
                listValues.add( String.valueOf( address.getText() ) );
                listValues.add( String.valueOf( cp.getText() ) );
                listValues.add( String.valueOf( date.getText() ) );
                listValues.add( String.valueOf( time.getText() ) );
                listValues.add( String.valueOf( description.getText() ) );
                updateEvent( checkField( listValues ) );
            }
        });
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

            evento e = new evento(
                    idEvent,
                    String.valueOf( nameEvent.getText() ),
                    String.valueOf( address.getText() ) + ", " + String.valueOf( cp.getText() ),
                    String.valueOf( date.getText() ) + " " + String.valueOf( time.getText() ),
                    String.valueOf( description.getText() ),
                    ( isActive.isChecked() ) ? 1 : 0
            );

            db.execSQL("UPDATE " +
                            "eventos " +
                        "SET nombre='" + e.getNombre() + "', " +
                            "direccion = '" + e.getDireccion() + "', " +
                            "fecha_hora = '" + e.getFecha_hora() + "', " +
                            "descripcion = '" + e.getDescripcion() + "', " +
                            "activo = '" + e.getActivo() + "' " +
                        "WHERE " +
                            "ID = '" + e.getId() + "';");
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
