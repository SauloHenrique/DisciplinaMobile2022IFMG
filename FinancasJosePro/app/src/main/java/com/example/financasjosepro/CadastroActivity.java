package com.example.financasjosepro;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

public class CadastroActivity extends AppCompatActivity {

    private TextView dataTxt;
    private Calendar dataSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dataTxt = findViewById(R.id.dataCadTxt);

        configuraDataInicial();
        registrarEventos();
    }

    //trocar para LocalDateFormat
    private void configuraDataInicial(){
        dataSelecionada = Calendar.getInstance();

        SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");

        dataTxt.setText(sf.format(dataSelecionada.getTime()));
    }

    private void registrarEventos(){
        dataTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                configuraCalendario(dataTxt, dataSelecionada);
            }
        });
    }

    private void configuraCalendario(TextView campoTexto, Calendar calendario){
        DatePickerDialog.OnDateSetListener listener =
                new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                        calendario.set(ano, mes, dia);
                        campoTexto.setText(dia+"/"+mes+"/"+ano);
                    }
                };

        new DatePickerDialog(CadastroActivity.this,listener,
                calendario.get(Calendar.YEAR),
                calendario.get(Calendar.MONTH),
                calendario.get(Calendar.DAY_OF_MONTH)).show();
    }

}