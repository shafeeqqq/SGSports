package com.example.shaf.sgsports;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public String type;

    public TimePickerFragment() {
    }

    @SuppressLint("ValidFragment")
    public TimePickerFragment(String type) {
        this.type = type;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            final Calendar c = Calendar.getInstance();

            c.set(Calendar.HOUR_OF_DAY, hourOfDay);
            c.set(Calendar.MINUTE, minute);

            SimpleDateFormat getDate = new SimpleDateFormat("HH:mm");
            String dateToDisplay = getDate.format(c.getTime());

            if (type.equals("from")){
                TextView mFrom = getActivity().findViewById(R.id.create_event_details_from);
                mFrom.setText(dateToDisplay);

            } else if (type.equals("to")) {
                TextView mFrom = getActivity().findViewById(R.id.create_event_details_to);
                mFrom.setText(dateToDisplay);
            }
    }

}
