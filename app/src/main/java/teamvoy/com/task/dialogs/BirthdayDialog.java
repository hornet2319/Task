package teamvoy.com.task.dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.Time;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

import teamvoy.com.task.Fragments.PersonalDataFragment;
import teamvoy.com.task.R;
import teamvoy.com.task.utils.PreferencesUtil;

/**
 * Created by lubomyrshershun on 8/19/15.
 */
public class BirthdayDialog {
    String data="";
    private Context context;
    private String message;
    public BirthdayDialog(Context context) {
        this.context=context;
    }
    public void setMessage(String message){
        this.message=message;
    }
    public void show() {
        // Process to get Current Date
        PersonalDataFragment.birthDayData="";
        int mYear, mMonth, mDay;
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        mDay=today.monthDay;
        mMonth=today.month;
        mYear=today.year;
        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int nYear,
                                          int monthOfYear, int dayOfMonth) {
                        data=dayOfMonth + "/" + (monthOfYear + 1) + "/" + nYear;
                        Log.d("BirthDayDialog data",data);
                        PersonalDataFragment.birthDayData=data;
                        PersonalDataFragment.updateBirth();

                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle("personal data");
        dpd.setMessage(message);
        dpd.setIcon(R.drawable.ic_settings);
        dpd.show();
        Log.d("BirthDayDialog result",data);
      ;
    }
}