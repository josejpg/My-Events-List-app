package es.iessanvicente.eventos.myeventslistapp;

import android.preference.PreferenceActivity;
import android.os.Bundle;

/**
 * Jose Manuel Esparcia Cañizares
 * Jose J. Pardines Garcia
 */


public class PreferenciasActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
    }
}
