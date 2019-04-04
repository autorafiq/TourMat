package com.example.tour;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    public static final int FLAG_START_DATE = 0;
    public static final int FLAG_END_DATE = 1;
    private String date="";
    private String date1="";


    private int flag = 0;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        return new DatePickerDialog(getActivity(), this, year, month, day);
    }


    public void setFlag(int i) {
        this.flag = i;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (flag == FLAG_START_DATE) {
            date = format.format(calendar.getTime());

        } else if (flag == FLAG_END_DATE) {
            date1 = format.format(calendar.getTime());

        }

    }
    public String getA(){
        return this.date = date;
    }
    public String getA1(){
        return this.date1 = date1;
    }
}
