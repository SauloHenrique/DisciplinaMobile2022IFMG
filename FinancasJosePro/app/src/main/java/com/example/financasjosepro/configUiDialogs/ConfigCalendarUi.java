package com.example.financasjosepro.configUiDialogs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConfigCalendarUi {
    public static void configuraCalendario(TextView campoTexto, Calendar calendario, Context cont){
        DatePickerDialog.OnDateSetListener listener =
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                        calendario.set(ano, mes, dia);

                        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

                        campoTexto.setText(sf.format(calendario.getTime()));
                    }
                };

        new DatePickerDialog(cont,listener,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)).show();
    }
}
