package es.iessanvicente.eventos.myeventslistapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class NuevaCitaActivity extends AppCompatActivity {

    EditText nombreEvento;
    EditText lugarEvento;
    CheckBox activoEvento;
    EditText fechaEvento;
    EditText horaEvento;
    EditText descripcionEvento;
    Button CrearEvento;
    List<String> lvaloresString;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_cita);

        nombreEvento = (EditText) findViewById(R.id.etNombreEvento);
        lugarEvento = (EditText) findViewById(R.id.etDireccion);
        activoEvento = (CheckBox) findViewById(R.id.cbActivoEvento);
        fechaEvento = (EditText) findViewById(R.id.etFecha);
        horaEvento = (EditText) findViewById(R.id.etHora);
        descripcionEvento = (EditText) findViewById(R.id.etDescription);
        CrearEvento = (Button) findViewById(R.id.btCrearEvento);

        lvaloresString = new ArrayList<>();


        fechaEvento.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                MustraDialogFecha();
            }
        });

        horaEvento.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                MustraDialogHora();
            }
        });

        CrearEvento.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                lvaloresString.add(String.valueOf(nombreEvento.getText()));
                lvaloresString.add(String.valueOf(lugarEvento.getText()));
                lvaloresString.add(String.valueOf(fechaEvento.getText()));
                lvaloresString.add(String.valueOf(horaEvento.getText()));
                lvaloresString.add(String.valueOf(descripcionEvento.getText()));
                GeneraEvento(compruebaCampos(lvaloresString));
            }
        });
    }

    private void MustraDialogFecha()
    {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void MustraDialogHora()
    {
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"TimePicker");
    }

    private boolean compruebaCampos(List<String> lCamposEvento)
    {
        for (String valor: lCamposEvento)
        {
            if (valor.isEmpty())
            {
                return false;
            }
        }
        return true;
    }

    private void GeneraEvento(boolean EventoOK)
    {
        if (EventoOK)
        {
            Integer activoEvent = 0;
            if (activoEvento.isChecked())
            {
                activoEvent = 1;
            }

            evento e = new evento(lvaloresString.get(0),lvaloresString.get(1), lvaloresString.get(2)+" "+lvaloresString.get(3),lvaloresString.get(4),activoEvent);

            String rutaApp = Environment.getExternalStorageDirectory()+"/Android/Data/es.miseventos.iessanvicente/";
            String rutaDB = rutaApp + "eventosDB";
            SQLiteDatabase db = openOrCreateDatabase(rutaDB, MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS eventos(ID INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR, direccion VARCHAR, fecha_hora VARCHAR, descripcion VARCHAR, activo INT);");
            db.execSQL("INSERT INTO eventos (nombre, direccion, fecha_hora, descripcion, activo) VALUES('"+e.getNombre()+"','"+e.getDireccion()+"', '"+e.getFecha_hora()+"','"+e.getDescripcion()+"','"+e.getActivo()+"');");
            db.close();
            this.finish();
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
