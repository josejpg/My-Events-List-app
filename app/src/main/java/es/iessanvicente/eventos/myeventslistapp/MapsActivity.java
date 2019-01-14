package es.iessanvicente.eventos.myeventslistapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String idEvento;
    private evento e;
    String[] permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        idEvento = getIntent().getExtras().getString("parametro");
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

    private void ObtenEvento(String eventoID) {
        if (!eventoID.isEmpty()) {
            try {
                String rutaApp = Environment.getExternalStorageDirectory() + "/Android/Data/es.miseventos.iessanvicente/";
                String rutaDB = rutaApp + "eventosDB";
                SQLiteDatabase db;
                db = openOrCreateDatabase(rutaDB, MODE_PRIVATE, null);
                Cursor fila = db.rawQuery("SELECT nombre, direccion, fecha_hora, descripcion, activo FROM eventos WHERE id = '" + eventoID + "'", null);
                if (fila.getCount() > 0)
                {
                    fila.moveToFirst();
                    e = new evento(fila.getString(0), fila.getString(1), fila.getString(2), fila.getString(3), fila.getInt(4));
                    getLatLongFromAddress(e.getDireccion());
                }
            } catch (Exception ex) {
                String error = ex.getMessage();
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        this.ObtenEvento(idEvento);
    }

    private void getLatLongFromAddress(String address) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try
        {
            address += ", Spain";
            List<Address> addresses = geoCoder.getFromLocationName(address, 1);
            double latitude = (double) (addresses.get(0).getLatitude());
            double longitude = (double) (addresses.get(0).getLongitude());
            LatLng destino = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(destino).title(e.getNombre()+" || "+e.getFecha_hora()));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(destino));
        }
        catch (Exception e)
        {
            String exc = e.getMessage();
        }
    }
}