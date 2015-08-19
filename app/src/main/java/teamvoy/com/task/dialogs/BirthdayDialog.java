package teamvoy.com.task.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

import teamvoy.com.task.utils.PreferencesUtil;

/**
 * Created by lubomyrshershun on 8/19/15.
 */
public class BirthdayDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    Context context;
    private String data;
    private int year;
    private int month;
    private int day;
    private PreferencesUtil mPrefs;

    public BirthdayDialog() {
    }

    public BirthdayDialog(Context context) {
       this.context=context;
        mPrefs=PreferencesUtil.getInstance(context);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(context, this, year, month, day);
    }


    void write(String data) {
    mPrefs.setBirthDay(data);
    }

    @Override
    public void onDateSet(DatePicker view, int selectedYear,
                          int selectedMonth, int selectedDay) {
        year  = selectedYear;
        month = selectedMonth;
        day   = selectedDay;

        // Show selected date
        data=""+day+"/"+(month+1)+"/"+year;
        write(data);
    }
}
