package es.iessanvicente.eventos.myeventslistapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //Declaracion de variables
    ListView listadoEventos;
    ArrayList<String> lDatosEventos;
    SQLiteDatabase db;
    String rutaApp;
    String rutaDB;
   // AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rutaApp = Environment.getExternalStorageDirectory()+"/Android/Data/es.miseventos.iessanvicente/";
        rutaDB = rutaApp + "eventosDB";
        listadoEventos = (ListView) findViewById(R.id.lvEventos);
        lDatosEventos = new ArrayList<String>();
        GeneraListaCitas();
    }

    private void GeneraListaCitas()
    {
        try
        {
            db = openOrCreateDatabase(rutaDB, MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS eventos(ID INTEGER PRIMARY KEY AUTOINCREMENT, nombre VARCHAR, direccion VARCHAR, fecha_hora VARCHAR, descripcion VARCHAR, activo INT);");
            Cursor fila = db.rawQuery("SELECT id, nombre, direccion, fecha_hora, descripcion, activo FROM eventos", null);
            if (fila.moveToFirst())
            {
                do
                {
                    evento e = new evento(fila.getInt(0),fila.getString(1), fila.getString(2), fila.getString(3), fila.getString(4), fila.getInt(5));
                    lDatosEventos.add(e.DatosEvento());
                }
                while (fila.moveToNext());
            }
            db.close();

            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lDatosEventos);
            listadoEventos.setAdapter(adapter);
            listadoEventos.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                public void onItemClick(AdapterView<?> a, View v, int position, long id)
                {
                    //String prueba = (String) a.getAdapter().getItem(position);
                }
            });
        }
        catch (Exception ex)
        {
            String exc = ex.getMessage();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itemNuevaCita)
        {
            Intent NuevaCita = new Intent(getApplicationContext(), NuevaCitaActivity.class);
            startActivity(NuevaCita);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
