package es.iessanvicente.eventos.myeventslistapp;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.widget.EditText;
import android.app.DialogFragment;
import android.app.Dialog;
import java.util.Calendar;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    EditText et;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        et = (EditText) getActivity().findViewById(R.id.etHora);

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        if( !String.valueOf( et.getText() ).equals( "" ) ){
            String[] split = String.valueOf( et.getText() ).split( ":" );
            hour = Integer.parseInt( split[ 0 ] );
            minute = Integer.parseInt( split[ 1 ] );
        }

        return new TimePickerDialog(getActivity(),this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {

        String formatHour = ( hourOfDay < 10) ? "0" + hourOfDay : String.valueOf( hourOfDay );
        String formatMinute = ( minute < 10) ? "0" + minute : String.valueOf( minute );
        et.setText( formatHour + ":" + formatMinute );

    }
}
