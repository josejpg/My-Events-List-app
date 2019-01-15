package es.iessanvicente.eventos.myeventslistapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Jose Manuel Esparcia Cañizares
 * Jose J. Pardines Garcia
 */


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    EditText et;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        et = (EditText) getActivity().findViewById(R.id.etFecha);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if( !String.valueOf( et.getText() ).equals( "" ) ){
            String[] split = String.valueOf( et.getText() ).split( "/" );
            day = Integer.parseInt( split[ 0 ] );
            month = Integer.parseInt( split[ 1 ] ) - 1;
            year = Integer.parseInt( split[ 2 ] );
        }

        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day)
    {

        String formatYear = ( year < 10 ) ? "0" + year : String.valueOf( year );
        String formatMonth = ( month + 1 < 10 ) ? "0" + ( month + 1 ) : String.valueOf( month + 1 );
        String formatDay = ( day < 10 ) ? "0" + day : String.valueOf( day );
        et.setText( formatDay + "/" + formatMonth + "/" + formatYear );
    }
}